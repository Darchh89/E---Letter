package com.example.androideletter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
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

class halaman_riwayat_siswa : AppCompatActivity() {

    // Deklarasi RecyclerView
    private lateinit var rvRiwayat: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_riwayat_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // INISIALISASI RECYCLERVIEW UNTUK RIWAYAT
        // ==========================================
        rvRiwayat = findViewById(R.id.rv_riwayat)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        // Panggil fungsi untuk mengambil data surat dari server
        muatRiwayatSurat()

        // ==========================================
        // HUBUNGKAN TOMBOL NAVIGASI BAWAH
        // ==========================================
        val navBeranda = findViewById<LinearLayout>(R.id.nav_beranda)
        val navPanduan = findViewById<LinearLayout>(R.id.nav_panduan)
        val navProfil = findViewById<LinearLayout>(R.id.nav_profil) // Menambahkan inisialisasi Profil

        // 1. Jika tombol Beranda ditekan
        navBeranda.setOnClickListener {
            val intent = Intent(this, halaman_beranda_siswa::class.java)
            // Menggunakan flag agar kembali ke beranda utama tanpa menumpuk activity baru
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Tutup halaman riwayat
        }

        // 2. Jika tombol Panduan ditekan
        navPanduan.setOnClickListener {
            val intent = Intent(this, halaman_panduan_siswa::class.java)
            startActivity(intent)
            finish() // Tutup halaman riwayat
        }

        // 3. Jika tombol Profil ditekan
        navProfil.setOnClickListener {
            val intent = Intent(this, halaman_profil_siswa::class.java)
            startActivity(intent)
            finish() // Tutup halaman riwayat
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

                    // Masukkan data dari database ke Adapter agar dicetak ke layar
                    val adapter = RiwayatAdapter(dataRiwayat)
                    rvRiwayat.adapter = adapter
                } else {
                    Toast.makeText(this@halaman_riwayat_siswa, "Gagal memuat riwayat surat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RiwayatSuratResponse>>, t: Throwable) {
                Toast.makeText(this@halaman_riwayat_siswa, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}