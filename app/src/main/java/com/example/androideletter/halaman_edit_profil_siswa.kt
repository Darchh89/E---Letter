package com.example.androideletter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class halaman_edit_profil_siswa : AppCompatActivity() {

    private var jenisKelaminDipilih = "Laki-laki" // Default pilihan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_edit_profil_siswa)

        // Hilangkan Navbar Bawaan HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Tombol Kembali
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // ==========================================
        // LOGIKA DROPDOWN JURUSAN
        // ==========================================
        val spinnerJurusan = findViewById<Spinner>(R.id.spinner_keahlian)
        val daftarKeahlian = arrayOf(
            "Audio Vidio", "Mekatronika", "Elektronika Industri",
            "Teknik Komputer Jaringan", "Rekayasa Perangkat Lunak",
            "Desain Komunikasi Visual", "Broadcasting", "Animasi"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarKeahlian)
        spinnerJurusan.adapter = adapter

        // ==========================================
        // LOGIKA TOGGLE JENIS KELAMIN
        // ==========================================
        val cardLaki = findViewById<MaterialCardView>(R.id.card_laki)
        val tvLaki = findViewById<TextView>(R.id.tv_laki)
        val cardPerempuan = findViewById<MaterialCardView>(R.id.card_perempuan)
        val tvPerempuan = findViewById<TextView>(R.id.tv_perempuan)

        cardLaki.setOnClickListener {
            jenisKelaminDipilih = "Laki-laki"
            cardLaki.setCardBackgroundColor(Color.WHITE)
            cardLaki.cardElevation = 4f
            tvLaki.setTextColor(Color.parseColor("#3FA2F6"))

            cardPerempuan.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
            cardPerempuan.cardElevation = 0f
            tvPerempuan.setTextColor(Color.parseColor("#9E9E9E"))
        }

        cardPerempuan.setOnClickListener {
            jenisKelaminDipilih = "Perempuan"
            cardPerempuan.setCardBackgroundColor(Color.WHITE)
            cardPerempuan.cardElevation = 4f
            tvPerempuan.setTextColor(Color.parseColor("#3FA2F6"))

            cardLaki.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
            cardLaki.cardElevation = 0f
            tvLaki.setTextColor(Color.parseColor("#9E9E9E"))
        }

        // ==========================================
        // TOMBOL LANJUT KE TANDA TANGAN
        // ==========================================
        val btnLanjut = findViewById<MaterialButton>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            // MENGARAH KE HALAMAN KEDUA (TANDA TANGAN)
            val intent = Intent(this, halaman_tanda_tangan_siswa::class.java)
            startActivity(intent)
        }
    }
}