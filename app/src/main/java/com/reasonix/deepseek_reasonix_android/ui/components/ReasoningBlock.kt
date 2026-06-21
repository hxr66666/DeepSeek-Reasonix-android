package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reasonix.deepseek_reasonix_android.data.model.AskQuestion

// ═══════════════════════════════════════════════
// 调色板 (Reasonix 暗色主题)
// ═══════════════════════════════════════════════

private val panel     = Color(0xFF2A2729)
private val panel2    = Color(0xFF2E2C2E)
private val bg2       = Color(0xFF222022)
private val card      = Color(0xFF282528)
private val border    = Color(0xFF3D3938)
private val borderStrong = Color(0xFF5A5452)
private val accent    = Color(0xFFEA8800)
private val accentSoft = Color(0x26EA8800)
private val fg        = Color(0xFFF5F2F0)
private val fg2       = Color(0xFFCCC5C0)
private val muted     = Color(0xFF9E9896)
private val muted2    = Color(0xFF7A7270)
private val warning   = Color(0xFFE5B830)
private val warningSoft = Color(0x29E5B830)
private val danger    = Color(0xFFE04636)

// ═══════════════════════════════════════════════
// 1. ReasoningBlock
// ═══════════════════════════════════════════════

/**
 * 可折叠的推理过程展示面板。
 * 默认折叠，点击展开后以等宽字体沿左侧彩色边框滚动展示推理文本。
 *
 * @param text     推理文本内容
 * @param modifier 外部修饰符
 */
@Composable
fun ReasoningBlock(
    text: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "chevronRotation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // ── 切换按钮 ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "▶",
                modifier = Modifier.rotate(rotation),
                color = muted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "思考中…",
                color = muted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // ── 展开面板 ──
        if (expanded) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(280.dp)
                        .background(border, RoundedCornerShape(1.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = 280.dp)
                        .padding(start = 8.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = text,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = fg2,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// 2. ApprovalCard
// ═══════════════════════════════════════════════

/**
 * 工具执行审批卡片。
 * 展示待审批的工具名称与操作对象，并提供四个审批级别按钮。
 *
 * @param id      审批请求 ID
 * @param tool    工具名称
 * @param subject 操作对象描述（可选）
 * @param onAllow 批准回调 — (session: Boolean, persist: Boolean, scope: String)
 * @param onDeny  拒绝回调
 */
@Composable
fun ApprovalCard(
    id: String,
    tool: String,
    subject: String? = null,
    onAllow: (session: Boolean, persist: Boolean, scope: String) -> Unit,
    onDeny: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = card),
        border = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // ── 标题行 ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⚠",
                    fontSize = 16.sp,
                    color = warning
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "需要审批",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fg
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── 工具信息区 ──
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = panel
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = tool,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = accent
                    )
                    if (!subject.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subject,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = muted,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── 操作按钮组 ──
            // 第一行：Allow (Y) + Allow for session (A)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ApprovalButton(
                    label = "允许",
                    shortcut = "Y",
                    backgroundColor = accent,
                    onClick = { onAllow(false, false, "once") },
                    modifier = Modifier.weight(1f)
                )
                ApprovalButton(
                    label = "本次会话允许",
                    shortcut = "A",
                    backgroundColor = accent,
                    onClick = { onAllow(true, false, "session") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 第二行：Always allow (P) + Deny (N)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ApprovalButton(
                    label = "始终允许",
                    shortcut = "P",
                    backgroundColor = accent,
                    onClick = { onAllow(false, true, "persist") },
                    modifier = Modifier.weight(1f)
                )
                ApprovalButton(
                    label = "拒绝",
                    shortcut = "N",
                    backgroundColor = Color.Transparent,
                    borderColor = muted2,
                    onClick = onDeny,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 审批按钮组件。
 */
@Composable
private fun ApprovalButton(
    label: String,
    shortcut: String,
    backgroundColor: Color,
    borderColor: Color = backgroundColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor.copy(alpha = if (backgroundColor == Color.Transparent) 0f else 0.15f),
        border = if (borderColor != backgroundColor || backgroundColor == Color.Transparent) {
            androidx.compose.foundation.BorderStroke(1.dp, borderColor)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (backgroundColor == Color.Transparent) muted else accent
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (backgroundColor == Color.Transparent) {
                    muted2.copy(alpha = 0.15f)
                } else {
                    accent.copy(alpha = 0.2f)
                }
            ) {
                Text(
                    text = shortcut,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (backgroundColor == Color.Transparent) muted else accent
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════
// 3. AskCard
// ═══════════════════════════════════════════════

/**
 * 向用户提问的卡片组件。
 * 支持单选和多选问题，选中项以 accent 色高亮，底部提供提交按钮。
 *
 * @param id        提问请求 ID
 * @param questions 问题列表（[AskQuestion]）
 * @param onSubmit  提交回调，传递每个问题的选中选项列表
 */
@Composable
fun AskCard(
    id: String,
    questions: List<AskQuestion>,
    onSubmit: (answers: List<Map<String, String>>) -> Unit
) {
    val selections = remember(questions) {
        questions.map { q -> mutableStateListOf<String>() }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = card),
        border = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // ── 标题 ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "提问",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fg
                )
            }

            // ── 问题列表 ──
            questions.forEachIndexed { qIndex, question ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = question.prompt,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = fg
                )
                val modeLabel = if (question.multi) " (多选)" else " (单选)"
                Text(
                    text = modeLabel,
                    fontSize = 11.sp,
                    color = muted2
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ── 选项列表 ──
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    question.options.forEach { option ->
                        val isSelected = option.label in selections[qIndex]
                        val selectedBg by animateColorAsState(
                            targetValue = if (isSelected) accentSoft else Color.Transparent,
                            animationSpec = tween(durationMillis = 150),
                            label = "optionBg"
                        )
                        val selectedBorder by animateColorAsState(
                            targetValue = if (isSelected) accent else border,
                            animationSpec = tween(durationMillis = 150),
                            label = "optionBorder"
                        )

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    if (question.multi) {
                                        if (isSelected) {
                                            selections[qIndex].remove(option.label)
                                        } else {
                                            selections[qIndex].add(option.label)
                                        }
                                    } else {
                                        selections[qIndex].clear()
                                        selections[qIndex].add(option.label)
                                    }
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = selectedBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, selectedBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = option.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) accent else fg2
                                )
                                if (!option.description.isNullOrBlank()) {
                                    Text(
                                        text = option.description,
                                        fontSize = 12.sp,
                                        color = muted,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── 提交按钮 ──
            val hasSelection = selections.any { it.isNotEmpty() }
            Button(
                onClick = {
                    val answers = selections.mapIndexed { index, selectedLabels ->
                        mapOf(
                            "id" to questions[index].id,
                            "answers" to selectedLabels.joinToString(",")
                        )
                    }
                    onSubmit(answers)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = fg,
                    disabledContainerColor = accent.copy(alpha = 0.3f),
                    disabledContentColor = fg.copy(alpha = 0.4f)
                ),
                enabled = hasSelection
            ) {
                Text(
                    text = "提交",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
