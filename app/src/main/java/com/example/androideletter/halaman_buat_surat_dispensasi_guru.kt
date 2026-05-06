package com.example.androideletter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class halaman_buat_surat_dispensasi_guru : AppCompatActivity() {

    private var tanggalTerpilihRaw = "" // Untuk menyimpan hasil format tanggal sementara

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_dispensasi_guru)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI ELEMEN
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnPilihTanggal = findViewById<MaterialCardView>(R.id.btn_pilih_tanggal)
        val tvTanggalTerpilih = findViewById<TextView>(R.id.tv_tanggal_terpilih)
        val layoutKalender = findViewById<MaterialCardView>(R.id.layout_kalender)
        val calendarView = findViewById<CalendarView>(R.id.calendar_view)
        val btnSimpanTanggal = findViewById<MaterialButton>(R.id.btn_simpan_tanggal)

        // LOGIKA KEMBALI
        btnBack.setOnClickListener { finish() }

        // LOGIKA BUKA/TUTUP KALENDER
        btnPilihTanggal.setOnClickListener {
            if (layoutKalender.visibility == View.GONE) {
                layoutKalender.visibility = View.VISIBLE
            } else {
                layoutKalender.visibility = View.GONE
            }
        }

        // LOGIKA SAAT TANGGAL DIKLIK DI KALENDER
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Mengambil nama hari
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val namaHari = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
            val namaBulan = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")

            val hariFix = namaHari[dayOfWeek - 1]
            val bulanFix = namaBulan[month]

            // Format jadi: "Rabu, 11 Desember 2025"
            tanggalTerpilihRaw = "$hariFix, $dayOfMonth $bulanFix $year"
        }

        // LOGIKA TOMBOL SIMPAN TANGGAL
        btnSimpanTanggal.setOnClickListener {
            // Jika user langsung klik simpan tanpa memilih tanggal baru, kita ambil tanggal hari ini
            if (tanggalTerpilihRaw.isEmpty()) {
                val calendar = Calendar.getInstance()
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                val namaHari = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
                val namaBulan = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")

                tanggalTerpilihRaw = "${namaHari[dayOfWeek - 1]}, $dayOfMonth ${namaBulan[month]} $year"
            }

            // Ubah teks & warnanya menjadi hitam tebal
            tvTanggalTerpilih.text = tanggalTerpilihRaw
            tvTanggalTerpilih.setTextColor(Color.BLACK)
            tvTanggalTerpilih.setTypeface(null, android.graphics.Typeface.BOLD)

            // Sembunyikan kalender kembali
            layoutKalender.visibility = View.GONE
        }
    }
}