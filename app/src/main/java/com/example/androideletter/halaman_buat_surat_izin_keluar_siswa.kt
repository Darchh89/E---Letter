package com.example.androideletter

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton

class halaman_buat_surat_izin_keluar_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_izin_keluar_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Tombol Back Header
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val btnAjukan = findViewById<MaterialButton>(R.id.btn_ajukan)

        // Memunculkan Pop Up Konfirmasi Pertama saat ditekan
        btnAjukan.setOnClickListener {
            tampilkanDialogKonfirmasi()
        }
    }

    private fun tampilkanDialogKonfirmasi() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Memanggil file XML pop up konfirmasi
        dialog.setContentView(R.layout.dialog_konfirmasi_pengajuan)
        // Membuat latar belakang pop up transparan agar sudut melengkungnya (radius) terlihat
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val btnPeriksaLagi = dialog.findViewById<MaterialButton>(R.id.btn_periksa_lagi)
        val btnYaBuat = dialog.findViewById<MaterialButton>(R.id.btn_ya_buat)

        btnPeriksaLagi.setOnClickListener {
            dialog.dismiss() // Tutup pop up
        }

        btnYaBuat.setOnClickListener {
            dialog.dismiss() // Tutup pop up pertama
            tampilkanDialogBerhasil() // Munculkan pop up kedua
        }

        dialog.show()
    }

    private fun tampilkanDialogBerhasil() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Memanggil file XML pop up berhasil
        dialog.setContentView(R.layout.dialog_surat_berhasil)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val btnKembaliDialog = dialog.findViewById<MaterialButton>(R.id.btn_kembali_dialog)
        val btnLihatSurat = dialog.findViewById<MaterialButton>(R.id.btn_lihat_surat)

        btnKembaliDialog.setOnClickListener {
            dialog.dismiss()
            finish() // Kembali ke halaman sebelumnya
        }

        btnLihatSurat.setOnClickListener {
            dialog.dismiss()
            // Mengarahkan ke halaman Lihat Surat (Sesuaikan nama class-nya)
            // startActivity(Intent(this, halaman_lihat_surat_siswa::class.java))
            // finish()
        }

        dialog.show()
    }
}