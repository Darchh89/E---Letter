package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class halaman_beranda_admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama layout XML ini sesuai dengan milikmu
        setContentView(R.layout.halaman_beranda_admin)

        // 1. Kenalkan View/Tombol dari XML ke Kotlin
        val btnKeluar = findViewById<LinearLayout>(R.id.btn_keluar)
        val tabKelola = findViewById<MaterialCardView>(R.id.tab_kelola)
        val tabUnduh = findViewById<MaterialCardView>(R.id.tab_unduh)
        val tabPengguna = findViewById<MaterialCardView>(R.id.tab_pengguna)

        // ==========================================
        // 2. Logika Aksi Saat Tombol Ditekan (Klik)
        // ==========================================

        // Logika Tombol Keluar
        btnKeluar.setOnClickListener {
            // Opsional: Hapus sesi/token pengguna di SharedPreferences sebelum keluar
            clearUserSession()

            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()

            // Arahkan kembali ke halaman login (sesuaikan nama class login-mu)
            val intent = Intent(this, halaman_masuk_siswa::class.java)
            // Bersihkan history tumpukan halaman (opsional namun disarankan untuk fitur logout)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Logika Tab Kelola Surat
        tabKelola.setOnClickListener {
            Toast.makeText(this, "Masuk ke menu Kelola Surat", Toast.LENGTH_SHORT).show()
            // Contoh navigasi jika halamannya sudah dibuat:
            // startActivity(Intent(this, halaman_kelola_surat::class.java))
        }

        // Logika Tab Unduh Surat
        tabUnduh.setOnClickListener {
            Toast.makeText(this, "Masuk ke menu Unduh Surat", Toast.LENGTH_SHORT).show()
            // Contoh navigasi jika halamannya sudah dibuat:
            // startActivity(Intent(this, halaman_unduh_surat::class.java))
        }

        // Logika Tab Pengguna
        tabPengguna.setOnClickListener {
            Toast.makeText(this, "Masuk ke menu Pengguna", Toast.LENGTH_SHORT).show()
            // Contoh navigasi jika halamannya sudah dibuat:
            // startActivity(Intent(this, halaman_pengguna::class.java))
        }
    }

    // Fungsi kecil untuk menghapus sesi saat user memilih keluar (Logout)
    private fun clearUserSession() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear() // Menghapus semua data token dan role yang tersimpan
        editor.apply()
    }
}
