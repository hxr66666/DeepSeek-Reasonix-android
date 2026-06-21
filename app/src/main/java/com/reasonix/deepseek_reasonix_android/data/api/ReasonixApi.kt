package com.reasonix.deepseek_reasonix_android.data.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reasonix.deepseek_reasonix_android.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Reasonix REST API — 对应 index.html 中 fetch() 调用的所有后端接口。
 */
class ReasonixApi(
    private val baseUrl: String,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()
) {
    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    // ── 发送消息 ──
    suspend fun submit(input: String): Boolean = withContext(Dispatchers.IO) {
        post("/submit", mapOf("input" to input))
        true
    }

    // ── 取消当前操作 ──
    suspend fun cancel() = withContext(Dispatchers.IO) {
        post("/cancel")
    }

    // ── 获取历史消息 ──
    suspend fun getHistory(): List<HistoryMessage> = withContext(Dispatchers.IO) {
        val json = get("/history")
        if (json.isNullOrBlank()) return@withContext emptyList()
        try {
            gson.fromJson(json, object : TypeToken<List<HistoryMessage>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ── 获取服务器状态 ──
    suspend fun getStatus(): StatusInfo? = withContext(Dispatchers.IO) {
        val json = get("/status")
        if (json.isNullOrBlank()) return@withContext null
        try {
            gson.fromJson(json, StatusInfo::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // ── 会话列表 ──
    suspend fun getSessions(): List<SessionInfo> = withContext(Dispatchers.IO) {
        val json = get("/sessions")
        if (json.isNullOrBlank()) return@withContext emptyList()
        try {
            gson.fromJson(json, object : TypeToken<List<SessionInfo>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ── 新建会话 ──
    suspend fun newSession() = withContext(Dispatchers.IO) {
        post("/new")
    }

    // ── 恢复会话 ──
    suspend fun resumeSession(path: String) = withContext(Dispatchers.IO) {
        post("/resume", mapOf("path" to path))
    }

    // ── 删除会话 ──
    suspend fun deleteSession(name: String) = withContext(Dispatchers.IO) {
        post("/delete-session", mapOf("name" to name))
    }

    // ── 压缩对话 ──
    suspend fun compact() = withContext(Dispatchers.IO) {
        post("/compact")
    }

    // ── 获取检查点 ──
    suspend fun getCheckpoints(): List<CheckpointInfo> = withContext(Dispatchers.IO) {
        val json = get("/checkpoints")
        if (json.isNullOrBlank()) return@withContext emptyList()
        try {
            gson.fromJson(json, object : TypeToken<List<CheckpointInfo>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ── 回退 ──
    suspend fun rewind(turn: Int, scope: String = "both") = withContext(Dispatchers.IO) {
        post("/rewind", mapOf("turn" to turn, "scope" to scope))
    }

    // ── 分叉 ──
    suspend fun fork(turn: Int, name: String = "") = withContext(Dispatchers.IO) {
        post("/fork", mapOf("turn" to turn, "name" to name))
    }

    // ── 总结 ──
    suspend fun summarize(turn: Int, mode: String) = withContext(Dispatchers.IO) {
        post("/summarize", mapOf("turn" to turn, "mode" to mode))
    }

    // ── 批准工具 ──
    suspend fun approve(
        id: String,
        allow: Boolean,
        session: Boolean = false,
        persist: Boolean = false,
        scope: String = ""
    ) = withContext(Dispatchers.IO) {
        post("/approve", mapOf(
            "id" to id,
            "allow" to allow,
            "session" to session,
            "persist" to persist,
            "scope" to scope
        ))
    }

    // ── 回答提问卡片 ──
    suspend fun answer(id: String, answers: List<Map<String, Any>>) = withContext(Dispatchers.IO) {
        post("/answer", mapOf("id" to id, "answers" to answers))
    }

    // ── 计划模式 ──
    suspend fun setPlan(on: Boolean) = withContext(Dispatchers.IO) {
        post("/plan", mapOf("on" to on))
    }

    // ── 工具审批模式 ──
    suspend fun setToolApprovalMode(mode: String) = withContext(Dispatchers.IO) {
        post("/tool-approval-mode", mapOf("mode" to mode))
    }

    // ═══════════════════════════════════════════════
    // 内部 HTTP 辅助
    // ═══════════════════════════════════════════════

    private suspend fun get(path: String): String? {
        val request = Request.Builder()
            .url("$baseUrl$path")
            .get()
            .build()
        return execute(request)
    }

    private suspend fun post(path: String, body: Any? = null) {
        val requestBody = if (body != null) {
            gson.toJson(body).toRequestBody(jsonMediaType)
        } else {
            "{}".toRequestBody(jsonMediaType)
        }
        val request = Request.Builder()
            .url("$baseUrl$path")
            .post(requestBody)
            .build()
        execute(request)
    }

    private suspend fun execute(request: Request): String? {
        return try {
            val response = client.newCall(request).execute()
            response.body?.string().also { response.close() }
        } catch (e: IOException) {
            null
        }
    }
}
