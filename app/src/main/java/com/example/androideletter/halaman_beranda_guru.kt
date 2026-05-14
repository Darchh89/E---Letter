package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class halaman_beranda_guru : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_beranda_guru)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI BOTTOM NAVIGATION
        // ==========================================
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        // Karena ini sudah di Beranda, tombol Beranda tidak perlu pindah ke mana-mana

        navRiwayat?.setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_guru::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navPanduan?.setOnClickListener {
            startActivity(Intent(this, halaman_panduan_guru::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navProfil?.setOnClickListener {
            // Pastikan Anda sudah membuat class halaman_profil_guru
            // startActivity(Intent(this, halaman_profil_guru::class.java))
            // overridePendingTransition(0, 0)
            // finish()
        }
    }
}