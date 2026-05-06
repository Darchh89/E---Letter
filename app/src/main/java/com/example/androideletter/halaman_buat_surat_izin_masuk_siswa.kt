package com.example.androideletter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class halaman_buat_surat_izin_masuk_siswa : AppCompatActivity() {

    private var tanggalTerpilihRaw = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_izin_masuk_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI ELEMEN UMUM
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnBuatSurat = findViewById<MaterialCardView>(R.id.btn_buat_surat)

        // DEKLARASI ELEMEN KELAS
        val btnKelas = findViewById<MaterialCardView>(R.id.btn_kelas)
        val tvKelasTerpilih = findViewById<TextView>(R.id.tv_kelas_terpilih)
        val layoutDropdownKelas = findViewById<MaterialCardView>(R.id.layout_dropdown_kelas)

        val optKelas1 = findViewById<TextView>(R.id.opt_kelas_1)
        val optKelas2 = findViewById<TextView>(R.id.opt_kelas_2)
        val optKelas3 = findViewById<TextView>(R.id.opt_kelas_3)
        val opsiKelas = listOf(optKelas1, optKelas2, optKelas3)

        // DEKLARASI ELEMEN KALENDER
        val btnPilihTanggal = findViewById<MaterialCardView>(R.id.btn_pilih_tanggal)
        val tvTanggalTerpilih = findViewById<TextView>(R.id.tv_tanggal_terpilih)
        val layoutKalender = findViewById<MaterialCardView>(R.id.layout_kalender)
        val calendarView = findViewById<CalendarView>(R.id.calendar_view)
        val btnSimpanTanggal = findViewById<MaterialButton>(R.id.btn_simpan_tanggal)

        // DEKLARASI ELEMEN DROPDOWN KONSENTRASI
        val btnKonsentrasi = findViewById<MaterialCardView>(R.id.btn_konsentrasi)
        val tvKonsentrasiTerpilih = findViewById<TextView>(R.id.tv_konsentrasi_terpilih)
        val layoutDropdownKonsentrasi = findViewById<MaterialCardView>(R.id.layout_dropdown_konsentrasi)

        val opt1 = findViewById<TextView>(R.id.opt_1)
        val opt2 = findViewById<TextView>(R.id.opt_2)
        val opt3 = findViewById<TextView>(R.id.opt_3)
        val opt4 = findViewById<TextView>(R.id.opt_4)
        val opt5 = findViewById<TextView>(R.id.opt_5)
        val opt6 = findViewById<TextView>(R.id.opt_6)
        val opt7 = findViewById<TextView>(R.id.opt_7)
        val opt8 = findViewById<TextView>(R.id.opt_8)

        val opsiKonsentrasi = listOf(opt1, opt2, opt3, opt4, opt5, opt6, opt7, opt8)

        // ----------------------------------------------------
        // FUNGSI PENUTUP MENU OTOMATIS
        // ----------------------------------------------------
        fun tutupSemuaMenuKecuali(menuAktif: MaterialCardView) {
            if (menuAktif != layoutDropdownKelas) layoutDropdownKelas.visibility = View.GONE
            if (menuAktif != layoutDropdownKonsentrasi) layoutDropdownKonsentrasi.visibility = View.GONE
            if (menuAktif != layoutKalender) layoutKalender.visibility = View.GONE
        }

        // ----------------------------------------------------
        // LOGIKA KEMBALI & BUAT SURAT
        // ----------------------------------------------------
        btnBack.setOnClickListener { finish() }

        btnBuatSurat.setOnClickListener {
            Toast.makeText(this, "Surat Izin Masuk Berhasil Dibuat!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // ----------------------------------------------------
        // LOGIKA DROPDOWN KELAS
        // ----------------------------------------------------
        btnKelas.setOnClickListener {
            tutupSemuaMenuKecuali(layoutDropdownKelas)
            if (layoutDropdownKelas.visibility == View.GONE) layoutDropdownKelas.visibility = View.VISIBLE
            else layoutDropdownKelas.visibility = View.GONE
        }

        opsiKelas.forEach { textView ->
            textView.setOnClickListener {
                tvKelasTerpilih.text = textView.text.toString()
                tvKelasTerpilih.setTextColor(Color.BLACK)
                layoutDropdownKelas.visibility = View.GONE
            }
        }

        // ----------------------------------------------------
        // LOGIKA DROPDOWN KONSENTRASI KEAHLIAN
        // ----------------------------------------------------
        btnKonsentrasi.setOnClickListener {
            tutupSemuaMenuKecuali(layoutDropdownKonsentrasi)
            if (layoutDropdownKonsentrasi.visibility == View.GONE) layoutDropdownKonsentrasi.visibility = View.VISIBLE
            else layoutDropdownKonsentrasi.visibility = View.GONE
        }

        opsiKonsentrasi.forEach { textView ->
            textView.setOnClickListener {
                tvKonsentrasiTerpilih.text = textView.text.toString()
                tvKonsentrasiTerpilih.setTextColor(Color.BLACK)
                layoutDropdownKonsentrasi.visibility = View.GONE
            }
        }

        // ----------------------------------------------------
        // LOGIKA KALENDER
        // ----------------------------------------------------
        btnPilihTanggal.setOnClickListener {
            tutupSemuaMenuKecuali(layoutKalender)
            if (layoutKalender.visibility == View.GONE) layoutKalender.visibility = View.VISIBLE
            else layoutKalender.visibility = View.GONE
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val namaHari = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
            val namaBulan = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")

            val hariFix = namaHari[dayOfWeek - 1]
            val bulanFix = namaBulan[month]

            tanggalTerpilihRaw = "$hariFix, $dayOfMonth $bulanFix $year"
        }

        btnSimpanTanggal.setOnClickListener {
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

            tvTanggalTerpilih.text = tanggalTerpilihRaw
            tvTanggalTerpilih.setTextColor(Color.BLACK)
            layoutKalender.visibility = View.GONE
        }
    }
}