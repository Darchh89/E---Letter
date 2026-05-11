package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.card.MaterialCardView

class halaman_lihat_surat_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lihat_surat_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI ELEMEN (Sesuaikan ID dengan XML)
        // ==========================================
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        // Menggunakan ID yang benar dari file XML
        val btnLihatDispensasi = findViewById<MaterialCardView>(R.id.btn_lihat_dispensasi)
        val btnLihatMasuk = findViewById<MaterialCardView>(R.id.btn_lihat_masuk)
        val btnLihatKeluar = findViewById<MaterialCardView>(R.id.btn_lihat_keluar)

        // ==========================================
        // LOGIKA KLIK & NAVIGASI
        // ==========================================

        btnBack?.setOnClickListener {
            finish()
        }

        // 1. Tombol Surat Dispensasi
        btnLihatDispensasi?.setOnClickListener {
            val intent = Intent(this, halaman_lihat_surat_dispensasi_siswa::class.java)
            startActivity(intent)
        }

        // 2. Tombol Surat Izin Masuk
        btnLihatMasuk?.setOnClickListener {
            val intent = Intent(this, halaman_lihat_surat_izin_masuk_siswa::class.java)
            startActivity(intent)
        }

        // 3. Tombol Surat Izin Keluar
        btnLihatKeluar?.setOnClickListener {
            val intent = Intent(this, halaman_lihat_surat_izin_keluar_siswa::class.java)
            startActivity(intent)
        }
    }
}