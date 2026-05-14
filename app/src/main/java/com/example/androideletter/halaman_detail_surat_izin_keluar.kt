package com.example.androideletter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androideletter.network.RetrofitClient
import com.example.androideletter.model.DetailSuratResponse
import com.example.androideletter.model.RiwayatSuratResponse
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_detail_surat_izin_keluar : AppCompatActivity() {

    // === GANTI INI DENGAN IP LAPTOP ANDA YANG BENAR ===
    private val serverIp = "10.55.226.99"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_detail_surat_izin_keluar)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // Ambil ID dari Intent
        val dataIntent = intent.getParcelableExtra<RiwayatSuratResponse>("DATA_SURAT")

        if (dataIntent != null) {
            fetchDetailDariServer(dataIntent.id)
        } else {
            Toast.makeText(this, "Data surat tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDetailDariServer(requestId: Int) {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getDetailSurat(token, requestId).enqueue(object : Callback<DetailSuratResponse> {
            override fun onResponse(call: Call<DetailSuratResponse>, response: Response<DetailSuratResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    tampilkanDataKeLayar(response.body()!!)
                } else {
                    Toast.makeText(this@halaman_detail_surat_izin_keluar, "Gagal mengambil detail", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DetailSuratResponse>, t: Throwable) {
                Toast.makeText(this@halaman_detail_surat_izin_keluar, "Koneksi Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanDataKeLayar(data: DetailSuratResponse) {
        findViewById<TextView>(R.id.tv_tanggal_izin).text = data.request_date ?: "-"
        findViewById<TextView>(R.id.tv_waktu_izin).text = "${data.start_time ?: "-"} - ${data.end_time ?: "-"}"
        findViewById<TextView>(R.id.tv_keperluan).text = data.reason ?: "-"
        findViewById<TextView>(R.id.tv_id_permohonan).text = data.request_number ?: "-"
        findViewById<TextView>(R.id.tv_tanggal_pembuatan).text = data.created_at_formatted ?: "-"

        // Dinamis Multi-Siswa dengan Warna ORANYE
        val llContainerSiswa = findViewById<LinearLayout>(R.id.ll_container_siswa)
        llContainerSiswa.removeAllViews()

        for (siswa in data.students) {
            val cardSiswa = MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, 0, 0, 16)
                }
                setCardBackgroundColor(Color.parseColor("#FFF0E6")) // Oren Muda
                radius = 24f
                cardElevation = 0f
            }

            val layoutHorizontal = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(32, 32, 32, 32)
            }

            val cardIkon = MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(90, 90)
                setCardBackgroundColor(Color.parseColor("#FFCC80"))
                radius = 45f
                cardElevation = 0f
            }
            val ikonUser = ImageView(this).apply {
                setImageResource(android.R.drawable.ic_menu_myplaces)
                setColorFilter(Color.parseColor("#E65100"))
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                    setMargins(16, 16, 16, 16)
                }
            }
            cardIkon.addView(ikonUser)

            val layoutTeks = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 0, 0, 0)
            }
            val tvNama = TextView(this).apply {
                text = siswa.full_name
                setTextColor(Color.parseColor("#374151"))
                textSize = 14f
                setTypeface(null, android.graphics.Typeface.BOLD)
            }
            val tvKelas = TextView(this).apply {
                text = siswa.class_name ?: "-"
                setTextColor(Color.parseColor("#6B7280"))
                textSize = 12f
            }

            layoutTeks.addView(tvNama)
            layoutTeks.addView(tvKelas)
            layoutHorizontal.addView(cardIkon)
            layoutHorizontal.addView(layoutTeks)
            cardSiswa.addView(layoutHorizontal)
            llContainerSiswa.addView(cardSiswa)
        }

        // LOGIKA STATUS & WARNA
        val cvWarning = findViewById<MaterialCardView>(R.id.cv_warning_ditolak)
        val cvStatusAtas = findViewById<MaterialCardView>(R.id.cv_status_atas)
        val tvStatusAtas = findViewById<TextView>(R.id.tv_status_atas)
        val tvStatusTtdGuru = findViewById<TextView>(R.id.tv_status_ttd_guru)
        val ivTtdGuru = findViewById<ImageView>(R.id.iv_ttd_guru)

        when (data.status) {
            "approved" -> {
                cvWarning.visibility = View.GONE
                tvStatusAtas.text = "Disetujui"
                tvStatusAtas.setTextColor(Color.parseColor("#4CAF50"))
                cvStatusAtas.setCardBackgroundColor(Color.parseColor("#E8F5E9"))
                tvStatusTtdGuru.visibility = View.GONE
                ivTtdGuru.visibility = View.VISIBLE
            }
            "rejected" -> {
                cvWarning.visibility = View.VISIBLE
                tvStatusAtas.text = "Ditolak"
                tvStatusAtas.setTextColor(Color.parseColor("#F44336"))
                cvStatusAtas.setCardBackgroundColor(Color.parseColor("#FFEBEE"))
                tvStatusTtdGuru.visibility = View.VISIBLE
                tvStatusTtdGuru.text = "DITOLAK"
                tvStatusTtdGuru.setTextColor(Color.parseColor("#F44336"))
                ivTtdGuru.visibility = View.GONE
            }
            else -> {
                cvWarning.visibility = View.GONE
                tvStatusAtas.text = "Menunggu"
                tvStatusAtas.setTextColor(Color.parseColor("#FF9800"))
                cvStatusAtas.setCardBackgroundColor(Color.parseColor("#FFF9C4"))
                tvStatusTtdGuru.visibility = View.VISIBLE
                tvStatusTtdGuru.text = "MENUNGGU"
                tvStatusTtdGuru.setTextColor(Color.parseColor("#374151"))
                ivTtdGuru.visibility = View.GONE
            }
        }

        // RENDER GAMBAR TANDA TANGAN (GLIDE)
        val ivTtdSiswa = findViewById<ImageView>(R.id.iv_ttd_siswa)

        if (!data.student_signature_url.isNullOrEmpty()) {
            ivTtdSiswa.imageTintList = null
            val urlSiswa = "http://$serverIp:3000${data.student_signature_url}"
            Glide.with(this).load(urlSiswa).into(ivTtdSiswa)
        } else {
            ivTtdSiswa.setImageResource(android.R.drawable.ic_menu_edit)
        }

        if (data.students.isNotEmpty()) {
            findViewById<TextView>(R.id.tv_ttd_nama_siswa).text = data.students[0].full_name
        }

        if (data.status == "approved" && data.approvals.isNotEmpty()) {
            val approvalTerakhir = data.approvals[0]
            if (!approvalTerakhir.signature_url.isNullOrEmpty()) {
                ivTtdGuru.imageTintList = null
                val urlGuru = "http://$serverIp:3000${approvalTerakhir.signature_url}"
                Glide.with(this).load(urlGuru).into(ivTtdGuru)
            }
            findViewById<TextView>(R.id.tv_ttd_nama_guru).text = approvalTerakhir.approver_name ?: "Petugas"
        }
    }
}