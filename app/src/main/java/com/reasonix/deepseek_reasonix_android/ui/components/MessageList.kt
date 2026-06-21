package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reasonix.deepseek_reasonix_android.data.model.ChatItem

// 匹配 index.html 调色板
private val bg = Color(0xFF1C1A1B)
private val border = Color(0xFF3D3938)

/**
 * 聊天消息列表 — LazyColumn 渲染所有 ChatItem 类型。
 * 自动滚动到最新消息。
 */
@Composable
fun MessageList(
    items: List<ChatItem>,
    modifier: Modifier = Modifier,
    balance: String? = null,
    onApprove: ((session: Boolean, persist: Boolean, scope: String) -> Unit)? = null,
    onDeny: (() -> Unit)? = null,
    onAskSubmit: ((List<Map<String, String>>) -> Unit)? = null
) {
    val listState = rememberLazyListState()

    // 新消息到达时自动滚到底部
    LaunchedEffect(items.size) {
        if (items.isNotEmpty()) {
            listState.animateScrollToItem(items.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(bg),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(items, key = { index, _ -> "msg_$index" }) { _, item ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ChatItemRow(
                    item = item,
                    balance = balance,
                    onApprove = onApprove,
                    onDeny = onDeny,
                    onAskSubmit = onAskSubmit
                )
            }
        }
    }
}

@Composable
private fun ChatItemRow(
    item: ChatItem,
    balance: String? = null,
    onApprove: ((session: Boolean, persist: Boolean, scope: String) -> Unit)?,
    onDeny: (() -> Unit)?,
    onAskSubmit: ((List<Map<String, String>>) -> Unit)?
) {
    when (item) {
        is ChatItem.UserMessage -> UserMessageBubble(item.content)

        is ChatItem.AssistantMessage -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 推理文本（如有）
                if (!item.reasoning.isNullOrBlank()) {
                    ReasoningBlock(text = item.reasoning)
                }
                // 助手正文（Markdown）
                if (item.content.isNotBlank()) {
                    AssistantMessageBubble(text = item.content)
                }
            }
        }

        is ChatItem.ToolCard -> ToolCard(
            id = item.id,
            name = item.name,
            args = item.args,
            output = item.output,
            err = item.err,
            truncated = item.truncated,
            isRunning = item.isRunning
        )

        is ChatItem.SystemNotice -> SystemNotice(
            text = item.text,
            isWarning = item.isWarning
        )

        is ChatItem.ErrorMessage -> ErrorMessage(text = item.text)

        is ChatItem.PhaseIndicator -> PhaseIndicator(text = item.text)

        is ChatItem.UsageStats -> UsageStatsRow(usage = item.usage, balance = balance)

        is ChatItem.CompactionNotice -> CompactionNoticeCard(item)

        is ChatItem.ApprovalCard -> {
            if (onApprove != null && onDeny != null) {
                ApprovalCard(
                    id = item.id,
                    tool = item.tool,
                    subject = item.subject,
                    onAllow = onApprove,
                    onDeny = onDeny
                )
            }
        }

        is ChatItem.AskCard -> {
            if (onAskSubmit != null) {
                AskCard(
                    id = item.id,
                    questions = item.questions,
                    onSubmit = onAskSubmit
                )
            }
        }
    }
}

/**
 * 压缩通知卡片
 */
@Composable
private fun CompactionNoticeCard(notice: ChatItem.CompactionNotice) {
    val bg2 = Color(0xFF222022)
    val muted = Color(0xFF9E9896)
    val border = Color(0xFF3D3938)
    val fg2 = Color(0xFFCCC5C0)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = bg2,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "压缩",
                color = muted,
                fontSize = 11.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            if (notice.trigger != null) {
                Text(
                    text = "触发: ${notice.trigger}",
                    color = fg2,
                    fontSize = 12.sp
                )
            }
            if (notice.summary != null) {
                Text(
                    text = notice.summary,
                    color = fg2,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Text(
                text = "${notice.messages} 条消息已压缩",
                color = muted,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
