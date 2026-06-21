package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.reasonix.deepseek_reasonix_android.data.model.StatusInfo

// ═══════════════════════════════════════════════
// 调色板 — Reasonix dark
// ═══════════════════════════════════════════════

private val Panel = Color(0xFF2A2729)
private val Bg2 = Color(0xFF222022)
private val Border = Color(0xFF3D3938)
private val BorderStrong = Color(0xFF5A5452)
private val Accent = Color(0xFFEA8800)

private val Fg = Color(0xFFF5F2F0)
private val Muted = Color(0xFF9E9896)
private val Muted2 = Color(0xFF7A7270)
private val Success = Color(0xFF40A060)
private val Danger = Color(0xFFE04636)
private val Warning = Color(0xFFE5B830)

// ═══════════════════════════════════════════════
// 工具函数
// ═══════════════════════════════════════════════

private fun fmtTok(n: Long): String =
    if (n >= 1000) "%.1fk".format(n / 1000.0) else "$n"

private fun fmtMoney(n: Double): String = when {
    n >= 1.0 -> "¥%.2f".format(n)
    n >= 0.01 -> "¥%.4f".format(n)
    else -> "¥%.6f".format(n)
}

// ═══════════════════════════════════════════════
// StatsDialog
// ═══════════════════════════════════════════════

/**
 * 统计弹窗：以模态卡片展示累积用量与状态概览。
 * 点击遮罩层或关闭按钮均可关闭。
 */
@Composable
fun StatsDialog(
    status: StatusInfo?,
    sessionCount: Int,
    cumulativeTokens: Long,
    cumulativeCost: Double,
    cumulativeCacheHit: Long,
    cumulativeCacheMiss: Long,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = 520.dp)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(12.dp),
            color = Panel,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ── Header ──
                StatsHeader(onDismiss = onDismiss)

                // ── Body (scrollable) ──
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Row 1: Model | Sessions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCell(
                            label = "模型",
                            value = status?.label ?: "-",
                            modifier = Modifier.weight(1f)
                        )
                        StatCell(
                            label = "会话数",
                            value = "$sessionCount",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: Total Tokens | Cache Hit Rate
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCell(
                            label = "总 Token",
                            value = fmtTok(cumulativeTokens),
                            modifier = Modifier.weight(1f)
                        )
                        val cacheTotal = cumulativeCacheHit + cumulativeCacheMiss
                        StatCell(
                            label = "缓存命中率",
                            value = if (cacheTotal > 0) {
                                val pct = cumulativeCacheHit.toDouble() / cacheTotal * 100.0
                                if (pct >= 1.0) "${pct.toInt()}%" else "%.1f%%".format(pct)
                            } else {
                                "-"
                            },
                            modifier = Modifier.weight(1f),
                            valueColor = if (cacheTotal > 0 && cumulativeCacheHit > cumulativeCacheMiss) Success
                            else if (cacheTotal > 0) Warning
                            else null
                        )
                    }

                    // Row 3: Total Cost | Balance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCell(
                            label = "总费用",
                            value = fmtMoney(cumulativeCost),
                            modifier = Modifier.weight(1f)
                        )
                        StatCell(
                            label = "余额",
                            value = status?.balance?.display ?: "-",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 4 (full width): Context Usage
                    ContextUsageBar(
                        used = status?.used ?: 0,
                        window = status?.window ?: 0
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// Header
// ═══════════════════════════════════════════════

@Composable
private fun StatsHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 12.dp, top = 14.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "统计",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Fg,
            modifier = Modifier.weight(1f)
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Bg2,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickable { onDismiss() }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "\u00D7",
                    fontSize = 16.sp,
                    color = Muted
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════
// StatCell — 单个统计卡片
// ═══════════════════════════════════════════════

@Composable
private fun StatCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Bg2)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Muted,
            letterSpacing = 0.3.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            color = valueColor ?: Fg,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ═══════════════════════════════════════════════
// ContextUsageBar — 上下文用量条（全宽）
// ═══════════════════════════════════════════════

@Composable
private fun ContextUsageBar(used: Long, window: Long) {
    if (window <= 0) return

    val pct = (used.toFloat() / window).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Bg2)
            .padding(12.dp)
    ) {
        Text(
            text = "上下文用量",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Muted,
            letterSpacing = 0.3.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 进度条
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(Panel)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(pct)
                    .height(4.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(
                        when {
                            pct > 0.90f -> Danger
                            pct > 0.75f -> Warning
                            else -> Accent
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = fmtTok(used),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Muted2
            )
            Text(
                text = fmtTok(window),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Muted2
            )
        }
    }
}
