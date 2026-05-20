package com.example.androideletter.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("EletterPrefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ROLE = "user_role"
        const val USER_NAME = "user_name"
    }

    /**
     * Menyimpan token JWT dari backend
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Mengambil token JWT (tambahkan "Bearer " di depannya saat memanggil API)
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Menyimpan data user lainnya (opsional, tapi berguna untuk UI)
     */
    fun saveUserData(role: String, name: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROLE, role)
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    /**
     * Menghapus sesi saat logout
     */
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}