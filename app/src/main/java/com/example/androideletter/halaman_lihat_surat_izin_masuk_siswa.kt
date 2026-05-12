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

class halaman_lihat_surat_izin_masuk_siswa : AppCompatActivity() {

    private lateinit var rvSuratMasuk: RecyclerView
    private lateinit var tvTotalIzin: TextView
    private lateinit var tvTotalDisetujui: TextView
    private lateinit var tvTotalDitolak: TextView
    private lateinit var etCariSurat: EditText
    private lateinit var btnFilter: MaterialCardView

    // Variabel Data dan State Filter/Search
    private var listSuratMasuk = listOf<RiwayatSuratResponse>()
    private var currentKeyword = ""
    private var currentFilterStatus = "semua" // "semua", "pending", "approved", "rejected"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lihat_surat_izin_masuk_siswa)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        rvSuratMasuk = findViewById(R.id.rv_surat_masuk)
        rvSuratMasuk.layoutManager = LinearLayoutManager(this)

        tvTotalIzin = findViewById(R.id.tv_total_izin)
        tvTotalDisetujui = findViewById(R.id.tv_total_disetujui)
        tvTotalDitolak = findViewById(R.id.tv_total_ditolak)
        etCariSurat = findViewById(R.id.et_cari_surat)
        btnFilter = findViewById(R.id.btn_filter)

        muatDataSuratMasuk()

        etCariSurat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentKeyword = s.toString().lowercase()
                terapkanFilterDanPencarian()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnFilter.setOnClickListener {
            tampilkanDialogFilter()
        }
    }

    private fun muatDataSuratMasuk() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getHistorySurat(token).enqueue(object : Callback<List<RiwayatSuratResponse>> {
            override fun onResponse(call: Call<List<RiwayatSuratResponse>>, response: Response<List<RiwayatSuratResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    listSuratMasuk = response.body()!!.filter { it.type_code == "izin_masuk" }
                    terapkanFilterDanPencarian()
                    hitungStatistik(listSuratMasuk)
                } else {
                    Toast.makeText(this@halaman_lihat_surat_izin_masuk_siswa, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<RiwayatSuratResponse>>, t: Throwable) {
                Toast.makeText(this@halaman_lihat_surat_izin_masuk_siswa, "Error koneksi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun terapkanFilterDanPencarian() {
        val listFiltered = listSuratMasuk.filter { surat ->
            val matchKeyword = surat.request_number?.lowercase()?.contains(currentKeyword) == true ||
                    surat.title?.lowercase()?.contains(currentKeyword) == true

            val matchStatus = if (currentFilterStatus == "semua") {
                true
            } else {
                surat.status == currentFilterStatus
            }
            matchKeyword && matchStatus
        }
        rvSuratMasuk.adapter = RiwayatAdapter(listFiltered)
    }

    private fun tampilkanDialogFilter() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_filter_surat, null)
        bottomSheetDialog.setContentView(view)

        var tempFilterStatus = currentFilterStatus

        val cardSemua = view.findViewById<MaterialCardView>(R.id.filter_semua)
        val rbSemua = view.findViewById<RadioButton>(R.id.rb_semua)
        val cardMenunggu = view.findViewById<MaterialCardView>(R.id.filter_menunggu)
        val rbMenunggu = view.findViewById<RadioButton>(R.id.rb_menunggu)
        val cardDisetujui = view.findViewById<MaterialCardView>(R.id.filter_disetujui)
        val rbDisetujui = view.findViewById<RadioButton>(R.id.rb_disetujui)
        val cardDitolak = view.findViewById<MaterialCardView>(R.id.filter_ditolak)
        val rbDitolak = view.findViewById<RadioButton>(R.id.rb_ditolak)

        fun updateUIDialog(statusDipilih: String) {
            val bgPasif = Color.parseColor("#F9FAFB")
            val strokePasif = Color.parseColor("#E5E7EB")
            val tintPasif = ColorStateList.valueOf(Color.parseColor("#D1D5DB"))

            cardSemua.setCardBackgroundColor(bgPasif); cardSemua.strokeColor = strokePasif; rbSemua.isChecked = false; rbSemua.buttonTintList = tintPasif
            cardMenunggu.setCardBackgroundColor(bgPasif); cardMenunggu.strokeColor = strokePasif; rbMenunggu.isChecked = false; rbMenunggu.buttonTintList = tintPasif
            cardDisetujui.setCardBackgroundColor(bgPasif); cardDisetujui.strokeColor = strokePasif; rbDisetujui.isChecked = false; rbDisetujui.buttonTintList = tintPasif
            cardDitolak.setCardBackgroundColor(bgPasif); cardDitolak.strokeColor = strokePasif; rbDitolak.isChecked = false; rbDitolak.buttonTintList = tintPasif

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

        updateUIDialog(tempFilterStatus)

        cardSemua.setOnClickListener { tempFilterStatus = "semua"; updateUIDialog(tempFilterStatus) }
        cardMenunggu.setOnClickListener { tempFilterStatus = "pending"; updateUIDialog(tempFilterStatus) }
        cardDisetujui.setOnClickListener { tempFilterStatus = "approved"; updateUIDialog(tempFilterStatus) }
        cardDitolak.setOnClickListener { tempFilterStatus = "rejected"; updateUIDialog(tempFilterStatus) }

        view.findViewById<ImageView>(R.id.btn_tutup_filter).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btn_reset_filter).setOnClickListener {
            currentFilterStatus = "semua"
            terapkanFilterDanPencarian()
            bottomSheetDialog.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btn_terapkan_filter).setOnClickListener {
            currentFilterStatus = tempFilterStatus
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