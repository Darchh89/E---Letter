package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.card.MaterialCardView

class halaman_kelola_surat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_kelola_surat)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val btnKeluar = findViewById<LinearLayout>(R.id.btn_keluar)
        val tabDasbor = findViewById<MaterialCardView>(R.id.tab_dasbor)
        val tabUnduh = findViewById<MaterialCardView>(R.id.tab_unduh)
        val tabPengguna = findViewById<MaterialCardView>(R.id.tab_pengguna)

        // LOGIKA TOMBOL KELUAR DINONAKTIFKAN
        btnKeluar.setOnClickListener {
            Toast.makeText(this, "Fungsi Keluar dinonaktifkan", Toast.LENGTH_SHORT).show()
        }

        // LOGIKA NAVIGASI TAB
        tabDasbor.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_admin::class.java))
            finish()
        }
        tabUnduh.setOnClickListener {
            startActivity(Intent(this, halaman_unduh_surat::class.java))
            finish()
        }
        tabPengguna.setOnClickListener {
            startActivity(Intent(this, halaman_kelola_pengguna::class.java))
            finish()
        }
    }
}