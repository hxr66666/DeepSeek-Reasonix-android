package com.reasonix.deepseek_reasonix_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.reasonix.deepseek_reasonix_android.ui.screen.ChatScreen
import com.reasonix.deepseek_reasonix_android.ui.screen.ServerConfigScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Reasonix 暗色主题 — 覆盖 Material3 默认色板
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF1C1A1B) // --bg
            ) {
                var serverConfigured by remember { mutableStateOf(false) }
                var serverUrl by remember { mutableStateOf("http://127.0.0.1:8920") }

                if (!serverConfigured) {
                    ServerConfigScreen(
                        onConnect = { url ->
                            serverUrl = url
                            serverConfigured = true
                        }
                    )
                } else {
                    ChatScreen(initialServerUrl = serverUrl)
                }
            }
        }
    }
}
