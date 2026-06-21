package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.reasonix.deepseek_reasonix_android.data.model.CheckpointInfo

// ═══════════════════════════════════════════════
// 调色板 (Reasonix dark)
// ═══════════════════════════════════════════════

private val Bg          = Color(0xFF1C1A1B)
private val Panel       = Color(0xFF2A2729)
private val CardHover   = Color(0xFF302E30)
private val Border      = Color(0xFF3D3938)
private val BorderStrong = Color(0xFF5A5452)
private val Accent      = Color(0xFFEA8800)
private val AccentSoft  = Color(0x26EA8800)
private val Fg          = Color(0xFFF5F2F0)
private val Fg2         = Color(0xFFCCC5C0)
private val Muted       = Color(0xFF9E9896)
private val Muted2      = Color(0xFF7A7270)

// ═══════════════════════════════════════════════
// Scope 定义
// ═══════════════════════════════════════════════

private data class ScopeDef(
    val letter: String,
    val label: String,
    val desc: String
)

private val scopes = listOf(
    ScopeDef("b", "全部",              "代码 + 对话"),
    ScopeDef("c", "仅对话",            "仅对话"),
    ScopeDef("d", "仅代码",            "仅代码"),
    ScopeDef("f", "分叉",              "新分支"),
    ScopeDef("s", "总结",              "从此处"),
    ScopeDef("u", "总结至此处",        "至此")
)

// ═══════════════════════════════════════════════
// RewindPickerDialog
// ═══════════════════════════════════════════════

/**
 * 两阶段回退对话框：
 * - Stage 0: 从检查点列表中选择 turn
 * - Stage 1: 选择回退范围动作
 *
 * 键盘操作：
 *   j / ArrowDown   — 向下移动
 *   k / ArrowUp     — 向上移动
 *   Enter           — 前进 / 应用
 *   Esc             — 后退 / 关闭
 *   Stage 1 快捷键  — 按下字母键直接应用对应范围
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RewindPickerDialog(
    checkpoints: List<CheckpointInfo>,
    onRewind: (turn: Int, scope: String) -> Unit,
    onFork: (turn: Int) -> Unit,
    onSummarize: (turn: Int, mode: String) -> Unit,
    onDismiss: () -> Unit
) {
    // ── 状态 ──
    var stage by remember { mutableStateOf(0) }
    var selectedTurnIndex by remember { mutableStateOf(0) }
    var selectedScopeIndex by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    // 检查点列表变化时重置
    LaunchedEffect(checkpoints) {
        selectedTurnIndex = 0
        selectedScopeIndex = 0
        stage = 0
    }

    // 请求焦点以接收键盘事件
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val turnCount = checkpoints.size
    val scopeCount = scopes.size

    // ── 选取当前检查点的 turn 值 ──
    val currentTurn = checkpoints.getOrNull(selectedTurnIndex)?.turn ?: 0

    // ── 范围应用逻辑 ──
    fun applyScope(letter: String) {
        val turn = currentTurn
        when (letter) {
            "b" -> { onRewind(turn, "both"); onDismiss() }
            "c" -> { onRewind(turn, "conversation"); onDismiss() }
            "d" -> { onRewind(turn, "code"); onDismiss() }
            "f" -> { onFork(turn); onDismiss() }
            "s" -> { onSummarize(turn, "from"); onDismiss() }
            "u" -> { onSummarize(turn, "up_to"); onDismiss() }
        }
    }

    // ── 键盘事件分发 ──
    fun onKey(keyEvent: KeyEvent): Boolean {
        if (keyEvent.type != KeyEventType.KeyDown) return false

        return when (stage) {
            // ── Stage 0: 检查点导航 ──
            0 -> when {
                (keyEvent.key == Key.J || keyEvent.key == Key.DirectionDown) && turnCount > 0 -> {
                    selectedTurnIndex = (selectedTurnIndex + 1).coerceAtMost(turnCount - 1)
                    true
                }
                (keyEvent.key == Key.K || keyEvent.key == Key.DirectionUp) && turnCount > 0 -> {
                    selectedTurnIndex = (selectedTurnIndex - 1).coerceAtLeast(0)
                    true
                }
                keyEvent.key == Key.Enter && turnCount > 0 -> {
                    stage = 1
                    selectedScopeIndex = 0
                    true
                }
                keyEvent.key == Key.Escape -> {
                    onDismiss()
                    true
                }
                else -> false
            }

            // ── Stage 1: 范围导航 + 快捷键 ──
            1 -> when {
                keyEvent.key == Key.J || keyEvent.key == Key.DirectionDown -> {
                    selectedScopeIndex = (selectedScopeIndex + 1).coerceAtMost(scopeCount - 1)
                    true
                }
                keyEvent.key == Key.K || keyEvent.key == Key.DirectionUp -> {
                    selectedScopeIndex = (selectedScopeIndex - 1).coerceAtLeast(0)
                    true
                }
                keyEvent.key == Key.Enter -> {
                    applyScope(scopes[selectedScopeIndex].letter)
                    true
                }
                keyEvent.key == Key.Escape -> {
                    stage = 0
                    true
                }
                // 快捷键：字母直接应用
                keyEvent.key == Key.B -> { applyScope("b"); true }
                keyEvent.key == Key.C -> { applyScope("c"); true }
                keyEvent.key == Key.D -> { applyScope("d"); true }
                keyEvent.key == Key.F -> { applyScope("f"); true }
                keyEvent.key == Key.S -> { applyScope("s"); true }
                keyEvent.key == Key.U -> { applyScope("u"); true }
                else -> false
            }

            else -> false
        }
    }

    // ── Dialog ──
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent(::onKey)
                .clip(RoundedCornerShape(12.dp))
                .background(Panel)
                .border(1.dp, BorderStrong, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(0.dp)) {
                // ── 头部 ──
                DialogHeader(
                    stage = stage,
                    turn = currentTurn,
                    onDismiss = onDismiss
                )

                // 分隔线
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border)
                )

                if (turnCount == 0) {
                    // ── 空状态 ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "无可用检查点。",
                            color = Muted,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    when (stage) {
                        // ── Stage 0: 检查点列表 ──
                        0 -> Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            checkpoints.forEachIndexed { index, cp ->
                                CheckpointRow(
                                    checkpoint = cp,
                                    isSelected = index == selectedTurnIndex,
                                    onClick = {
                                        selectedTurnIndex = index
                                        stage = 1
                                        selectedScopeIndex = 0
                                    }
                                )
                            }
                        }

                        // ── Stage 1: 范围列表 ──
                        1 -> Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            scopes.forEachIndexed { index, scope ->
                                ScopeRow(
                                    scope = scope,
                                    isSelected = index == selectedScopeIndex,
                                    onClick = { applyScope(scope.letter) }
                                )
                            }
                        }
                    }
                }

                // 分隔线
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border)
                )

                // ── 底部提示 ──
                DialogFooter(stage = stage)
            }
        }
    }
}

// ═══════════════════════════════════════════════
// 子组件
// ═══════════════════════════════════════════════

/**
 * 对话框头部：标题 + 关闭按钮。
 */
@Composable
private fun DialogHeader(
    stage: Int,
    turn: Int,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 标题
        Text(
            text = when (stage) {
                0 -> "倒带 — 选择轮次"
                else -> "倒带回 #$turn → 范围"
            },
            color = Fg,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        // Esc 关闭按钮
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onDismiss() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Esc",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Muted2
            )
        }
    }
}

/**
 * 检查点行：左侧 3dp 选中色条 + turn# + prompt 截断 + file count。
 */
@Composable
private fun CheckpointRow(
    checkpoint: CheckpointInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val rowBg = if (isSelected) AccentSoft else Color.Transparent
    val promptText = checkpoint.prompt ?: ""
    val snippet = if (promptText.length > 60) promptText.take(60) + "…" else promptText

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .clickable { onClick() }
            .padding(start = 12.dp, end = 12.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 选中指示条
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) Accent else Color.Transparent)
        )

        Spacer(modifier = Modifier.width(9.dp))

        // turn 编号
        Text(
            text = "#${checkpoint.turn}",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Accent else Muted,
            modifier = Modifier.width(32.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        // prompt 摘要
        Text(
            text = snippet.ifEmpty { "—" },
            color = if (isSelected) Fg2 else Muted,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 文件数量
        if (checkpoint.files > 0) {
            Text(
                text = "${checkpoint.files}",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = if (isSelected) Muted else Muted2
            )
        } else {
            Text(
                text = "—",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Muted2
            )
        }
    }
}

/**
 * 范围行：左侧 3dp 选中色条 + 快捷键字母 + label + 描述。
 */
@Composable
private fun ScopeRow(
    scope: ScopeDef,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val rowBg = if (isSelected) AccentSoft else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .clickable { onClick() }
            .padding(start = 12.dp, end = 12.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 选中指示条
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) Accent else Color.Transparent)
        )

        Spacer(modifier = Modifier.width(9.dp))

        // 快捷键字母
        Text(
            text = scope.letter,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Accent else Fg2,
            modifier = Modifier.width(18.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        // label
        Text(
            text = scope.label,
            color = if (isSelected) Fg else Fg2,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 描述
        Text(
            text = scope.desc,
            color = Muted,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 底部键盘提示。
 */
@Composable
private fun DialogFooter(stage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (stage) {
                0 -> "j/k ↑↓ 导航"
                else -> "j/k ↑↓ 导航  ·  字母键执行"
            },
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = Muted2
        )

        Text(
            text = when (stage) {
                0 -> "Enter: 选择"
                else -> "Enter: 执行  ·  Esc: 返回"
            },
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = Muted2
        )
    }
}
