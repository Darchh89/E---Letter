package com.example.androideletter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androideletter.model.RiwayatSuratResponse
import com.example.androideletter.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class halaman_riwayat_siswa : AppCompatActivity() {

    private lateinit var rvRiwayat: RecyclerView
    private lateinit var tvTotalBulanIni: TextView
    private lateinit var tvDisetujui: TextView
    private lateinit var tvMenunggu: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_riwayat_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI VIEW
        // ==========================================
        rvRiwayat = findViewById(R.id.rv_riwayat)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        tvTotalBulanIni = findViewById(R.id.tv_total_bulan_ini)
        tvDisetujui = findViewById(R.id.tv_total_disetujui)
        tvMenunggu = findViewById(R.id.tv_total_menunggu)

        // Panggil fungsi untuk mengambil data riwayat dari server
        muatRiwayatSurat()

        // ==========================================
        // NAVIGASI BAWAH
        // ==========================================
        findViewById<LinearLayout>(R.id.nav_beranda).setOnClickListener {
            val intent = Intent(this, halaman_beranda_siswa::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_panduan).setOnClickListener {
            startActivity(Intent(this, halaman_panduan_siswa::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_profil).setOnClickListener {
            startActivity(Intent(this, halaman_profil_siswa::class.java))
            finish()
        }
    }

    // ==========================================
    // FUNGSI MENGAMBIL DATA RIWAYAT DARI API
    // ==========================================
    private fun muatRiwayatSurat() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getHistorySurat(token).enqueue(object : Callback<List<RiwayatSuratResponse>> {
            override fun onResponse(call: Call<List<RiwayatSuratResponse>>, response: Response<List<RiwayatSuratResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val dataRiwayat = response.body()!!

                    // 1. Tampilkan List RecyclerView menggunakan Adapter
                    val adapter = RiwayatAdapter(dataRiwayat)
                    rvRiwayat.adapter = adapter

                    // 2. Hitung Statistik Kotak Biru Secara Otomatis
                    hitungStatistikBulanIni(dataRiwayat)

                } else {
                    Toast.makeText(this@halaman_riwayat_siswa, "Gagal memuat riwayat surat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RiwayatSuratResponse>>, t: Throwable) {
                Toast.makeText(this@halaman_riwayat_siswa, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ==========================================
    // FUNGSI MENGHITUNG STATISTIK BULAN INI
    // ==========================================
    private fun hitungStatistikBulanIni(dataRiwayat: List<RiwayatSuratResponse>) {
        var totalBulanIni = 0
        var disetujuiBulanIni = 0
        var menungguBulanIni = 0

        // Ambil format bulan dan tahun saat ini (Contoh: "2026-05")
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val currentMonth = sdf.format(Date())

        for (item in dataRiwayat) {
            // Cek apakah tanggal tidak kosong dan diawali dengan tahun-bulan saat ini
            if (item.request_date != null && item.request_date.startsWith(currentMonth)) {
                totalBulanIni++
                when (item.status) {
                    "approved" -> disetujuiBulanIni++
                    "pending"  -> menungguBulanIni++
                }
            }
        }

        // Terapkan hasil hitungan ke UI
        tvTotalBulanIni.text = "$totalBulanIni Izin"
        tvDisetujui.text = "$disetujuiBulanIni Disetujui"
        tvMenunggu.text = "$menungguBulanIni Menunggu"
    }
}