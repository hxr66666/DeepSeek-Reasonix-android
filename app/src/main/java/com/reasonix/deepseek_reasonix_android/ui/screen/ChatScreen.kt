package com.reasonix.deepseek_reasonix_android.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reasonix.deepseek_reasonix_android.data.model.SessionInfo
import com.reasonix.deepseek_reasonix_android.data.model.StatusInfo
import com.reasonix.deepseek_reasonix_android.ui.components.*
import com.reasonix.deepseek_reasonix_android.ui.viewmodel.ChatViewModel

// ═══════════════════════════════════════════════
// 调色板 — 匹配 index.html 的 Reasonix 暗色主题
// ═══════════════════════════════════════════════

private val Bg       = Color(0xFF1C1A1B)
private val Bg2      = Color(0xFF222022)
private val Panel    = Color(0xFF2A2729)
private val Panel2   = Color(0xFF2E2C2E)
private val Card     = Color(0xFF282528)
private val CardHover= Color(0xFF302E30)
private val Border   = Color(0xFF3D3938)
private val BorderStr= Color(0xFF5A5452)
private val Accent   = Color(0xFFEA8800)
private val AccentS  = Color(0x26EA8800)
private val Violet   = Color(0xFF9B6FD8)
private val Fg       = Color(0xFFF5F2F0)
private val Fg2      = Color(0xFFCCC5C0)
private val Muted    = Color(0xFF9E9896)
private val Muted2   = Color(0xFF7A7270)
private val Danger    = Color(0xFFE04636)
private val DangerS   = Color(0x29E04636)
private val Success   = Color(0xFF40A060)
private val SuccessS  = Color(0x2440A060)
private val Warning   = Color(0xFFE5B830)

// ═══════════════════════════════════════════════
// ChatScreen — 主界面入口
// ═══════════════════════════════════════════════

@Composable
fun ChatScreen(initialServerUrl: String = "http://127.0.0.1:8920", viewModel: ChatViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    // 首次启动时配置服务器地址
    LaunchedEffect(Unit) {
        if (state.serverUrl != initialServerUrl) {
            viewModel.configureServer(initialServerUrl)
        }
    }

    val focusRequester = remember { FocusRequester() }

    // 全局键盘事件处理
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .imePadding()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyUp) return@onKeyEvent false
                when {
                    // / 聚焦输入框
                    event.key == Key.Slash && state.isStreaming -> {
                        viewModel.onInputChange("/")
                        focusRequester.requestFocus()
                        true
                    }
                    // Esc → 取消流式 / 双 Esc 倒带
                    event.key == Key.Escape -> {
                        if (state.isStreaming) {
                            viewModel.cancelStreaming()
                        } else {
                            viewModel.tryDoubleEscRewind()
                        }
                        true
                    }
                    // Shift+Tab → 切计划模式
                    event.key == Key.Tab && event.isShiftPressed && !state.isStreaming -> {
                        viewModel.togglePlanMode()
                        true
                    }
                    // Ctrl+Y → 切 YOLO
                    event.key == Key.Y && event.isCtrlPressed && !state.isStreaming -> {
                        val newMode = if (state.toolApprovalMode == "yolo") "auto" else "yolo"
                        viewModel.setToolApprovalMode(newMode)
                        true
                    }
                    else -> false
                }
            }
    ) {
        // ── 主内容区 ──
        Column(modifier = Modifier.fillMaxSize()) {
            // 欢迎页（无消息时） 或 消息列表
            if (state.messages.isEmpty() && !state.isStreaming) {
                WelcomeScreen(
                    onPromptClick = { prompt ->
                        viewModel.onInputChange(prompt)
                        viewModel.sendMessage()
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
            MessageList(
                items = state.messages,
                modifier = Modifier.weight(1f),
                balance = state.status?.balance?.display,
                onApprove = { session, persist, scope ->
                    val approval = state.messages.lastOrNull {
                        it is com.reasonix.deepseek_reasonix_android.data.model.ChatItem.ApprovalCard
                    } as? com.reasonix.deepseek_reasonix_android.data.model.ChatItem.ApprovalCard
                    approval?.let { viewModel.approveTool(it.id, session, persist, scope) }
                },
                onDeny = {
                    val approval = state.messages.lastOrNull {
                        it is com.reasonix.deepseek_reasonix_android.data.model.ChatItem.ApprovalCard
                    } as? com.reasonix.deepseek_reasonix_android.data.model.ChatItem.ApprovalCard
                    approval?.let { viewModel.denyTool(it.id) }
                },
                onAskSubmit = { answers ->
                    val ask = state.messages.lastOrNull {
                        it is com.reasonix.deepseek_reasonix_android.data.model.ChatItem.AskCard
                    } as? com.reasonix.deepseek_reasonix_android.data.model.ChatItem.AskCard
                    ask?.let { viewModel.submitAskAnswers(it.id, answers) }
                }
            )
            }

            // 底部输入区域
            Footer(
                inputText = state.inputText,
                onInputChange = { viewModel.onInputChange(it) },
                onSend = { viewModel.sendMessage() },
                onCancel = { viewModel.cancelStreaming() },
                isStreaming = state.isStreaming,
                planMode = state.planMode,
                toolApprovalMode = state.toolApprovalMode,
                onTogglePlan = { viewModel.togglePlanMode() },
                onToggleBypass = {
                    val newMode = if (state.toolApprovalMode == "yolo") "auto" else "yolo"
                    viewModel.setToolApprovalMode(newMode)
                },
                onToggleAuto = { viewModel.setToolApprovalMode("auto") },
                serverUrl = state.serverUrl,
                onServerUrlChange = { viewModel.onServerUrlChange(it) },
                isConnected = state.error == null,
                cumulativeCost = state.cumulativeCost,
                cumulativeTokens = state.cumulativeTokens,
                balance = state.status?.balance?.display,
                focusRequester = focusRequester
            )
        }

        // ── 侧边栏悬浮面板 ──
        AnimatedVisibility(
            visible = state.showSidebar,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it }),
            modifier = Modifier.zIndex(10f)
        ) {
            Sidebar(
                sessions = state.sessions,
                status = state.status,
                isStreaming = state.isStreaming,
                cumulativeCost = state.cumulativeCost,
                onNewSession = {
                    viewModel.newSession()
                    viewModel.toggleSidebar()
                },
                onSelectSession = {
                    viewModel.selectSession(it)
                    viewModel.toggleSidebar()
                },
                onDeleteSession = { viewModel.deleteSession(it) },
                onCompact = { viewModel.compactConversation() },
                onRewind = { viewModel.showRewindPicker() },
                onFork = { viewModel.showRewindPicker() },
                onStats = { viewModel.showStatsDialog() },
                modifier = Modifier.width(220.dp)
            )
        }

        // ── 侧边栏遮罩 ──
        if (state.showSidebar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(9f)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable { viewModel.toggleSidebar() }
            )
        }

        // ── Slash 菜单（悬浮在输入框上方，向上展开） ──
        val slashPrefix = remember(state.inputText) {
            if (state.inputText.startsWith("/") && !state.inputText.contains(" ")) {
                state.inputText.removePrefix("/")
            } else null
        }
        if (slashPrefix != null) {
            SlashMenu(
                prefix = slashPrefix,
                onSelect = { command ->
                    viewModel.onInputChange(command + " ")
                    focusRequester.requestFocus()
                },
                onDismiss = {},
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 100.dp)
                    .zIndex(8f)
            )
        }

        // ── 侧边栏切换按钮（左侧 2/5 高度处） ──
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = maxHeight * 0.4f, start = 8.dp)
                .zIndex(5f)
                .size(48.dp)
                .clip(CircleShape)
                .background(Bg2.copy(alpha = 0.9f))
                .border(1.dp, Border, CircleShape)
                .clickable { viewModel.toggleSidebar() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (state.showSidebar) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = if (state.showSidebar) "Close sidebar" else "Open sidebar",
                tint = if (state.showSidebar) Accent else Muted,
                modifier = Modifier.size(26.dp)
            )
        }

        // ── Rewind Picker 对话框 ──
        if (state.showRewindPicker) {
            RewindPickerDialog(
                checkpoints = state.checkpoints,
                onRewind = { turn, scope -> viewModel.rewindTo(turn, scope) },
                onFork = { turn -> viewModel.forkAt(turn) },
                onSummarize = { turn, mode -> viewModel.summarizeAt(turn, mode) },
                onDismiss = { viewModel.dismissRewindPicker() }
            )
        }

        // ── Stats 对话框 ──
        if (state.showStatsDialog) {
            StatsDialog(
                status = state.status,
                sessionCount = state.sessions.size,
                cumulativeTokens = state.cumulativeTokens,
                cumulativeCost = state.cumulativeCost,
                cumulativeCacheHit = state.cumulativeCacheHit,
                cumulativeCacheMiss = state.cumulativeCacheMiss,
                onDismiss = { viewModel.dismissStatsDialog() }
            )
        }
    }
}

// ═══════════════════════════════════════════════
// Sidebar
// ═══════════════════════════════════════════════

@Composable
private fun Sidebar(
    sessions: List<SessionInfo>,
    status: StatusInfo?,
    isStreaming: Boolean,
    cumulativeCost: Double,
    onNewSession: () -> Unit,
    onSelectSession: (String) -> Unit,
    onDeleteSession: (String) -> Unit,
    onCompact: () -> Unit,
    onRewind: () -> Unit,
    onFork: () -> Unit,
    onStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Bg2)
            .border(1.dp, Border)
    ) {
        // ── Brand ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(Accent, Violet)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("R", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.width(9.dp))
            Text("Reasonix", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Fg)
        }

        HorizontalDivider(color = Border, thickness = 1.dp)

        // ── 导航 ──
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            SidebarItem("新建会话", onClick = onNewSession, accent = true)
            SidebarItem("压缩", onClick = onCompact)
            SidebarItem("倒带", onClick = onRewind)
            SidebarItem("分支", onClick = onFork)

            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(color = Border, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

            SidebarItem("统计", onClick = onStats)
        }

        // ── 会话标签 ──
        Text(
            "会话",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = Muted,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
        )

        // ── 会话列表 ──
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            if (sessions.isEmpty()) {
                Text(
                    "无会话",
                    fontSize = 12.sp,
                    color = Muted2,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                sessions.forEach { session ->
                    SessionRow(
                        session = session,
                        isStreaming = isStreaming,
                        onSelect = { onSelectSession(session.path) },
                        onDelete = { onDeleteSession(session.name) }
                    )
                }
            }
        }

        // ── 底部状态 ──
        HorizontalDivider(color = Border, thickness = 1.dp)

        Column(modifier = Modifier.padding(8.dp)) {
            Text("状态", fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp, color = Muted,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))

            // 上下文用量条
            val used = status?.used ?: 0
            val window = status?.window ?: 0
            if (window > 0) {
                val pct = (used.toFloat() / window).coerceIn(0f, 1f)
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                    // 3 段颜色进度条
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(Panel2)
                    ) {
                        val barColor = when {
                            pct > 0.83f -> Danger
                            pct > 0.6f  -> Warning
                            else        -> Accent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(pct)
                                .height(3.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(barColor)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(fmtTok(used), fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Muted2)
                        Text(fmtTok(window), fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Muted2)
                    }
                }
            }

            // ── 状态指标 ──
            val cacheTotal = (status?.cacheHit ?: 0) + (status?.cacheMiss ?: 0)
            if (cacheTotal > 0) {
                val cachePct = (status!!.cacheHit.toFloat() / cacheTotal * 100).toInt()
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("缓存", fontSize = 11.sp, color = Muted2)
                    Text(
                        "$cachePct%",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = if (cachePct > 50) Success else Muted2
                    )
                }
            }

            if (cumulativeCost > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("费用", fontSize = 11.sp, color = Muted2)
                    Text(
                        fmtMoney(cumulativeCost),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Fg2
                    )
                }
            }

            status?.balance?.display?.let { bal ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("余额", fontSize = 11.sp, color = Muted2)
                    Text(
                        bal,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Fg2
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 状态指示器
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (isStreaming) Accent else Muted2)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = status?.label ?: "-",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Muted
                )
            }
        }
    }
}

@Composable
private fun SidebarItem(label: String, onClick: () -> Unit, accent: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (accent) Accent else Card)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = if (accent) FontWeight.Medium else FontWeight.Normal,
            color = if (accent) Color.White else Fg2
        )
    }
}

@Composable
private fun SessionRow(
    session: SessionInfo,
    isStreaming: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    var showDelete by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (session.current) AccentS else Card)
            .combinedClickable(
                onClick = { if (!isStreaming && !session.current) onSelect() },
                onLongClick = { showDelete = !showDelete }
            )
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = session.title ?: session.name.take(30),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (session.current) Accent else Fg2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (showDelete) {
            Text(
                "×",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Danger,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable { onDelete(); showDelete = false }
            )
        }
    }
}

// ═══════════════════════════════════════════════
// Footer — 工具栏 + 输入框 + SlashMenu
// ═══════════════════════════════════════════════

@Composable
private fun Footer(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    isStreaming: Boolean,
    planMode: Boolean,
    toolApprovalMode: String,
    onTogglePlan: () -> Unit,
    onToggleBypass: (() -> Unit)?,
    onToggleAuto: (() -> Unit)?,
    serverUrl: String,
    onServerUrlChange: (String) -> Unit,
    isConnected: Boolean,
    cumulativeCost: Double,
    cumulativeTokens: Long,
    balance: String?,
    focusRequester: FocusRequester
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Bg)
            .border(1.dp, Border)
    ) {
            // ── 工具栏 ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Auto
                ToolbarButton("Auto", active = toolApprovalMode == "auto", accent = false) { onToggleAuto?.invoke() }
                // Plan
                ToolbarButton("Plan", active = planMode, accent = false) { onTogglePlan() }
                // YOLO
                ToolbarButton("YOLO", active = toolApprovalMode == "yolo", danger = true) { onToggleBypass?.invoke() }

                // 分隔
                Box(modifier = Modifier.width(1.dp).height(16.dp).background(Border))

                // 状态
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(if (isStreaming) Accent else Success)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isStreaming) "思考中…" else "就绪",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Muted
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // turn info + balance
                if (cumulativeTokens > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "T:${fmtTok(cumulativeTokens)}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Muted2
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (cumulativeCost > 0.0) {
                    Text(
                        text = fmtMoney(cumulativeCost),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Muted2
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                balance?.let { b ->
                    Text(
                        text = b,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Success,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // 服务器地址
                Text(
                    text = serverUrl,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Muted2
                )
            }

            // ── 输入框 ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Card)
                    .border(1.dp, BorderStr, RoundedCornerShape(14.dp))
                    .padding(start = 14.dp, top = 2.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "›",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Accent,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(6.dp))

                BasicTextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .padding(vertical = 10.dp)
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyUp &&
                                event.key == Key.Enter &&
                                !event.isShiftPressed &&
                                inputText.isNotBlank()
                            ) {
                                onSend()
                                true
                            } else false
                        },
                    textStyle = TextStyle(
                        color = Fg,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = SolidColor(Accent),
                    singleLine = false,
                    maxLines = 5,
                    decorationBox = { innerTextField ->
                        Box {
                            if (inputText.isEmpty()) {
                                Text(
                                    "输入消息…  / 查看命令",
                                    color = Muted2,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // 发送/停止按钮
                if (isStreaming) {
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Danger),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White)
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = onSend,
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    if (inputText.isNotBlank()) Accent else Panel2
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("↑", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
}

@Composable
private fun ToolbarButton(
    label: String,
    active: Boolean,
    accent: Boolean = false,
    danger: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = when {
            active && danger -> DangerS
            active -> AccentS
            else -> Bg2
        },
        border = if (active) null else androidx.compose.foundation.BorderStroke(1.dp, Border),
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(onClick = onClick)
    ) {
        Text(
            label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = when {
                active && danger -> Danger
                active -> Accent
                else -> Muted
            },
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
        )
    }
}

// ═══════════════════════════════════════════════
// 工具函数
// ═══════════════════════════════════════════════

private fun fmtTok(n: Long): String =
    if (n >= 1000) "%.1fk".format(n / 1000.0) else "$n"

private fun fmtMoney(n: Double): String = when {
    n >= 1.0   -> "¥%.2f".format(n)
    n >= 0.01  -> "¥%.4f".format(n)
    else       -> "¥%.6f".format(n)
}
