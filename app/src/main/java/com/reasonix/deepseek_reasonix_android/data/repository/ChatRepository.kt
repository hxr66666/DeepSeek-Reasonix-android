package com.reasonix.deepseek_reasonix_android.data.repository

import com.reasonix.deepseek_reasonix_android.data.api.ReasonixApi
import com.reasonix.deepseek_reasonix_android.data.api.ReasonixSseClient
import com.reasonix.deepseek_reasonix_android.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * 仓库层 — 将 API、SSE 客户端整合为统一的聊天数据源。
 */
class ChatRepository(
    private val api: ReasonixApi,
    private val sseClient: ReasonixSseClient
) {

    // ── SSE 事件流 ──
    fun sseEvents(): Flow<SseEvent> = sseClient.connect()

    fun disconnectSse() = sseClient.disconnect()

    // ── REST 接口 ──

    suspend fun submit(input: String) = api.submit(input)

    suspend fun cancel() = api.cancel()

    suspend fun getHistory(): List<HistoryMessage> = api.getHistory()

    suspend fun getStatus(): StatusInfo? = api.getStatus()

    suspend fun getSessions(): List<SessionInfo> = api.getSessions()

    suspend fun newSession() = api.newSession()

    suspend fun resumeSession(path: String) = api.resumeSession(path)

    suspend fun deleteSession(name: String) = api.deleteSession(name)

    suspend fun compact() = api.compact()

    suspend fun getCheckpoints(): List<CheckpointInfo> = api.getCheckpoints()

    suspend fun rewind(turn: Int, scope: String = "both") = api.rewind(turn, scope)

    suspend fun fork(turn: Int, name: String = "") = api.fork(turn, name)

    suspend fun summarize(turn: Int, mode: String) = api.summarize(turn, mode)

    suspend fun approve(
        id: String,
        allow: Boolean,
        session: Boolean = false,
        persist: Boolean = false,
        scope: String = ""
    ) = api.approve(id, allow, session, persist, scope)

    suspend fun answer(id: String, answers: List<Map<String, Any>>) =
        api.answer(id, answers)

    suspend fun setPlan(on: Boolean) = api.setPlan(on)

    suspend fun setToolApprovalMode(mode: String) = api.setToolApprovalMode(mode)
}
