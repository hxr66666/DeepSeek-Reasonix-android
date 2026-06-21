package com.reasonix.deepseek_reasonix_android.data

import android.content.Context

/**
 * 服务器配置本地持久化存储（SharedPreferences）。
 * 保存用户上次输入的 IP / 端口，下次打开自动回填。
 */
object ServerConfigStore {

    private const val PREFS_NAME = "reasonix_server_config"
    private const val KEY_IP = "ip"
    private const val KEY_PORT = "port"

    data class Config(val ip: String = "", val port: String = "")

    fun load(context: Context): Config {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return Config(
            ip = prefs.getString(KEY_IP, "") ?: "",
            port = prefs.getString(KEY_PORT, "") ?: ""
        )
    }

    fun save(context: Context, ip: String, port: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_IP, ip)
            .putString(KEY_PORT, port)
            .apply()
    }
}
