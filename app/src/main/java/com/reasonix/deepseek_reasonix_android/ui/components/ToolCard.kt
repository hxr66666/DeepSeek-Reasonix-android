package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════
// 颜色常量
// ═══════════════════════════════════════════════

private val CardBg = Color(0xFF282528)
private val Border = Color(0xFF3D3938)
private val Accent = Color(0xFFEA8800)
private val AccentSoft = Color(0x26EA8800)
private val Success = Color(0xFF40A060)
private val SuccessSoft = Color(0x2440A060)
private val Danger = Color(0xFFE04636)
private val DangerSoft = Color(0x29E04636)
private val Bg2 = Color(0xFF222022)
private val Fg = Color(0xFFF5F2F0)
private val Fg2 = Color(0xFFCCC5C0)
private val Muted = Color(0xFF9E9896)
private val Panel2 = Color(0xFF2E2C2E)

// ═══════════════════════════════════════════════
// ToolCard — 可折叠工具执行卡片
// ═══════════════════════════════════════════════

/**
 * 工具执行状态卡片，展示工具调用及其结果。
 *
 * 三种视觉状态：
 * - **运行中** (`isRunning = true`)：橙色图标背景 + 旋转加载动画
 * - **成功** (`isRunning = false`, `err = null`)：绿色图标背景 + 勾号
 * - **失败** (`isRunning = false`, `err != null`)：红色图标背景 + 叉号
 *
 * @param id       工具调用唯一标识
 * @param name     工具名称（如 "websearch", "read"）
 * @param args     工具调用参数（可选），以等宽字体截断显示
 * @param output   执行成功时的输出文本
 * @param err      执行失败时的错误信息，非 null 时进入错误状态
 * @param truncated 输出是否已被上游截断
 * @param isRunning 是否仍在执行中
 */
@Composable
fun ToolCard(
    id: String,
    name: String,
    args: String? = null,
    output: String? = null,
    err: String? = null,
    truncated: Boolean = false,
    isRunning: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    // 敲定状态
    val status: ToolStatus = when {
        isRunning  -> ToolStatus.RUNNING
        err != null -> ToolStatus.ERROR
        else        -> ToolStatus.SUCCESS
    }

    // 无限旋转动画（仅运行态）
    val rotation by if (status == ToolStatus.RUNNING) {
        val transition = rememberInfiniteTransition(label = "toolCardSpin")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = remember(toolCardSpinDuration) {
                toolCardSpinDuration
            },
            label = "spinRotation"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBg,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp
    ) {
        Column {
            // ═══════════ 卡片头部 ═══════════
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 状态图标
                StatusIcon(status = status, rotation = rotation)

                // 工具名称
                Text(
                    text = name,
                    color = Fg,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                // 参数（等宽截断）
                if (!args.isNullOrBlank()) {
                    Text(
                        text = args,
                        color = Muted,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 折叠箭头
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                                  else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Muted,
                    modifier = Modifier.size(20.dp)
                )
            }

            // ═══════════ 卡片体（可折叠） ═══════════
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ToolBody(status = status, output = output, err = err, truncated = truncated)
            }
        }
    }
}

// ═══════════════════════════════════════════════
// 内部枚举 & 组件
// ═══════════════════════════════════════════════

private enum class ToolStatus { RUNNING, SUCCESS, ERROR }

/**
 * 状态图标：圆形背景 + 内部图标。
 * 运行态带旋转动画。
 */
@Composable
private fun StatusIcon(status: ToolStatus, rotation: Float) {
    val bgColor = when (status) {
        ToolStatus.RUNNING -> AccentSoft
        ToolStatus.SUCCESS -> SuccessSoft
        ToolStatus.ERROR   -> DangerSoft
    }

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            ToolStatus.RUNNING -> {
                SpinningLoaderIcon(
                    rotation = rotation,
                    tint = Accent,
                    modifier = Modifier.size(14.dp)
                )
            }
            ToolStatus.SUCCESS -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier.size(14.dp)
                )
            }
            ToolStatus.ERROR -> {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Danger,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * 运行态旋转加载图标：使用 Compose Canvas 绘制带弧形缺口的圆环。
 * 通过 [rotation] 驱动整体旋转。
 */
@Composable
private fun SpinningLoaderIcon(
    rotation: Float,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.rotate(rotation)) {
        val strokeWidth = 2.5.dp.toPx()
        val sweep = 90f
        drawArc(
            color = tint,
            startAngle = 0f,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = tint.copy(alpha = 0.35f),
            startAngle = 120f,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = tint.copy(alpha = 0.15f),
            startAngle = 240f,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
    }
}

/**
 * 卡片体：展示工具输出或错误信息。
 */
@Composable
private fun ToolBody(
    status: ToolStatus,
    output: String?,
    err: String?,
    truncated: Boolean
) {
    val bodyText = when (status) {
        ToolStatus.ERROR -> err.orEmpty()
        else             -> output.orEmpty()
    }

    if (bodyText.isBlank()) return

    val displayText = if (bodyText.length > TOOL_BODY_MAX_CHARS) {
        bodyText.take(TOOL_BODY_MAX_CHARS) + "\n…"
    } else {
        bodyText
    }

    val bodyBg = when (status) {
        ToolStatus.ERROR   -> DangerSoft
        ToolStatus.RUNNING -> Panel2
        ToolStatus.SUCCESS -> Bg2
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bodyBg)
            .padding(12.dp)
    ) {
        if (truncated && status != ToolStatus.ERROR) {
            Text(
                text = "输出被截断。仅显示前 $TOOL_BODY_MAX_CHARS 个字符。",
                color = Muted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Text(
            text = displayText,
            color = Fg2,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 18.sp
        )
    }
}

// ═══════════════════════════════════════════════
// 常量
// ═══════════════════════════════════════════════

/** 卡片体展示字符上限 */
private const val TOOL_BODY_MAX_CHARS = 2000

/** 旋转动画时长 */
private val toolCardSpinDuration = infiniteRepeatable<Float>(
    animation = tween(
        durationMillis = 1200,
        easing = LinearEasing
    )
)
