package com.reasonix.deepseek_reasonix_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reasonix.deepseek_reasonix_android.data.api.ReasonixApi
import com.reasonix.deepseek_reasonix_android.data.api.ReasonixSseClient
import com.reasonix.deepseek_reasonix_android.data.model.*
import com.reasonix.deepseek_reasonix_android.data.repository.ChatRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatItem> = emptyList(),
    val sessions: List<SessionInfo> = emptyList(),
    val status: StatusInfo? = null,
    val isStreaming: Boolean = false,
    val planMode: Boolean = false,
    val toolApprovalMode: String = "auto",
    val inputText: String = "",
    val serverUrl: String = "http://127.0.0.1:8920",
    val showSidebar: Boolean = false,
    val showRewindPicker: Boolean = false,
    val checkpoints: List<CheckpointInfo> = emptyList(),
    val showStatsDialog: Boolean = false,
    val cumulativeTokens: Long = 0,
    val cumulativeCost: Double = 0.0,
    val cumulativeCacheHit: Long = 0,
    val cumulativeCacheMiss: Long = 0,
    val error: String? = null
)

class ChatViewModel(
    initialServerUrl: String = "http://127.0.0.1:8920"
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(serverUrl = initialServerUrl))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var repository: ChatRepository = createRepository(initialServerUrl)
    private var sseCollectionJob: Job? = null

    // 当前流的助手消息 builder（增量）
    private var currentAssistantMsgIndex: Int? = null
    private var pendingReasoning: StringBuilder? = null
    private var pendingContent: StringBuilder? = null
    private var pendingToolCards: MutableMap<String, ChatItem.ToolCard> = mutableMapOf()

    // 双 Esc 倒带
    private var lastEscTime: Long = 0L
    private val doubleEscWindowMs: Long = 600

    init {
        loadInitialData()
    }

    // ── 服务器配置 ──

    /** 动态切换服务器地址，重建 API/SSE 客户端并重新加载数据。 */
    fun configureServer(url: String) {
        val normalized = url.trimEnd('/')
        repository = createRepository(normalized)
        _uiState.update { it.copy(serverUrl = normalized, messages = emptyList(), error = null) }
        loadInitialData()
    }

    private fun createRepository(url: String): ChatRepository {
        return ChatRepository(
            api = ReasonixApi(url),
            sseClient = ReasonixSseClient(url)
        )
    }

    // ── 初始化 ──

    private fun loadInitialData() {
        viewModelScope.launch {
            val sessions = repository.getSessions()
            val status = repository.getStatus()
            val history = repository.getHistory()

            val historyItems = buildHistoryItems(history)

            _uiState.update {
                it.copy(
                    sessions = sessions,
                    status = status,
                    messages = historyItems,
                    planMode = status?.plan ?: false,
                    toolApprovalMode = status?.toolApprovalMode ?: "auto"
                )
            }
        }
    }

    /** 将后端历史记录转换为 ChatItem 列表，含工具调用/结果配对。 */
    private fun buildHistoryItems(history: List<HistoryMessage>): List<ChatItem> {
        val toolResults = history
            .filter { it.role == "tool" && it.toolCallId != null }
            .associateBy { it.toolCallId!! }

        return history.flatMap { hist ->
            when (hist.role) {
                "user" -> listOf(ChatItem.UserMessage(hist.content ?: ""))
                "assistant" -> {
                    val items = mutableListOf<ChatItem>()
                    hist.toolCalls?.forEach { tc ->
                        val result = toolResults[tc.id]
                        val isRunning = result == null
                        items.add(
                            ChatItem.ToolCard(
                                id = tc.id,
                                name = tc.name,
                                args = tc.arguments,
                                output = result?.content,
                                isRunning = isRunning,
                                expanded = !isRunning
                            )
                        )
                    }
                    val content = hist.content.orEmpty()
                    val reasoning = hist.reasoning
                    if (content.isNotBlank() || reasoning != null) {
                        items.add(
                            ChatItem.AssistantMessage(
                                content = content,
                                reasoning = reasoning
                            )
                        )
                    }
                    items
                }
                "system" -> listOf(ChatItem.SystemNotice(hist.content ?: ""))
                else -> emptyList()
            }
        }
    }

    // ── 输入 ──

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onServerUrlChange(url: String) {
        _uiState.update { it.copy(serverUrl = url) }
    }

    // ── 发送消息 ──

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        _uiState.update {
            it.copy(
                inputText = "",
                isStreaming = true,
                error = null
            )
        }

        // Slash 命令解析
        if (parseSlashCommand(text)) {
            _uiState.update { it.copy(isStreaming = false) }
            return
        }

        // 添加用户消息
        appendMessage(ChatItem.UserMessage(text))

        // 初始化流式缓冲区
        currentAssistantMsgIndex = null
        pendingContent = StringBuilder()
        pendingReasoning = StringBuilder()
        pendingToolCards.clear()

        // 提交消息 → 启动 SSE 监听
        viewModelScope.launch {
            try {
                repository.submit(text)
                startSseCollection()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                appendMessage(ChatItem.ErrorMessage("提交失败: ${e.message}"))
                _uiState.update { it.copy(isStreaming = false) }
            }
        }
    }

    /** 解析并执行斜杠命令。返回 true 表示已处理（不发往服务器）。 */
    private fun parseSlashCommand(text: String): Boolean {
        val cmd = text.trim()
        when {
            cmd == "/help" -> {
                appendMessage(ChatItem.SystemNotice("可用命令: /plan /yolo /auto /compact /compact auto /compact manual /rewind /fork /new /stats /status /sessions /summarize /resume /delete /theme /help"))
                return true
            }
            cmd == "/plan" -> {
                togglePlanMode()
                return true
            }
            cmd == "/yolo" -> {
                setToolApprovalMode("yolo")
                appendMessage(ChatItem.SystemNotice("已切换到 YOLO 模式"))
                return true
            }
            cmd == "/auto" -> {
                setToolApprovalMode("auto")
                appendMessage(ChatItem.SystemNotice("已切换到自动审批模式"))
                return true
            }
            cmd == "/compact" -> {
                compactConversation()
                return true
            }
            cmd == "/rewind" -> {
                showRewindPicker()
                return true
            }
            cmd == "/fork" -> {
                viewModelScope.launch {
                    repository.fork(1)
                    loadInitialData()
                }
                return true
            }
            cmd == "/new" -> {
                newSession()
                return true
            }
            cmd == "/stats" -> {
                _uiState.update { it.copy(showStatsDialog = true) }
                return true
            }
            cmd == "/status" -> {
                viewModelScope.launch {
                    val s = repository.getStatus()
                    appendMessage(ChatItem.SystemNotice(
                        "模型=${s?.label ?: "?"} 计划=${s?.plan ?: false} 模式=${s?.toolApprovalMode ?: "auto"} " +
                        "Token=${s?.used ?: 0}/${s?.window ?: 0}"
                    ))
                }
                return true
            }
            cmd == "/sessions" -> {
                viewModelScope.launch {
                    val s = repository.getSessions()
                    val list = s.joinToString("\n") { "- ${it.name}${if (it.current) " (当前)" else ""}" }
                    appendMessage(ChatItem.SystemNotice(list.ifBlank { "无会话" }))
                }
                return true
            }
            cmd.startsWith("/summarize") -> {
                viewModelScope.launch {
                    repository.summarize(1, "from")
                    appendMessage(ChatItem.SystemNotice("正在总结…"))
                }
                return true
            }
            cmd.startsWith("/delete ") -> {
                val name = cmd.removePrefix("/delete ").trim()
                viewModelScope.launch {
                    repository.deleteSession(name)
                    loadInitialData()
                    appendMessage(ChatItem.SystemNotice("已删除会话: $name"))
                }
                return true
            }
            cmd.startsWith("/resume ") -> {
                val name = cmd.removePrefix("/resume ").trim()
                viewModelScope.launch {
                    repository.resumeSession(name)
                    loadInitialData()
                    appendMessage(ChatItem.SystemNotice("已恢复会话: $name"))
                }
                return true
            }
            cmd == "/compact auto" -> {
                appendMessage(ChatItem.SystemNotice("已设置压缩模式: 自动"))
                return true
            }
            cmd == "/compact manual" -> {
                appendMessage(ChatItem.SystemNotice("已设置压缩模式: 手动"))
                return true
            }
            cmd == "/theme" -> {
                appendMessage(ChatItem.SystemNotice("主题切换功能开发中…"))
                return true
            }
        }
        return false
    }

    // ── SSE 事件处理 ──

    private fun startSseCollection() {
        sseCollectionJob?.cancel()
        sseCollectionJob = viewModelScope.launch {
            try {
                repository.sseEvents().collect { event ->
                    handleSseEvent(event)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (e.message?.contains("closed") != true) {
                    appendMessage(ChatItem.ErrorMessage("连接错误: ${e.message}"))
                }
            } finally {
                finalizeTurn()
            }
        }
    }

    private fun handleSseEvent(event: SseEvent) {
        when (event.kind) {
            "turn_started" -> {
                currentAssistantMsgIndex = null
                pendingContent = StringBuilder()
                pendingReasoning = StringBuilder()
                pendingToolCards.clear()
            }

            "reasoning" -> {
                event.reasoning?.let { r ->
                    pendingReasoning?.append(r)
                    updatePendingAssistant()
                }
            }

            "text" -> {
                event.text?.let { t ->
                    pendingContent?.append(t)
                    updatePendingAssistant()
                }
            }

            "message" -> {
                event.message?.let { msg ->
                    pendingContent = StringBuilder(msg.content ?: "")
                    pendingReasoning = if (msg.reasoning != null) StringBuilder(msg.reasoning) else null
                    updatePendingAssistant()
                }
            }

            "tool_dispatch" -> {
                event.tool?.let { tool ->
                    val card = ChatItem.ToolCard(
                        id = tool.id,
                        name = tool.name,
                        args = tool.args ?: tool.arguments,
                        isRunning = true
                    )
                    pendingToolCards[tool.id] = card
                    appendMessage(card)
                }
            }

            "tool_result" -> {
                event.tool?.let { tool ->
                    val card = ChatItem.ToolCard(
                        id = tool.id,
                        name = tool.name,
                        output = tool.output,
                        err = tool.err,
                        truncated = tool.truncated,
                        isRunning = false
                    )
                    pendingToolCards[tool.id] = card
                    replaceToolCard(tool.id, card)
                }
            }

            "tool_progress" -> {
                event.tool?.let { tool ->
                    val card = ChatItem.ToolCard(
                        id = tool.id,
                        name = tool.name,
                        output = tool.output,
                        isRunning = true
                    )
                    pendingToolCards[tool.id] = card
                    replaceToolCard(tool.id, card)
                }
            }

            "usage" -> {
                event.usage?.let { u ->
                    appendMessage(ChatItem.UsageStats(u))
                    _uiState.update { state ->
                        state.copy(
                            cumulativeTokens = state.cumulativeTokens + u.totalTokens,
                            cumulativeCost = state.cumulativeCost + (u.costUsd ?: u.cost ?: 0.0),
                            cumulativeCacheHit = state.cumulativeCacheHit + u.cacheHitTokens,
                            cumulativeCacheMiss = state.cumulativeCacheMiss + u.cacheMissTokens
                        )
                    }
                }
            }

            "notice" -> {
                val text = event.text ?: event.err ?: return
                val isWarning = event.level == "warning"
                appendMessage(ChatItem.SystemNotice(text, isWarning))
            }

            "phase" -> {
                event.text?.let { txt ->
                    appendMessage(ChatItem.PhaseIndicator(txt))
                }
            }

            "approval_request" -> {
                event.approval?.let { a ->
                    appendMessage(ChatItem.ApprovalCard(a.id, a.tool, a.subject))
                }
            }

            "ask_request" -> {
                event.ask?.let { a ->
                    appendMessage(ChatItem.AskCard(a.id, a.questions))
                }
            }

            "compaction_started", "compaction_done" -> {
                event.compaction?.let { c ->
                    appendMessage(ChatItem.CompactionNotice(c.trigger, c.summary, c.messages))
                }
                if (event.kind == "compaction_done") {
                    viewModelScope.launch {
                        val history = repository.getHistory()
                        rebuildFromHistory(history)
                    }
                }
            }

            "turn_done" -> {
                finalizeTurn()
            }
        }
    }

    private fun updatePendingAssistant() {
        val content = pendingContent?.toString() ?: ""
        val reasoning = pendingReasoning?.toString()?.takeIf { it.isNotBlank() }

        val msg = ChatItem.AssistantMessage(
            content = content,
            reasoning = reasoning
        )

        val idx = currentAssistantMsgIndex
        if (idx != null) {
            _uiState.update { state ->
                if (idx < state.messages.size && state.messages[idx] is ChatItem.AssistantMessage) {
                    val updated = state.messages.toMutableList()
                    updated[idx] = msg
                    state.copy(messages = updated)
                } else {
                    // 索引失效（如 compaction 重建），回退到追加
                    val newList = state.messages + msg
                    currentAssistantMsgIndex = newList.size - 1
                    state.copy(messages = newList)
                }
            }
        } else {
            // 本轮首次助手消息 — 追加到列表末尾
            _uiState.update { state ->
                val newList = state.messages + msg
                currentAssistantMsgIndex = newList.size - 1
                state.copy(messages = newList)
            }
        }
    }

    private fun appendMessage(item: ChatItem) {
        _uiState.update { state ->
            state.copy(messages = state.messages + item)
        }
    }

    private fun replaceToolCard(id: String, card: ChatItem.ToolCard) {
        _uiState.update { state ->
            val list = state.messages.toMutableList()
            val idx = list.indexOfLast {
                it is ChatItem.ToolCard && it.id == id
            }
            if (idx >= 0) list[idx] = card
            state.copy(messages = list)
        }
    }

    private fun rebuildFromHistory(history: List<HistoryMessage>) {
        currentAssistantMsgIndex = null
        val items = buildHistoryItems(history)
        _uiState.update { it.copy(messages = items) }
    }

    private fun finalizeTurn() {
        currentAssistantMsgIndex = null
        pendingContent = null
        pendingReasoning = null
        _uiState.update { it.copy(isStreaming = false) }
        sseCollectionJob?.cancel()
    }

    // ── 倒带 / 分叉 / 总结 ──

    fun showRewindPicker() {
        viewModelScope.launch {
            try {
                val checkpoints = repository.getCheckpoints()
                _uiState.update { it.copy(checkpoints = checkpoints, showRewindPicker = true) }
            } catch (e: Exception) {
                appendMessage(ChatItem.ErrorMessage("加载检查点失败: ${e.message}"))
            }
        }
    }

    fun dismissRewindPicker() {
        _uiState.update { it.copy(showRewindPicker = false) }
    }

    fun rewindTo(turn: Int, scope: String = "both") {
        viewModelScope.launch {
            repository.rewind(turn, scope)
            loadInitialData()
            _uiState.update { it.copy(showRewindPicker = false) }
        }
    }

    fun forkAt(turn: Int) {
        viewModelScope.launch {
            repository.fork(turn)
            loadInitialData()
            _uiState.update { it.copy(showRewindPicker = false) }
        }
    }

    fun summarizeAt(turn: Int, mode: String) {
        viewModelScope.launch {
            repository.summarize(turn, mode)
            _uiState.update { it.copy(showRewindPicker = false) }
            appendMessage(ChatItem.SystemNotice("正在从第 $turn 轮总结…"))
        }
    }

    // ── 统计对话框 ──

    fun showStatsDialog() {
        _uiState.update { it.copy(showStatsDialog = true) }
    }

    fun dismissStatsDialog() {
        _uiState.update { it.copy(showStatsDialog = false) }
    }

    // ── 双 Esc 倒带 ──

    fun tryDoubleEscRewind(): Boolean {
        if (_uiState.value.isStreaming || _uiState.value.inputText.isNotEmpty()) {
            return false
        }
        val now = System.currentTimeMillis()
        val within = (now - lastEscTime) < doubleEscWindowMs
        lastEscTime = now
        if (within) {
            showRewindPicker()
            return true
        }
        return false
    }

    // ── 操作 ──

    fun cancelStreaming() {
        viewModelScope.launch {
            repository.cancel()
            finalizeTurn()
        }
    }

    fun togglePlanMode() {
        val newPlan = !_uiState.value.planMode
        _uiState.update { it.copy(planMode = newPlan) }
        viewModelScope.launch { repository.setPlan(newPlan) }
    }

    fun setToolApprovalMode(mode: String) {
        _uiState.update { it.copy(toolApprovalMode = mode) }
        viewModelScope.launch { repository.setToolApprovalMode(mode) }
    }

    fun approveTool(id: String, session: Boolean = false, persist: Boolean = false, scope: String = "") {
        viewModelScope.launch {
            repository.approve(id, true, session, persist, scope)
            _uiState.update { state ->
                state.copy(messages = state.messages.filter {
                    it !is ChatItem.ApprovalCard || it.id != id
                })
            }
        }
    }

    fun denyTool(id: String) {
        viewModelScope.launch {
            repository.approve(id, false)
            _uiState.update { state ->
                state.copy(messages = state.messages.filter {
                    it !is ChatItem.ApprovalCard || it.id != id
                })
            }
        }
    }

    fun submitAskAnswers(id: String, answers: List<Map<String, String>>) {
        val formattedAnswers = answers.map { map ->
            map.mapValues { it.value }.toMap()
        }.map { it.mapValues { e -> e.value as Any } }

        viewModelScope.launch {
            repository.answer(id, formattedAnswers)
            _uiState.update { state ->
                state.copy(messages = state.messages.filter {
                    it !is ChatItem.AskCard || it.id != id
                })
            }
        }
    }

    fun newSession() {
        viewModelScope.launch {
            repository.newSession()
            _uiState.update { it.copy(messages = emptyList()) }
            loadInitialData()
        }
    }

    fun selectSession(path: String) {
        viewModelScope.launch {
            repository.resumeSession(path)
            loadInitialData()
        }
    }

    fun deleteSession(name: String) {
        viewModelScope.launch {
            repository.deleteSession(name)
            loadInitialData()
        }
    }

    fun compactConversation() {
        viewModelScope.launch {
            repository.compact()
        }
    }

    fun toggleSidebar() {
        _uiState.update { it.copy(showSidebar = !it.showSidebar) }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectSse()
    }
}
