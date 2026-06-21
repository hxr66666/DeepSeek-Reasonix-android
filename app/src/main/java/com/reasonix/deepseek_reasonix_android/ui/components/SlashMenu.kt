package com.reasonix.deepseek_reasonix_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════
// 调色板
// ═══════════════════════════════════════════════

private val Bg2     = Color(0xFF222022)
private val Card    = Color(0xFF282528)
private val Border  = Color(0xFF3D3938)
private val Accent  = Color(0xFFEA8800)
private val AccentSoft = Color(0x26EA8800)
private val Fg      = Color(0xFFF5F2F0)
private val Muted   = Color(0xFF9E9896)
private val Muted2  = Color(0xFF7A7270)

// ═══════════════════════════════════════════════
// 命令定义
// ═══════════════════════════════════════════════

data class SlashCommand(
    val name: String,
    val description: String,
    val action: String
)

private val COMMANDS = listOf(
    SlashCommand("help", "显示可用命令", "/help"),
    SlashCommand("plan", "切换计划模式", "/plan"),
    SlashCommand("yolo", "切换 YOLO 模式", "/yolo"),
    SlashCommand("auto", "切换到自动模式", "/auto"),
    SlashCommand("compact", "压缩对话", "/compact"),
    SlashCommand("rewind", "回退对话", "/rewind"),
    SlashCommand("fork", "在当前轮次分叉对话", "/fork"),
    SlashCommand("new", "新建会话", "/new"),
    SlashCommand("stats", "显示统计", "/stats"),
    SlashCommand("status", "显示服务器状态", "/status"),
    SlashCommand("sessions", "列出会话", "/sessions"),
    SlashCommand("summarize", "总结对话", "/summarize"),
    SlashCommand("resume", "恢复会话", "/resume "),
    SlashCommand("delete", "删除会话", "/delete "),
    SlashCommand("compact", "设置压缩自动模式", "/compact auto"),
    SlashCommand("compact", "设置压缩手动模式", "/compact manual"),
    SlashCommand("theme", "切换主题", "/theme"),
)

// ═══════════════════════════════════════════════
// SlashMenu
// ═══════════════════════════════════════════════

/**
 * 斜杠命令下拉菜单 —— 匹配 index.html 的 .slash-menu。
 * 在输入 `/` 后自动弹出，按输入前缀过滤命令列表。
 *
 * @param prefix     `/` 之后的文本前缀（用于过滤）
 * @param onSelect   选择命令后的回调，传入完整的 command.action 文本
 * @param onDismiss  菜单关闭回调（通过 Esc 或失去焦点触发）
 * @param modifier   外部修饰符
 */
@Composable
fun SlashMenu(
    prefix: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filtered = remember(prefix) {
        if (prefix.isBlank()) {
            COMMANDS
        } else {
            COMMANDS.filter { it.name.startsWith(prefix, ignoreCase = true) }
        }
    }

    if (filtered.isEmpty()) {
        onDismiss()
        return
    }

    Surface(
        modifier = modifier
            .width(260.dp)
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = Card,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            // 提示行
            Text(
                text = "命令",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = Muted2,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )

            // 命令列表
            LazyColumn(
                modifier = Modifier.heightIn(max = 220.dp)
            ) {
                items(filtered.take(12)) { cmd ->
                    SlashCommandRow(
                        command = cmd,
                        onClick = {
                            onSelect(cmd.action)
                        }
                    )
                }
            }

            // 底部提示
            Text(
                text = "↑↓ 导航  ↵ 选择  Esc 关闭",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = Muted2,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun SlashCommandRow(
    command: SlashCommand,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 命令名
        Text(
            text = "/${command.name}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            color = Fg
        )
        Spacer(modifier = Modifier.width(10.dp))
        // 描述
        Text(
            text = command.description,
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier.weight(1f)
        )
    }
}
