package com.reasonix.deepseek_reasonix_android.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reasonix.deepseek_reasonix_android.data.ServerConfigStore

// ═══════════════════════════════════════════════
// 调色板 — Reasonix 暗色主题
// ═══════════════════════════════════════════════

private val Bg       = Color(0xFF1C1A1B)
private val Bg2      = Color(0xFF222022)
private val Panel    = Color(0xFF2A2729)
private val Panel2   = Color(0xFF2E2C2E)
private val Border   = Color(0xFF3D3938)
private val Accent   = Color(0xFFEA8800)
private val Violet   = Color(0xFF9B6FD8)
private val Fg       = Color(0xFFF5F2F0)
private val Fg2      = Color(0xFFCCC5C0)
private val Muted    = Color(0xFF9E9896)
private val Muted2   = Color(0xFF7A7270)
private val Success  = Color(0xFF40A060)

// ═══════════════════════════════════════════════
// ServerConfigScreen
// ═══════════════════════════════════════════════

/**
 * 启动服务器配置页面。
 * 用户输入 IP 和端口（协议固定 HTTP），点击连接后回调 [onConnect]。
 * 若留空，默认连接 http://127.0.0.1:8920。
 * 自动从本地存储加载上次配置并回填表单，连接时持久化。
 */
@Composable
fun ServerConfigScreen(
    onConnect: (String) -> Unit
) {
    val context = LocalContext.current
    val saved = remember { ServerConfigStore.load(context) }

    var ipInput by remember { mutableStateOf(saved.ip) }
    var portInput by remember { mutableStateOf(saved.port) }

    val defaultIp = "127.0.0.1"
    val defaultPort = "8920"

    val resolvedIp = ipInput.ifBlank { defaultIp }
    val resolvedPort = portInput.ifBlank { defaultPort }
    val previewUrl = "http://$resolvedIp:$resolvedPort"

    val canConnect = resolvedIp.isNotBlank() && resolvedPort.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .widthIn(max = 420.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Logo ──
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Accent, Violet)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "R",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            androidx.compose.material3.Text(
                text = "Reasonix",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Fg
            )

            Spacer(modifier = Modifier.height(6.dp))

            androidx.compose.material3.Text(
                text = "AI 编程助手",
                fontSize = 13.sp,
                color = Fg2
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── 服务器地址配置卡片 ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Panel)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "连接服务器",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Fg
                )

                // ── 协议（固定 HTTP） ──
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Text(
                        text = "协议",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.width(48.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Bg2)
                            .border(1.dp, Border, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = "HTTP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Accent
                        )
                    }
                }

                // ── IP 地址 ──
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Text(
                        text = "地址",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.width(48.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Bg2)
                            .border(1.dp, Border, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (ipInput.isEmpty()) {
                            androidx.compose.material3.Text(
                                text = defaultIp,
                                fontSize = 14.sp,
                                color = Muted2
                            )
                        }
                        BasicTextField(
                            value = ipInput,
                            onValueChange = { ipInput = it },
                            textStyle = TextStyle(
                                color = Fg,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(Accent),
                            singleLine = true
                        )
                    }
                }

                // ── 端口 ──
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Text(
                        text = "端口",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.width(48.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Bg2)
                            .border(1.dp, Border, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (portInput.isEmpty()) {
                            androidx.compose.material3.Text(
                                text = defaultPort,
                                fontSize = 14.sp,
                                color = Muted2
                            )
                        }
                        BasicTextField(
                            value = portInput,
                            onValueChange = { portInput = it.filter { c -> c.isDigit() } },
                            textStyle = TextStyle(
                                color = Fg,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(Accent),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }

                // ── 预览 URL ──
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Text(
                        text = "地址",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.width(48.dp)
                    )
                    androidx.compose.material3.Text(
                        text = previewUrl,
                        fontSize = 13.sp,
                        color = if (ipInput.isBlank() && portInput.isBlank()) Muted2 else Fg2,
                        textAlign = TextAlign.Start
                    )
                }

                // ── 提示 ──
                if (ipInput.isBlank() || portInput.isBlank()) {
                    androidx.compose.material3.Text(
                        text = "留空将使用默认地址 ${defaultIp}:${defaultPort}",
                        fontSize = 11.sp,
                        color = Muted2
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── 连接按钮 ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (canConnect) Accent else Panel2)
                        .then(
                            if (canConnect) Modifier.clickable {
                            val ip = resolvedIp
                            val port = resolvedPort
                            ServerConfigStore.save(context, ip, port)
                            onConnect(previewUrl)
                        }
                            else Modifier
                        )
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "连接",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (canConnect) Color.White else Muted
                    )
                }
            }
        }
    }
}


