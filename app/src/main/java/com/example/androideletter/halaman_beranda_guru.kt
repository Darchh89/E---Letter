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

class halaman_beranda_guru : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cukup panggil layout XML saja, bebas dari error
        setContentView(R.layout.halaman_beranda_guru)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI KARTU MENU TENGAH
        // ==========================================
        val cardLihatSurat = findViewById<MaterialCardView>(R.id.card_lihat_surat)
        val cardBuatSurat = findViewById<MaterialCardView>(R.id.card_buat_surat)

        // ==========================================
        // DEKLARASI TOMBOL NAVIGASI BAWAH
        // ==========================================
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        // ==========================================
        // LOGIKA KLIK MENU TENGAH
        // ==========================================

        cardLihatSurat.setOnClickListener {
            // Nanti bisa diarahkan jika halaman lihat surat untuk guru sudah dibuat
            Toast.makeText(this, "Membuka halaman Lihat Surat...", Toast.LENGTH_SHORT).show()
        }

        // MENGARAH KE HALAMAN BUAT SURAT DISPENSASI GURU
        cardBuatSurat.setOnClickListener {
            val intent = Intent(this, halaman_buat_surat_dispensasi_guru::class.java)
            startActivity(intent)
        }

        // ==========================================
        // LOGIKA KLIK NAVIGASI BAWAH
        // ==========================================

        navPanduan.setOnClickListener {
            startActivity(Intent(this, halaman_panduan_guru::class.java))
        }

        navRiwayat.setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_guru::class.java))
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, halaman_profil_guru::class.java))
        }
    }
}