package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class halaman_riwayat_guru : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_riwayat_guru)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // FITUR FILTER (BOTTOM SHEET)
        // ==========================================
        val btnFilter = findViewById<MaterialCardView>(R.id.btn_filter)
        btnFilter?.setOnClickListener {
            tampilkanDialogFilter()
        }

        // ==========================================
        // NAVIGASI BAWAH (BOTTOM NAVIGATION)
        // ==========================================
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navRiwayat = findViewById<LinearLayout>(R.id.nav_riwayat)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        navBeranda?.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_guru::class.java))
            overridePendingTransition(0, 0) // Menghilangkan animasi transisi agar mulus
            finish()
        }

        // Karena ini sedang berada di halaman Riwayat, tombol riwayat tidak perlu diberi aksi pindah
        // navRiwayat?.setOnClickListener { }

        navPanduan?.setOnClickListener {
            startActivity(Intent(this, halaman_panduan_guru::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navProfil?.setOnClickListener {
            // Pastikan Anda sudah membuat class halaman_profil_guru terlebih dahulu
            // startActivity(Intent(this, halaman_profil_guru::class.java))
            // overridePendingTransition(0, 0)
            // finish()
        }
    }

    private fun tampilkanDialogFilter() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.dialog_filter_riwayat_guru)

        // Deklarasi komponen di dalam dialog
        val btnTutup = bottomSheetDialog.findViewById<ImageView>(R.id.btn_tutup_filter)
        val btnReset = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_reset_filter)
        val btnTerapkan = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_terapkan_filter)

        // Aksi tombol tutup (X)
        btnTutup?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Aksi tombol reset
        btnReset?.setOnClickListener {
            // Logika reset dropdown Anda bisa diletakkan di sini nanti
            bottomSheetDialog.dismiss()
        }

        // Aksi tombol terapkan
        btnTerapkan?.setOnClickListener {
            // Logika menyimpan state filter dan memperbarui list bisa diletakkan di sini nanti
            bottomSheetDialog.dismiss()
        }

        // Menampilkan dialog ke layar
        bottomSheetDialog.show()
    }
}