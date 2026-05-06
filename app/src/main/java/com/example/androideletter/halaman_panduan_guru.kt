package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class halaman_panduan_guru : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_panduan_guru)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI TOMBOL NAVIGASI BAWAH
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)
        // navPanduan tidak dibuat intent karena sedang berada di halaman ini

        // LOGIKA KLIK NAVIGASI
        navBeranda.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_guru::class.java))
            finish() // Menutup halaman saat ini agar tidak menumpuk di memori
        }

        navRiwayat.setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_guru::class.java))
            finish()
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, halaman_profil_guru::class.java))
            finish()
        }
    }
}