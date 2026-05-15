package com.example.androideletter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class halaman_detail_surat_dispensasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_detail_surat_dispensasi)

        // Hilangkan Navbar Bawaan HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Tombol Kembali
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // =========================================================================
        // CARA PENGGUNAAN:
        // Ubah nilai di bawah ini menjadi 'true' untuk melihat versi DITOLAK (MERAH)
        // Ubah menjadi 'false' untuk melihat versi DISETUJUI (HIJAU)
        // =========================================================================
        val suratDitolak = false
        setStatusSurat(suratDitolak)

        // ==========================================
        // Memunculkan Pop Up saat tombol Finalisasi di-klik
        // ==========================================
        val btnFinalisasi = findViewById<MaterialButton>(R.id.btn_finalisasi)
        btnFinalisasi?.setOnClickListener {
            tampilkanPopupFinalisasi()
        }
    }

    private fun tampilkanPopupFinalisasi() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Menggunakan layout dialog yang Anda miliki
        dialog.setContentView(R.layout.dialog_surat_berhasil_masuk)

        // Membuat background dialog menjadi transparan agar rounded corners terlihat
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Pastikan ID tombol ini sama dengan yang ada di dalam file dialog_surat_berhasil_masuk.xml Anda
        val btnTinjau = dialog.findViewById<MaterialButton>(R.id.cb_konfirmasi)
        val btnPopupFinalisasi = dialog.findViewById<MaterialButton>(R.id.btn_finalisasi)

        // Aksi Batal / Tinjau Kembali
        btnTinjau?.setOnClickListener {
            dialog.dismiss()
        }

        // Aksi Konfirmasi Finalisasi
        btnPopupFinalisasi?.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Surat berhasil difinalisasi!", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    private fun setStatusSurat(isDitolak: Boolean) {
        val bannerPeringatan = findViewById<MaterialCardView>(R.id.banner_peringatan_ditolak)
        val cardStatusTag = findViewById<MaterialCardView>(R.id.card_status_tag)
        val tvStatusTag = findViewById<TextView>(R.id.tv_status_tag)

        val imgTtd = findViewById<ImageView>(R.id.img_ttd)
        val tvTtdDitolak = findViewById<TextView>(R.id.tv_ttd_ditolak)

        if (isDitolak) {
            // 1. Munculkan Popup/Banner Merah di atas
            bannerPeringatan?.visibility = View.VISIBLE

            // 2. Ubah Tag Status menjadi Merah (Ditolak)
            cardStatusTag?.setCardBackgroundColor(Color.parseColor("#FEE2E2"))
            tvStatusTag?.text = "Ditolak"
            tvStatusTag?.setTextColor(Color.parseColor("#DC2626"))

            // 3. Hilangkan gambar Tanda Tangan, ganti dengan teks "DITOLAK!"
            imgTtd?.visibility = View.GONE
            tvTtdDitolak?.visibility = View.VISIBLE

        } else {
            // 1. Sembunyikan Popup/Banner Merah
            bannerPeringatan?.visibility = View.GONE

            // 2. Ubah Tag Status menjadi Hijau (Disetujui)
            cardStatusTag?.setCardBackgroundColor(Color.parseColor("#DCFCE7"))
            tvStatusTag?.text = "Disetujui"
            tvStatusTag?.setTextColor(Color.parseColor("#16A34A"))

            // 3. Munculkan gambar Tanda Tangan, sembunyikan teks "DITOLAK!"
            imgTtd?.visibility = View.VISIBLE
            tvTtdDitolak?.visibility = View.GONE
        }
    }
}