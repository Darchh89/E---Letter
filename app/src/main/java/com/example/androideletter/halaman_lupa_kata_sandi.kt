package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton

class halaman_lupa_kata_sandi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lupa_kata_sandi)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI ELEMEN
        val btnBackLayout = findViewById<LinearLayout>(R.id.btn_back_layout)
        val btnKirim = findViewById<MaterialButton>(R.id.btn_kirim)
        val tvDaftar = findViewById<TextView>(R.id.tv_daftar)

        // LOGIKA TOMBOL KEMBALI
        btnBackLayout.setOnClickListener {
            finish() // Menutup halaman ini dan kembali ke halaman login
        }

        // LOGIKA TOMBOL KIRIM (Langsung pindah ke Verifikasi OTP)
        btnKirim.setOnClickListener {
            // Berpindah ke halaman verifikasi OTP tanpa pengecekan email
            val intent = Intent(this, halaman_verifikasi_otp::class.java)
            startActivity(intent)
        }

        // LOGIKA TOMBOL DAFTAR
        tvDaftar.setOnClickListener {
            val intent = Intent(this, halaman_daftar_siswa::class.java)
            startActivity(intent)
            finish()
        }
    }
}