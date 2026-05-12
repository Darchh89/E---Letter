package com.example.androideletter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androideletter.network.RetrofitClient
import com.example.androideletter.model.RiwayatSuratResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_lihat_surat_izin_keluar_siswa : AppCompatActivity() {

    private lateinit var rvSuratKeluar: RecyclerView
    private lateinit var tvTotalIzin: TextView
    private lateinit var tvTotalDisetujui: TextView
    private lateinit var tvTotalDitolak: TextView
    private lateinit var etCariSurat: EditText
    private lateinit var btnFilter: MaterialCardView

    // Variabel Data dan State Filter/Search
    private var listSuratKeluar = listOf<RiwayatSuratResponse>()
    private var currentKeyword = ""
    private var currentFilterStatus = "semua" // "semua", "pending", "approved", "rejected"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lihat_surat_izin_keluar_siswa)

        // Tombol Kembali
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // Inisialisasi View
        rvSuratKeluar = findViewById(R.id.rv_surat_keluar)
        rvSuratKeluar.layoutManager = LinearLayoutManager(this)

        tvTotalIzin = findViewById(R.id.tv_total_izin)
        tvTotalDisetujui = findViewById(R.id.tv_total_disetujui)
        tvTotalDitolak = findViewById(R.id.tv_total_ditolak)
        etCariSurat = findViewById(R.id.et_cari_surat)
        btnFilter = findViewById(R.id.btn_filter)

        // Ambil Data dari API
        muatDataSuratKeluar()

        // Event Listener untuk Pencarian (Ketik)
        etCariSurat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentKeyword = s.toString().lowercase()
                terapkanFilterDanPencarian() // Panggil fungsi gabungan
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Event Listener untuk Tombol Filter
        btnFilter.setOnClickListener {
            tampilkanDialogFilter()
        }
    }

    private fun muatDataSuratKeluar() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getHistorySurat(token).enqueue(object : Callback<List<RiwayatSuratResponse>> {
            override fun onResponse(call: Call<List<RiwayatSuratResponse>>, response: Response<List<RiwayatSuratResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Filter khusus jenis "Izin Keluar" dari database
                    listSuratKeluar = response.body()!!.filter { it.type_code == "izin_keluar" }
                    terapkanFilterDanPencarian() // Tampilkan data ke adapter
                    hitungStatistik(listSuratKeluar) // Update kotak biru
                } else {
                    Toast.makeText(this@halaman_lihat_surat_izin_keluar_siswa, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RiwayatSuratResponse>>, t: Throwable) {
                Toast.makeText(this@halaman_lihat_surat_izin_keluar_siswa, "Error koneksi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ========================================================
    // FUNGSI GABUNGAN: SEARCH & FILTER
    // ========================================================
    private fun terapkanFilterDanPencarian() {
        val listFiltered = listSuratKeluar.filter { surat ->
            // 1. Cek kecocokan pencarian (Keyword)
            val matchKeyword = surat.request_number?.lowercase()?.contains(currentKeyword) == true ||
                    surat.title?.lowercase()?.contains(currentKeyword) == true

            // 2. Cek kecocokan status (Filter Dialog)
            val matchStatus = if (currentFilterStatus == "semua") {
                true
            } else {
                surat.status == currentFilterStatus
            }

            // Harus cocok kedua-duanya
            matchKeyword && matchStatus
        }

        // Terapkan ke RecyclerView
        rvSuratKeluar.adapter = RiwayatAdapter(listFiltered)
    }

    // ========================================================
    // LOGIKA DIALOG FILTER BAWAH (BOTTOM SHEET)
    // ========================================================
    private fun tampilkanDialogFilter() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_filter_surat, null)
        bottomSheetDialog.setContentView(view)

        // Variabel penampung sementara saat dialog sedang dibuka
        var tempFilterStatus = currentFilterStatus

        // Inisialisasi View dari XML Dialog
        val cardSemua = view.findViewById<MaterialCardView>(R.id.filter_semua)
        val rbSemua = view.findViewById<RadioButton>(R.id.rb_semua)

        val cardMenunggu = view.findViewById<MaterialCardView>(R.id.filter_menunggu)
        val rbMenunggu = view.findViewById<RadioButton>(R.id.rb_menunggu)

        val cardDisetujui = view.findViewById<MaterialCardView>(R.id.filter_disetujui)
        val rbDisetujui = view.findViewById<RadioButton>(R.id.rb_disetujui)

        val cardDitolak = view.findViewById<MaterialCardView>(R.id.filter_ditolak)
        val rbDitolak = view.findViewById<RadioButton>(R.id.rb_ditolak)

        // Fungsi internal untuk merubah warna (Aktif/Pasif)
        fun updateUIDialog(statusDipilih: String) {
            // Reset semua ke warna abu-abu / pasif
            val bgPasif = Color.parseColor("#F9FAFB")
            val strokePasif = Color.parseColor("#E5E7EB")
            val tintPasif = ColorStateList.valueOf(Color.parseColor("#D1D5DB"))

            cardSemua.setCardBackgroundColor(bgPasif); cardSemua.strokeColor = strokePasif; rbSemua.isChecked = false; rbSemua.buttonTintList = tintPasif
            cardMenunggu.setCardBackgroundColor(bgPasif); cardMenunggu.strokeColor = strokePasif; rbMenunggu.isChecked = false; rbMenunggu.buttonTintList = tintPasif
            cardDisetujui.setCardBackgroundColor(bgPasif); cardDisetujui.strokeColor = strokePasif; rbDisetujui.isChecked = false; rbDisetujui.buttonTintList = tintPasif
            cardDitolak.setCardBackgroundColor(bgPasif); cardDitolak.strokeColor = strokePasif; rbDitolak.isChecked = false; rbDitolak.buttonTintList = tintPasif

            // Set yang terpilih menjadi biru / aktif
            val bgAktif = Color.parseColor("#F0F8FF")
            val strokeAktif = Color.parseColor("#3FA2F6")
            val tintAktif = ColorStateList.valueOf(Color.parseColor("#3FA2F6"))

            when (statusDipilih) {
                "semua" -> { cardSemua.setCardBackgroundColor(bgAktif); cardSemua.strokeColor = strokeAktif; rbSemua.isChecked = true; rbSemua.buttonTintList = tintAktif }
                "pending" -> { cardMenunggu.setCardBackgroundColor(bgAktif); cardMenunggu.strokeColor = strokeAktif; rbMenunggu.isChecked = true; rbMenunggu.buttonTintList = tintAktif }
                "approved" -> { cardDisetujui.setCardBackgroundColor(bgAktif); cardDisetujui.strokeColor = strokeAktif; rbDisetujui.isChecked = true; rbDisetujui.buttonTintList = tintAktif }
                "rejected" -> { cardDitolak.setCardBackgroundColor(bgAktif); cardDitolak.strokeColor = strokeAktif; rbDitolak.isChecked = true; rbDitolak.buttonTintList = tintAktif }
            }
        }

        // Terapkan warna awal saat dialog dibuka
        updateUIDialog(tempFilterStatus)

        // Event Klik pada setiap kotak di dialog
        cardSemua.setOnClickListener { tempFilterStatus = "semua"; updateUIDialog(tempFilterStatus) }
        cardMenunggu.setOnClickListener { tempFilterStatus = "pending"; updateUIDialog(tempFilterStatus) }
        cardDisetujui.setOnClickListener { tempFilterStatus = "approved"; updateUIDialog(tempFilterStatus) }
        cardDitolak.setOnClickListener { tempFilterStatus = "rejected"; updateUIDialog(tempFilterStatus) }

        // Tombol Tutup (X)
        view.findViewById<ImageView>(R.id.btn_tutup_filter).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Tombol Reset
        view.findViewById<MaterialButton>(R.id.btn_reset_filter).setOnClickListener {
            currentFilterStatus = "semua" // Kembalikan ke semua
            terapkanFilterDanPencarian()
            bottomSheetDialog.dismiss()
        }

        // Tombol Terapkan
        view.findViewById<MaterialButton>(R.id.btn_terapkan_filter).setOnClickListener {
            currentFilterStatus = tempFilterStatus // Simpan pilihan
            terapkanFilterDanPencarian()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun hitungStatistik(data: List<RiwayatSuratResponse>) {
        val total = data.size
        val disetujui = data.count { it.status == "approved" }
        val ditolak = data.count { it.status == "rejected" }

        tvTotalIzin.text = "$total Izin"
        tvTotalDisetujui.text = "✓ $disetujui Disetujui"
        tvTotalDitolak.text = "⊗ $ditolak Ditolak"
    }
}