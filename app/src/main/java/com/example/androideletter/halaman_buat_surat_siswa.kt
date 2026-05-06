package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.card.MaterialCardView

class halaman_buat_surat_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI ELEMEN
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val cardSuratMasuk = findViewById<MaterialCardView>(R.id.card_surat_masuk)
        val cardSuratKeluar = findViewById<MaterialCardView>(R.id.card_surat_keluar)

        // LOGIKA TOMBOL KEMBALI
        btnBack.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya (Beranda)
        }

        // MENGARAH KE FORM SURAT IZIN MASUK
        cardSuratMasuk.setOnClickListener {
            val intent = Intent(this, halaman_buat_surat_izin_masuk_siswa::class.java)
            startActivity(intent)
        }

        // MENGARAH KE FORM SURAT IZIN KELUAR
        cardSuratKeluar.setOnClickListener {
            val intent = Intent(this, halaman_buat_surat_izin_keluar_siswa::class.java)
            startActivity(intent)
        }
    }
}