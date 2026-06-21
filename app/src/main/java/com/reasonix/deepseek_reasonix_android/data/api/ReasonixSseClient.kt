package com.reasonix.deepseek_reasonix_android.data.api

import com.google.gson.Gson
import com.reasonix.deepseek_reasonix_android.data.model.SseEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

/**
 * SSE 客户端 — 连接 /events 端点，实时接收服务端推送的消息流。
 * 使用 OkHttp SSE 实现，通过 Kotlin Flow 暴露事件。
 */
class ReasonixSseClient(
    private val baseUrl: String,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()
) {
    private val gson = Gson()
    private var eventSource: EventSource? = null

    /**
     * 连接 SSE 并返回事件 Flow。
     * 调用方 collect 时自动连接，取消 collect 时自动断开。
     */
    fun connect(): Flow<SseEvent> = callbackFlow {
        val request = Request.Builder()
            .url("$baseUrl/events")
            .header("Accept", "text/event-stream")
            .build()

        val listener = object : EventSourceListener() {
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                try {
                    val event = gson.fromJson(data, SseEvent::class.java)
                    trySend(event)
                } catch (_: Exception) {
                    // 解析失败则忽略
                }
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                // SSE 断开时关闭 Flow
                close(t)
            }

            override fun onClosed(eventSource: EventSource) {
                close()
            }
        }

        val factory = EventSources.createFactory(client)
        eventSource = factory.newEventSource(request, listener)

        awaitClose {
            eventSource?.cancel()
            eventSource = null
        }
    }

    fun disconnect() {
        eventSource?.cancel()
        eventSource = null
    }
}
