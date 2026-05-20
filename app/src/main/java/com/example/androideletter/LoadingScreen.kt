package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class LoadingScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Delay selama 3 detik (3000ms) untuk menampilkan logo/loading screen, kemudian cek sesi
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 3000)
    }

    private fun checkUserSession() {
        // Ambil data SharedPreferences yang sama dengan halaman login
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = sharedPref.getString("USER_TOKEN", null)
        val role = sharedPref.getString("USER_ROLE", null)

        if (token != null && role != null) {
            // JIKA TOKEN ADA -> Langsung ke Beranda sesuai Role
            val intentBeranda = when (role) {
                "student" -> Intent(this, halaman_beranda_siswa::class.java)
                "teacher" -> Intent(this, halaman_beranda_guru::class.java)
                "admin" -> Intent(this, halaman_beranda_admin::class.java)
                "kepala_sekolah" -> Intent(this, halaman_beranda_kepsek::class.java)
                else -> Intent(this, LanjutkanSebagai::class.java) // Fallback jika terjadi keanehan data
            }
            startActivity(intentBeranda)
            finish() // Tutup LoadingScreen agar tidak bisa di-back
        } else {
            // JIKA TOKEN TIDAK ADA -> Arahkan ke halaman onboarding / pilih role awal
            val intent = Intent(this, LanjutkanSebagai::class.java)
            startActivity(intent)
            finish()
        }
    }
}