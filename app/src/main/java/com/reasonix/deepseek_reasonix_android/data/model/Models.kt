package com.reasonix.deepseek_reasonix_android.data.model

import com.google.gson.annotations.SerializedName

// ── SSE 事件类型 ──

enum class SseEventKind {
    turn_started,
    reasoning,
    text,
    message,
    tool_dispatch,
    tool_result,
    tool_progress,
    usage,
    notice,
    phase,
    approval_request,
    ask_request,
    compaction_started,
    compaction_done,
    turn_done,
    @SerializedName("") unknown
}

data class SseEvent(
    val kind: String = "",
    val text: String? = null,
    val reasoning: String? = null,
    val err: String? = null,
    val level: String? = null,
    val tool: ToolPayload? = null,
    val usage: UsagePayload? = null,
    val approval: ApprovalPayload? = null,
    val ask: AskPayload? = null,
    val compaction: CompactionPayload? = null,
    val message: MessagePayload? = null
)

// ── 工具相关 ──

data class ToolPayload(
    val id: String = "",
    val name: String = "",
    val args: String? = null,
    val arguments: String? = null,
    val output: String? = null,
    val err: String? = null,
    val truncated: Boolean = false,
    val readOnly: Boolean = false,
    val subject: String? = null
)

// ── 用量统计 ──

data class UsagePayload(
    val totalTokens: Long = 0,
    val promptTokens: Long = 0,
    val completionTokens: Long = 0,
    val cacheHitTokens: Long = 0,
    val cacheMissTokens: Long = 0,
    val cost: Double? = null,
    val costUsd: Double? = null,
    val currency: String? = null
)

// ── 审批 ──

data class ApprovalPayload(
    val id: String = "",
    val tool: String = "",
    val subject: String? = null
)

// ── 提问卡片 ──

data class AskPayload(
    val id: String = "",
    val questions: List<AskQuestion> = emptyList()
)

data class AskQuestion(
    val id: String = "",
    val prompt: String = "",
    val multi: Boolean = false,
    val options: List<AskOption> = emptyList()
)

data class AskOption(
    val label: String = "",
    val description: String? = null
)

// ── 压缩通知 ──

data class CompactionPayload(
    val trigger: String? = null,
    val summary: String? = null,
    val messages: Int = 0
)

// ── 消息（历史记录用） ──

data class MessagePayload(
    val role: String = "",
    val content: String? = null,
    val reasoning: String? = null
)

// ── 历史消息 ──

data class HistoryMessage(
    val role: String = "",
    val content: String? = null,
    val reasoning: String? = null,
    val toolCalls: List<ToolCallPayload>? = null,
    val toolCallId: String? = null,
    val toolName: String? = null
)

data class ToolCallPayload(
    val id: String = "",
    val name: String = "",
    val arguments: String? = null
)

// ── 会话 ──

data class SessionInfo(
    val name: String = "",
    val path: String = "",
    val current: Boolean = false,
    val title: String? = null,
    val turns: Int = 0
)

// ── 状态 ──

data class StatusInfo(
    val label: String? = null,
    val plan: Boolean = false,
    val toolApprovalMode: String? = null,
    val autoApproveTools: Boolean? = null,
    val bypass: Boolean? = null,
    val used: Long = 0,
    val window: Long = 0,
    val cacheHit: Long = 0,
    val cacheMiss: Long = 0,
    val lastUsage: LastUsage? = null,
    val balance: BalanceInfo? = null
)

data class LastUsage(
    val cost: Double? = null,
    val costUsd: Double? = null,
    val totalCost: Double? = null,
    val currency: String? = null
)

data class BalanceInfo(
    val display: String? = null
)

// ── 检查点 ──

data class CheckpointInfo(
    val turn: Int = 0,
    val prompt: String? = null,
    val files: Int = 0
)

// ── UI 消息模型 ──

sealed class ChatItem {
    data class UserMessage(val content: String) : ChatItem()

    data class AssistantMessage(
        val content: String = "",
        val reasoning: String? = null,
        val reasoningExpanded: Boolean = false
    ) : ChatItem()

    data class ToolCard(
        val id: String,
        val name: String,
        val args: String? = null,
        val output: String? = null,
        val err: String? = null,
        val truncated: Boolean = false,
        val isRunning: Boolean = true,
        val expanded: Boolean = false
    ) : ChatItem()

    data class SystemNotice(
        val text: String,
        val isWarning: Boolean = false
    ) : ChatItem()

    data class ErrorMessage(val text: String) : ChatItem()

    data class PhaseIndicator(val text: String) : ChatItem()

    data class UsageStats(val usage: UsagePayload) : ChatItem()

    data class CompactionNotice(
        val trigger: String? = null,
        val summary: String? = null,
        val messages: Int = 0
    ) : ChatItem()

    data class ApprovalCard(
        val id: String,
        val tool: String,
        val subject: String? = null
    ) : ChatItem()

    data class AskCard(
        val id: String,
        val questions: List<AskQuestion> = emptyList()
    ) : ChatItem()
}
