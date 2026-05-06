package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class halaman_riwayat_guru : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_riwayat_guru)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        navBeranda.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_guru::class.java))
            finish()
        }

        navPanduan.setOnClickListener {
            startActivity(Intent(this, halaman_panduan_guru::class.java))
            finish()
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, halaman_profil_guru::class.java))
            finish()
        }
    }
}