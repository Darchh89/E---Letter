package com.example.androideletter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.card.MaterialCardView

class halaman_lihat_surat_izin_keluar_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lihat_surat_izin_keluar_siswa)

        // HILANGKAN NAVBAR HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Teks Jumlah Surat
        val tvJumlahSurat = findViewById<TextView>(R.id.tv_jumlah_surat)

        // Tombol Kembali
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }

        // --- DEKLARASI TOMBOL FILTER ---
        val filterSemua = findViewById<MaterialCardView>(R.id.filter_semua)
        val tvSemua = findViewById<TextView>(R.id.tv_filter_semua)

        val filterMenunggu = findViewById<MaterialCardView>(R.id.filter_menunggu)
        val tvMenunggu = findViewById<TextView>(R.id.tv_filter_menunggu)

        val filterDisetujui = findViewById<MaterialCardView>(R.id.filter_disetujui)
        val tvDisetujui = findViewById<TextView>(R.id.tv_filter_disetujui)

        val filterDitolak = findViewById<MaterialCardView>(R.id.filter_ditolak)
        val tvDitolak = findViewById<TextView>(R.id.tv_filter_ditolak)

        // --- DEKLARASI KELOMPOK KARTU ---
        val listMenunggu = listOf(
            findViewById<MaterialCardView>(R.id.card_m1),
            findViewById<MaterialCardView>(R.id.card_m2)
        )
        val listDisetujui = listOf(
            findViewById<MaterialCardView>(R.id.card_s1),
            findViewById<MaterialCardView>(R.id.card_s2)
        )
        val listDitolak = listOf(
            findViewById<MaterialCardView>(R.id.card_d1),
            findViewById<MaterialCardView>(R.id.card_d2)
        )

        // --- FUNGSI RESET WARNA TOMBOL KE INACTIVE ---
        fun resetFilterColors() {
            filterSemua.setCardBackgroundColor(Color.WHITE)
            tvSemua.setTextColor(Color.parseColor("#1E293B"))
            filterMenunggu.setCardBackgroundColor(Color.WHITE)
            tvMenunggu.setTextColor(Color.parseColor("#D97706"))
            filterDisetujui.setCardBackgroundColor(Color.WHITE)
            tvDisetujui.setTextColor(Color.parseColor("#2E7D32"))
            filterDitolak.setCardBackgroundColor(Color.WHITE)
            tvDitolak.setTextColor(Color.parseColor("#7F1D1D"))
        }

        // --- LOGIKA KLIK FILTER "SEMUA" ---
        filterSemua.setOnClickListener {
            resetFilterColors()
            filterSemua.setCardBackgroundColor(Color.parseColor("#1E293B"))
            tvSemua.setTextColor(Color.WHITE)

            listMenunggu.forEach { it.visibility = View.VISIBLE }
            listDisetujui.forEach { it.visibility = View.VISIBLE }
            listDitolak.forEach { it.visibility = View.VISIBLE }
            tvJumlahSurat.text = "Daftar Surat (6)"
        }

        // --- LOGIKA KLIK FILTER "MENUNGGU" ---
        filterMenunggu.setOnClickListener {
            resetFilterColors()
            filterMenunggu.setCardBackgroundColor(Color.parseColor("#D97706"))
            tvMenunggu.setTextColor(Color.WHITE)

            listMenunggu.forEach { it.visibility = View.VISIBLE }
            listDisetujui.forEach { it.visibility = View.GONE }
            listDitolak.forEach { it.visibility = View.GONE }
            tvJumlahSurat.text = "Daftar Surat (2)"
        }

        // --- LOGIKA KLIK FILTER "DISETUJUI" ---
        filterDisetujui.setOnClickListener {
            resetFilterColors()
            filterDisetujui.setCardBackgroundColor(Color.parseColor("#2E7D32"))
            tvDisetujui.setTextColor(Color.WHITE)

            listDisetujui.forEach { it.visibility = View.VISIBLE }
            listMenunggu.forEach { it.visibility = View.GONE }
            listDitolak.forEach { it.visibility = View.GONE }
            tvJumlahSurat.text = "Daftar Surat (2)"
        }

        // --- LOGIKA KLIK FILTER "DITOLAK" ---
        filterDitolak.setOnClickListener {
            resetFilterColors()
            filterDitolak.setCardBackgroundColor(Color.parseColor("#7F1D1D"))
            tvDitolak.setTextColor(Color.WHITE)

            listDitolak.forEach { it.visibility = View.VISIBLE }
            listMenunggu.forEach { it.visibility = View.GONE }
            listDisetujui.forEach { it.visibility = View.GONE }
            tvJumlahSurat.text = "Daftar Surat (2)"
        }
    }
}