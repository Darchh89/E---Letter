package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton

class halaman_verifikasi_otp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_verifikasi_otp)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val btnBackLayout = findViewById<LinearLayout>(R.id.btn_back_layout)
        val btnKonfirmasi = findViewById<MaterialButton>(R.id.btn_konfirmasi)
        val tvKirimUlang = findViewById<TextView>(R.id.tv_kirim_ulang)

        val otp1 = findViewById<EditText>(R.id.otp_1)
        val otp2 = findViewById<EditText>(R.id.otp_2)
        val otp3 = findViewById<EditText>(R.id.otp_3)
        val otp4 = findViewById<EditText>(R.id.otp_4)
        val otp5 = findViewById<EditText>(R.id.otp_5)
        val otp6 = findViewById<EditText>(R.id.otp_6)

        setupOtpInput(otp1, next = otp2, prev = null)
        setupOtpInput(otp2, next = otp3, prev = otp1)
        setupOtpInput(otp3, next = otp4, prev = otp2)
        setupOtpInput(otp4, next = otp5, prev = otp3)
        setupOtpInput(otp5, next = otp6, prev = otp4)
        setupOtpInput(otp6, next = null, prev = otp5)

        btnBackLayout.setOnClickListener {
            finish()
        }

        // LOGIKA PINDAH HALAMAN SAAT KONFIRMASI DITEKAN
        btnKonfirmasi.setOnClickListener {
            val kodeOTP = "${otp1.text}${otp2.text}${otp3.text}${otp4.text}${otp5.text}${otp6.text}"

            if (kodeOTP.length == 6) {
                Toast.makeText(this, "OTP Berhasil Diverifikasi!", Toast.LENGTH_SHORT).show()

                // Berpindah ke halaman ubah kata sandi
                val intent = Intent(this, halaman_ubah_kata_sandi::class.java)
                startActivity(intent)
                finish() // Menutup halaman OTP agar tidak bisa kembali dengan tombol back
            } else {
                Toast.makeText(this, "Silakan lengkapi 6 digit kode OTP!", Toast.LENGTH_SHORT).show()
            }
        }

        tvKirimUlang.setOnClickListener {
            Toast.makeText(this, "Kode OTP baru telah dikirim ke email Anda.", Toast.LENGTH_SHORT).show()
            otp1.text.clear()
            otp2.text.clear()
            otp3.text.clear()
            otp4.text.clear()
            otp5.text.clear()
            otp6.text.clear()
            otp1.requestFocus()
        }
    }

    private fun setupOtpInput(current: EditText, next: EditText?, prev: EditText?) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    next?.requestFocus()
                }
            }
        })

        current.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (current.text.isEmpty()) {
                    prev?.requestFocus()
                    return@setOnKeyListener true
                }
            }
            false
        }
    }
}