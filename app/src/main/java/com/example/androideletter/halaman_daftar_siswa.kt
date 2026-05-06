package com.example.androideletter // Sesuaikan dengan package milikmu

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androideletter.model.AuthResponse
import com.example.androideletter.model.RegisterRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_daftar_siswa : AppCompatActivity() {

    private var isPassVisible = false
    private var isConfirmPassVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_daftar_siswa)

        val btnBack = findViewById<LinearLayout>(R.id.btn_back)
        val tvMasuk = findViewById<TextView>(R.id.tv_masuk)

        // Inisialisasi Input Text
        val etNama = findViewById<EditText>(R.id.et_nama)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)

        // Inisialisasi Ikon dan Tombol
        val ivTogglePass = findViewById<ImageView>(R.id.iv_toggle_password)
        val ivToggleConfirmPass = findViewById<ImageView>(R.id.iv_toggle_confirm_password)
        val btnDaftar = findViewById<MaterialButton>(R.id.btn_daftar)

        // 1. Logika Tombol Kembali
        btnBack.setOnClickListener {
            finish()
        }

        // 2. Fitur Mata: Kata Sandi
        ivTogglePass.setOnClickListener {
            isPassVisible = !isPassVisible
            if (isPassVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivTogglePass.setImageResource(R.drawable.mata_biru_on)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivTogglePass.setImageResource(R.drawable.mata_biru_off)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // 3. Fitur Mata: Konfirmasi Kata Sandi
        ivToggleConfirmPass.setOnClickListener {
            isConfirmPassVisible = !isConfirmPassVisible
            if (isConfirmPassVisible) {
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivToggleConfirmPass.setImageResource(R.drawable.mata_biru_on)
            } else {
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivToggleConfirmPass.setImageResource(R.drawable.mata_biru_off)
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }

        // 4. Kembali ke halaman Login
        tvMasuk.setOnClickListener {
            finish()
        }

        // 5. Logika Tombol Daftar (Mengirim data ke Node.js)
        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validasi: Pastikan tidak ada yang kosong
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi: Pastikan password cocok
            if (password != confirmPassword) {
                Toast.makeText(this, "Kata sandi tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat objek request sesuai model RegisterRequest (full_name, email, password)
            val request = RegisterRequest(
                full_name = nama,
                email = email,
                password = password,
                token = null // Token otomatis di-handle backend untuk siswa
            )

            // Efek loading pada tombol
            btnDaftar.isEnabled = false
            btnDaftar.text = "Mendaftar..."

            // Eksekusi menggunakan Retrofit Call (bukan Coroutine sesuai setup awal)
            RetrofitClient.instance.registerStudent(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    btnDaftar.isEnabled = true
                    btnDaftar.text = "Daftar"

                    if (response.isSuccessful) {
                        Toast.makeText(this@halaman_daftar_siswa, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@halaman_daftar_siswa, "Gagal: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    btnDaftar.isEnabled = true
                    btnDaftar.text = "Daftar"
                    Toast.makeText(this@halaman_daftar_siswa, "Kesalahan Jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}