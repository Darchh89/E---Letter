package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.card.MaterialCardView

class halaman_panduan_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_panduan_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI ACCORDION
        // ==========================================
        val headerDispensasi = findViewById<MaterialCardView>(R.id.header_dispensasi)
        val contentDispensasi = findViewById<MaterialCardView>(R.id.content_dispensasi)
        val arrowDispensasi = findViewById<ImageView>(R.id.arrow_dispensasi)

        val headerMasuk = findViewById<MaterialCardView>(R.id.header_masuk)
        val contentMasuk = findViewById<MaterialCardView>(R.id.content_masuk)
        val arrowMasuk = findViewById<ImageView>(R.id.arrow_masuk)

        val headerKeluar = findViewById<MaterialCardView>(R.id.header_keluar)
        val contentKeluar = findViewById<MaterialCardView>(R.id.content_keluar)
        val arrowKeluar = findViewById<ImageView>(R.id.arrow_keluar)

        // FUNGSI ANIMASI BUKA-TUTUP (ACCORDION)
        fun toggleAccordion(content: View, arrow: ImageView) {
            if (content.visibility == View.GONE) {
                content.visibility = View.VISIBLE
                // Panah diputar menghadap ke atas (0 derajat) karena sudah terbuka
                arrow.animate().rotation(0f).setDuration(200).start()
            } else {
                content.visibility = View.GONE
                // Panah diputar menghadap ke bawah (180 derajat) karena ditutup
                arrow.animate().rotation(180f).setDuration(200).start()
            }
        }

        headerDispensasi.setOnClickListener { toggleAccordion(contentDispensasi, arrowDispensasi) }
        headerMasuk.setOnClickListener { toggleAccordion(contentMasuk, arrowMasuk) }
        headerKeluar.setOnClickListener { toggleAccordion(contentKeluar, arrowKeluar) }

        // ==========================================
        // DEKLARASI & LOGIKA BOTTOM NAVIGATION
        // ==========================================
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)
        // navPanduan sedang aktif

        navBeranda.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_siswa::class.java))
            finish()
        }

        navRiwayat.setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_siswa::class.java))
            finish()
        }

        navProfil.setOnClickListener {
            startActivity(Intent(this, halaman_profil_siswa::class.java))
            finish()
        }
    }
}