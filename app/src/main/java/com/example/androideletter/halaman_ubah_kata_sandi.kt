package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton

class halaman_ubah_kata_sandi : AppCompatActivity() {

    private var isPasswordVisible1 = false
    private var isPasswordVisible2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_ubah_kata_sandi)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // DEKLARASI ELEMEN FORM
        val btnBackLayout = findViewById<LinearLayout>(R.id.btn_back_layout)
        val etSandiBaru = findViewById<EditText>(R.id.et_sandi_baru)
        val etKonfirmasiSandi = findViewById<EditText>(R.id.et_konfirmasi_sandi)
        val ivToggleSandi1 = findViewById<ImageView>(R.id.iv_toggle_sandi1)
        val ivToggleSandi2 = findViewById<ImageView>(R.id.iv_toggle_sandi2)
        val btnUbah = findViewById<MaterialButton>(R.id.btn_ubah)

        // DEKLARASI ELEMEN POPUP
        val popupOverlay = findViewById<ConstraintLayout>(R.id.popup_overlay)
        val btnTutupPopup = findViewById<ImageView>(R.id.btn_tutup_popup)

        // LOGIKA KEMBALI
        btnBackLayout.setOnClickListener {
            finish()
        }

        // LOGIKA MATA SANDI BARU
        ivToggleSandi1.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1
            if (isPasswordVisible1) {
                etSandiBaru.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleSandi1.setImageResource(R.drawable.mata_oren_on)
            } else {
                etSandiBaru.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleSandi1.setImageResource(R.drawable.mata_oren_off)
            }
            etSandiBaru.setSelection(etSandiBaru.text.length)
        }

        // LOGIKA MATA KONFIRMASI SANDI
        ivToggleSandi2.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2
            if (isPasswordVisible2) {
                etKonfirmasiSandi.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleSandi2.setImageResource(R.drawable.mata_oren_on)
            } else {
                etKonfirmasiSandi.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleSandi2.setImageResource(R.drawable.mata_oren_off)
            }
            etKonfirmasiSandi.setSelection(etKonfirmasiSandi.text.length)
        }

        // LOGIKA TOMBOL UBAH
        btnUbah.setOnClickListener {
            val sandiBaru = etSandiBaru.text.toString()
            val konfirmasiSandi = etKonfirmasiSandi.text.toString()

            if (sandiBaru.isEmpty() || konfirmasiSandi.isEmpty()) {
                Toast.makeText(this, "Harap isi kedua kolom sandi!", Toast.LENGTH_SHORT).show()
            } else if (sandiBaru != konfirmasiSandi) {
                Toast.makeText(this, "Kata sandi tidak cocok!", Toast.LENGTH_SHORT).show()
            } else {
                // Berhasil ubah sandi -> Munculkan Popup
                popupOverlay.visibility = View.VISIBLE
            }
        }

        // LOGIKA TOMBOL SILANG (X) PADA POPUP
        btnTutupPopup.setOnClickListener {
            // Sembunyikan popup (opsional, karena akan pindah halaman)
            popupOverlay.visibility = View.GONE

            // Pindah ke Beranda Siswa
            val intent = Intent(this, halaman_beranda_siswa::class.java)
            // Hapus riwayat halaman agar tidak bisa kembali ke halaman ubah sandi ini
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}