package com.example.androideletter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class halaman_riwayat_siswa : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_riwayat_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // HUBUNGKAN TOMBOL NAVIGASI BAWAH
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil) // Menambahkan inisialisasi Profil

        // 1. Jika tombol Beranda ditekan
        navBeranda.setOnClickListener {
            val intent = Intent(this, halaman_beranda_siswa::class.java)
            // Menggunakan flag agar kembali ke beranda utama tanpa menumpuk activity baru
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Tutup halaman riwayat
        }

        // 2. Jika tombol Panduan ditekan
        navPanduan.setOnClickListener {
            val intent = Intent(this, halaman_panduan_siswa::class.java)
            startActivity(intent)
            finish() // Tutup halaman riwayat
        }

        // 3. Jika tombol Profil ditekan
        navProfil.setOnClickListener {
            val intent = Intent(this, halaman_profil_siswa::class.java)
            startActivity(intent)
            finish() // Tutup halaman riwayat
        }
    }
}