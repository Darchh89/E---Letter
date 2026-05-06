package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class halaman_profil_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_profil_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI MENU AKUN
        // ==========================================
        val btnEditProfil = findViewById<LinearLayout>(R.id.btn_edit_profil)

        // Klik Edit Profil -> Pindah ke halaman edit profil
        btnEditProfil.setOnClickListener {
            val intent = Intent(this, halaman_edit_profil_siswa::class.java)
            startActivity(intent)
        }

        // ==========================================
        // DEKLARASI NAVIGASI BAWAH
        // ==========================================
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)

        navBeranda.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_siswa::class.java))
            finish()
        }

        navPanduan.setOnClickListener {
            startActivity(Intent(this, halaman_panduan_siswa::class.java))
            finish()
        }

        navRiwayat.setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_siswa::class.java))
            finish()
        }
    }
}