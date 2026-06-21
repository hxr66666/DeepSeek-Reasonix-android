package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reasonix.deepseek_reasonix_android.data.model.UsagePayload

// ═══════════════════════════════════════════════
// 调色板（与深色主题对齐）
// ═══════════════════════════════════════════════

private val accent = Color(0xFFEA8800)
private val fg = Color(0xFFF5F2F0)
private val fg2 = Color(0xFFCCC5C0)
private val muted = Color(0xFF9E9896)
private val muted2 = Color(0xFF7A7270)
private val danger = Color(0xFFE04636)
private val dangerSoft = Color(0x29E04636)
private val success = Color(0xFF40A060)
private val border = Color(0xFF3D3938)
private val bg2 = Color(0xFF222022)
private val panel2 = Color(0xFF2E2C2E)
private val userBubbleBg = accent   // #EA8800 橙底（高对比度）
private val userBubbleFg = Color(0xFFFFFFFF)

// ═══════════════════════════════════════════════
// 格式化工具
// ═══════════════════════════════════════════════

private fun fmtTok(n: Long): String =
    if (n >= 1000) "%.1fk".format(n / 1000.0) else "$n"

private fun fmtCost(costUsd: Double?, cost: Double?): String? {
    val v = costUsd ?: cost ?: return null
    return when {
        v >= 1.0 -> "$${"%.2f".format(v)}"
        v >= 0.01 -> "$${"%.4f".format(v)}"
        else -> "$${"%.6f".format(v)}"
    }
}

// ═══════════════════════════════════════════════
// 1. UserMessageBubble
// ═══════════════════════════════════════════════

/**
 * 用户消息气泡：右对齐，橙底白字圆角气泡，顶部带有复制按钮。
 */
@Composable
fun UserMessageBubble(text: String) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        // 复制按钮
        IconButton(
            onClick = { clipboardManager.setText(AnnotatedString(text)) },
            modifier = Modifier
                .padding(end = 12.dp)
                .size(34.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "复制",
                tint = muted,
                modifier = Modifier.size(17.dp)
            )
        }

        // 气泡
        Box(
            modifier = Modifier
                .padding(start = 64.dp, top = 2.dp, bottom = 6.dp, end = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(userBubbleBg)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                color = userBubbleFg,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}

// ═══════════════════════════════════════════════
// 2. AssistantMessageBubble
// ═══════════════════════════════════════════════

/**
 * 助手消息气泡：左对齐，Markdown 富文本渲染 + 右上角复制按钮。
 */
@Composable
fun AssistantMessageBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 16.dp, top = 2.dp, bottom = 6.dp)
    ) {
        // 复制按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(text)) },
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "复制",
                    tint = muted,
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        // Markdown 正文（Markwon 原生引擎：支持 HTML / 图片 / 表格 / 任务列表）
        MarkdownRenderer(
            markdown = text,
            codeBackground = bg2,
            codeTextColor = fg2,
            linkColor = accent
        )
    }
}

// ═══════════════════════════════════════════════
// 3. SystemNotice
// ═══════════════════════════════════════════════

/**
 * 系统通知：左侧 2dp 色条 + 小字正文。
 * 警告模式使用 warning 色条和 "!" 前缀。
 */
@Composable
fun SystemNotice(text: String, isWarning: Boolean = false) {
    val borderColor = if (isWarning) accent else muted
    val prefix = if (isWarning) "! " else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(20.dp)
                .background(borderColor, RoundedCornerShape(1.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$prefix$text",
            color = if (isWarning) fg2 else muted,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}

// ═══════════════════════════════════════════════
// 4. ErrorMessage
// ═══════════════════════════════════════════════

/**
 * 错误消息：红色左边框 + 微红背景 + 小字。
 */
@Composable
fun ErrorMessage(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(dangerSoft)
            .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(20.dp)
                .background(danger, RoundedCornerShape(1.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = danger,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}

// ═══════════════════════════════════════════════
// 5. PhaseIndicator
// ═══════════════════════════════════════════════

/**
 * 阶段指示器：居中、大写、等宽、小号、低调色。
 */
@Composable
fun PhaseIndicator(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = muted,
            letterSpacing = 1.sp
        )
    }
}

// ═══════════════════════════════════════════════
// 6. UsageStatsRow
// ═══════════════════════════════════════════════

/**
 * 用量统计卡片：醒目展示本轮费用 + 剩余余额。
 * 上排：本轮费用（橙色） ｜ 余额（绿色）
 * 下排：Token 明细（Total / In / Out） + 缓存命中率
 */
@Composable
fun UsageStatsRow(usage: UsagePayload, balance: String? = null) {
    val total = usage.totalTokens
    val prompt = usage.promptTokens
    val completion = usage.completionTokens
    val cacheHit = usage.cacheHitTokens
    val cacheMiss = usage.cacheMissTokens
    val costStr = fmtCost(usage.costUsd, usage.cost)

    // 计算缓存命中率
    val cacheTotal = cacheHit + cacheMiss
    val cachePercent: String? = if (cacheTotal > 0) {
        val pct = cacheHit.toDouble() / cacheTotal * 100.0
        if (pct >= 1.0) "${pct.toInt()}%" else "%.1f%%".format(pct)
    } else null

    // 卡片容器
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg2)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        // ── 上排：费用 + 余额 ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 本轮费用
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "本轮",
                    color = muted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = costStr ?: "-",
                    color = accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // 剩余余额
            if (balance != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "余额",
                        color = muted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = balance,
                        color = success,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ── 下排：Token 明细 ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "T:${fmtTok(total)}",
                color = muted2,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "入:${fmtTok(prompt)}",
                color = muted2,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "出:${fmtTok(completion)}",
                color = muted2,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )

            // 缓存命中率
            if (cachePercent != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "缓存 $cachePercent",
                    color = if (cacheHit > cacheMiss) success else muted2,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
