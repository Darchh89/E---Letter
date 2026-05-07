package com.example.androideletter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androideletter.model.RiwayatSuratResponse
import com.google.android.material.card.MaterialCardView

class RiwayatAdapter(private val listRiwayat: List<RiwayatSuratResponse>) :
    RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJenis: TextView = view.findViewById(R.id.tv_jenis_surat)
        val tvTanggalJudul: TextView = view.findViewById(R.id.tv_tanggal_judul)
        val cvBg: MaterialCardView = view.findViewById(R.id.cv_icon_bg)
        val ivIcon: ImageView = view.findViewById(R.id.iv_icon_surat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRiwayat[position]

        // Aman dari nilai kosong (null safety)
        holder.tvJenis.text = item.type_label ?: "Surat Izin"

        // Memotong jam pada format date (misal 2026-04-30T00:00:00.000Z menjadi 2026-04-30)
        val tanggalSingkat = item.request_date?.split("T")?.get(0) ?: "Tanggal Tidak Diketahui"
        val judulAman = item.title ?: "Tanpa Judul"
        holder.tvTanggalJudul.text = "$tanggalSingkat • $judulAman"

        // Mengatur Warna dan Ikon berdasarkan tipe surat
        when (item.type_code) {
            "izin_masuk" -> {
                holder.cvBg.setCardBackgroundColor(Color.parseColor("#F3E5F5"))
                holder.ivIcon.setColorFilter(Color.parseColor("#9C27B0"))
                holder.ivIcon.setImageResource(R.drawable.icon_surat_izin_masuk)
            }
            "izin_keluar" -> {
                holder.cvBg.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
                holder.ivIcon.setColorFilter(Color.parseColor("#FF9800"))
                holder.ivIcon.setImageResource(R.drawable.icon_surat_izin_keluar)
            }
            "dispensasi" -> {
                holder.cvBg.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
                holder.ivIcon.setColorFilter(Color.parseColor("#2196F3"))
                holder.ivIcon.setImageResource(R.drawable.icon_surat_dispensasi)
            }
        }

        // ==========================================================
        // TAMBAHAN: LOGIKA KLIK UNTUK MENUJU HALAMAN DETAIL
        // ==========================================================
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // Tentukan intent (halaman tujuan) berdasarkan tipe surat
            val intent = when (item.type_code) {
                "izin_masuk" -> Intent(context, halaman_detail_surat_izin_masuk::class.java)
                "izin_keluar" -> Intent(context, halaman_detail_surat_izin_keluar::class.java)
                else -> null // Jika ada surat jenis dispensasi, Anda bisa arahkan ke halaman lain nanti
            }

            // Jika intent valid, kirim data Parcelize dan buka halamannya
            intent?.let {
                it.putExtra("DATA_SURAT", item)
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int = listRiwayat.size
}