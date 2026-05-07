package com.example.androideletter

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androideletter.model.RiwayatSuratResponse

class halaman_detail_surat_izin_masuk : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_detail_surat_izin_masuk)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val data = intent.getParcelableExtra<RiwayatSuratResponse>("DATA_SURAT")

        if (data != null) {
            findViewById<TextView>(R.id.tv_status_surat).text = data.status?.replaceFirstChar { it.uppercase() }
            findViewById<TextView>(R.id.tv_nama_siswa).text = data.student_name ?: "-"
            findViewById<TextView>(R.id.tv_kelas_siswa).text = data.class_name ?: "-"
            findViewById<TextView>(R.id.tv_instansi).text = "SMKN 02 Singosari"

            findViewById<TextView>(R.id.tv_tanggal_izin).text = data.request_date?.split("T")?.get(0) ?: "-"
            findViewById<TextView>(R.id.tv_waktu_izin).text = "${data.start_time} - ${data.end_time}"
            findViewById<TextView>(R.id.tv_keperluan).text = data.reason ?: "-"

            findViewById<TextView>(R.id.tv_id_permohonan).text = "#${data.request_number}"

            val createdAt = data.created_at?.replace("T", " ")?.substring(0, 16) ?: "-"
            findViewById<TextView>(R.id.tv_tanggal_pembuatan).text = "$createdAt WIB"
        }
    }
}