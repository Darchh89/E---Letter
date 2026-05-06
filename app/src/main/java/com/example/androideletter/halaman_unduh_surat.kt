package com.example.androideletter

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class halaman_unduh_surat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_unduh_surat)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val btnKeluar = findViewById<LinearLayout>(R.id.btn_keluar)
        val tabDasbor = findViewById<MaterialCardView>(R.id.tab_dasbor)
        val tabKelola = findViewById<MaterialCardView>(R.id.tab_kelola)
        val tabPengguna = findViewById<MaterialCardView>(R.id.tab_pengguna)

        val btnTglAwal = findViewById<MaterialCardView>(R.id.btn_tgl_awal)
        val tvTglAwal = findViewById<TextView>(R.id.tv_tgl_awal)
        val btnTglAkhir = findViewById<MaterialCardView>(R.id.btn_tgl_akhir)
        val tvTglAkhir = findViewById<TextView>(R.id.tv_tgl_akhir)
        val btnFormat = findViewById<MaterialCardView>(R.id.btn_format)
        val tvFormat = findViewById<TextView>(R.id.tv_format)
        val btnUnduh = findViewById<MaterialButton>(R.id.btn_unduh)

        // LOGIKA TOMBOL KELUAR DINONAKTIFKAN
        btnKeluar.setOnClickListener {
            Toast.makeText(this, "Fungsi Keluar dinonaktifkan", Toast.LENGTH_SHORT).show()
        }

        // NAVIGASI TAB
        tabDasbor.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_admin::class.java))
            finish()
        }
        tabKelola.setOnClickListener {
            startActivity(Intent(this, halaman_kelola_surat::class.java))
            finish()
        }
        tabPengguna.setOnClickListener {
            startActivity(Intent(this, halaman_kelola_pengguna::class.java))
            finish()
        }

        // LOGIKA KALENDER
        btnTglAwal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val tanggal = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvTglAwal.text = tanggal
                tvTglAwal.setTextColor(Color.BLACK)
            }, year, month, day)
            datePickerDialog.show()
        }

        btnTglAkhir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val tanggal = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvTglAkhir.text = tanggal
                tvTglAkhir.setTextColor(Color.BLACK)
            }, year, month, day)
            datePickerDialog.show()
        }

        btnFormat.setOnClickListener {
            tvFormat.text = "PDF Document (.pdf)"
            tvFormat.setTextColor(Color.BLACK)
            Toast.makeText(this, "Format diubah ke PDF", Toast.LENGTH_SHORT).show()
        }

        btnUnduh.setOnClickListener {
            Toast.makeText(this, "Laporan sedang diunduh...", Toast.LENGTH_LONG).show()
        }
    }
}