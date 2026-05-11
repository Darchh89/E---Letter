package com.example.androideletter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.androideletter.model.DetailApproval
import com.example.androideletter.model.DetailSuratResponse
import com.example.androideletter.model.RiwayatSuratResponse
import com.example.androideletter.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_detail_surat_izin_keluar : AppCompatActivity() {

    private lateinit var llWadahSiswa: LinearLayout
    private lateinit var llWadahTtd: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_detail_surat_izin_keluar)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        llWadahSiswa = findViewById(R.id.ll_wadah_siswa)
        llWadahTtd = findViewById(R.id.ll_wadah_ttd)

        // Tangkap data dasar dari intent Adapter Riwayat
        val dataSuratDasar = intent.getParcelableExtra<RiwayatSuratResponse>("DATA_SURAT")

        if (dataSuratDasar != null) {
            muatDetailPenuhSurat(dataSuratDasar.id)
        } else {
            Toast.makeText(this, "Data surat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun muatDetailPenuhSurat(requestId: Int) {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getDetailSurat(token, requestId).enqueue(object : Callback<DetailSuratResponse> {
            override fun onResponse(call: Call<DetailSuratResponse>, response: Response<DetailSuratResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val detail = response.body()!!
                    terapkanDataKeUI(detail)
                }
            }

            override fun onFailure(call: Call<DetailSuratResponse>, t: Throwable) {
                Toast.makeText(this@halaman_detail_surat_izin_keluar, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun terapkanDataKeUI(detail: DetailSuratResponse) {
        // 1. Set Info Teks Dasar
        findViewById<TextView>(R.id.tv_detail_tanggal).text = detail.request_date
        findViewById<TextView>(R.id.tv_detail_waktu).text = "${detail.start_time} - ${detail.end_time}"
        findViewById<TextView>(R.id.tv_detail_alasan).text = detail.reason

        // Info Permohonan
        findViewById<TextView>(R.id.tv_id_permohonan).text = detail.request_number
        findViewById<TextView>(R.id.tv_tanggal_pembuatan).text = detail.created_at_formatted

        // 2. Looping Daftar Siswa
        llWadahSiswa.removeAllViews()
        for (siswa in detail.students) {
            val viewSiswa = layoutInflater.inflate(R.layout.item_detail_siswa_row, llWadahSiswa, false)
            viewSiswa.findViewById<TextView>(R.id.tv_row_nama_siswa).text = siswa.full_name
            viewSiswa.findViewById<TextView>(R.id.tv_row_kelas_siswa).text = siswa.class_name ?: "Belum ada kelas"
            llWadahSiswa.addView(viewSiswa)
        }

        // 3. TANDA TANGAN (HANYA SISWA & PETUGAS)
        llWadahTtd.removeAllViews()
        val ttdSiswa = detail.approvals.find { it.approver_role == "student" }
        val ttdPetugas = detail.approvals.find { it.approver_role == "tatib" }

        tambahkanKotakTtd("student", "Siswa Pemohon", ttdSiswa)
        tambahkanKotakTtd("tatib", "Petugas Piket", ttdPetugas)
    }

    // ==========================================
    // FUNGSI UNTUK MENCETAK KOTAK TANDA TANGAN
    // ==========================================
    private fun tambahkanKotakTtd(roleCode: String, namaDefault: String, dataApproval: DetailApproval?) {
        // Inflate desain item_detail_ttd_box.xml
        val viewTtd = layoutInflater.inflate(R.layout.item_detail_ttd_box, llWadahTtd, false)

        val tvNama = viewTtd.findViewById<TextView>(R.id.tv_nama_ttd)
        val tvRole = viewTtd.findViewById<TextView>(R.id.tv_role_ttd)
        val tvStatus = viewTtd.findViewById<TextView>(R.id.tv_status_ttd)
        val ivTtd = viewTtd.findViewById<ImageView>(R.id.iv_gambar_ttd)

        // Set nama perannya
        tvRole.text = if (roleCode == "student") "Siswa (Pemohon)" else "Petugas / Tatib"

        // Jika data dari database ditemukan
        if (dataApproval != null) {
            tvNama.text = dataApproval.approver_name

            if (dataApproval.status == "approved") {
                // TTD Disetujui
                tvStatus.visibility = View.GONE
                ivTtd.visibility = View.VISIBLE

                // Logika untuk menampilkan gambar TTD asli dari server menggunakan Glide
                if (!dataApproval.signature_url.isNullOrEmpty()) {
                    val baseUrl = "http://192.168.1.6:3000" // Pastikan IP ini sesuai dengan IP komputer Anda saat ini

                    Glide.with(this)
                        .load(baseUrl + dataApproval.signature_url)
                        .placeholder(R.drawable.icon_disetujui_riwayat) // Gambar sementara saat proses loading
                        .error(R.drawable.icon_disetujui_riwayat)       // Gambar jika gagal dimuat / URL rusak
                        .into(ivTtd)
                } else {
                    // Fallback: Jika di database statusnya approved tapi tidak ada file gambar
                    ivTtd.setImageResource(R.drawable.icon_disetujui_riwayat)
                }

            } else if (dataApproval.status == "rejected") {
                // TTD Ditolak
                tvStatus.visibility = View.VISIBLE
                ivTtd.visibility = View.GONE
                tvStatus.text = "DITOLAK"
                tvStatus.setTextColor(Color.parseColor("#C62828"))
            } else {
                // TTD Menunggu / Pending
                tvStatus.visibility = View.VISIBLE
                ivTtd.visibility = View.GONE
                tvStatus.text = "MENUNGGU"
                tvStatus.setTextColor(Color.parseColor("#F57F17"))
            }
        }
        // Jika data dari database belum ada sama sekali (menjamin kotak tetap muncul)
        else {
            tvNama.text = namaDefault
            tvStatus.visibility = View.VISIBLE
            ivTtd.visibility = View.GONE
            tvStatus.text = "MENUNGGU"
            tvStatus.setTextColor(Color.parseColor("#F57F17"))
        }

        // Tambahkan ke dalam horizontal scroll view
        llWadahTtd.addView(viewTtd)
    }
}