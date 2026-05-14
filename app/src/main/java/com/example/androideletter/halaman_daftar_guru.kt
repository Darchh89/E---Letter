package com.example.androideletter

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
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.RegisterRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_daftar_guru : AppCompatActivity() {

    // Deklarasi View
    private lateinit var btnBack: LinearLayout
    private lateinit var tvMasuk: TextView
    private lateinit var btnDaftar: MaterialButton

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etToken: EditText

    private lateinit var ivTogglePassword: ImageView
    private lateinit var ivToggleConfirmPassword: ImageView

    // State visibilitas password
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_daftar_guru)

        inisialisasiView()
        setupListener()
    }

    private fun inisialisasiView() {
        btnBack = findViewById(R.id.btn_back)
        tvMasuk = findViewById(R.id.tv_masuk)
        btnDaftar = findViewById(R.id.btn_daftar)

        etNama = findViewById(R.id.et_nama)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        etToken = findViewById(R.id.et_token)

        ivTogglePassword = findViewById(R.id.iv_toggle_password)
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_password)
    }

    private fun setupListener() {
        // Navigasi
        btnBack.setOnClickListener { finish() }
        tvMasuk.setOnClickListener {
            startActivity(Intent(this, halaman_masuk_user::class.java))
            finish()
        }

        // Toggle Password
        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, ivTogglePassword, isPasswordVisible)
        }

        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(etConfirmPassword, ivToggleConfirmPassword, isConfirmPasswordVisible)
        }

        // Tombol Daftar
        btnDaftar.setOnClickListener {
            prosesDaftar()
        }
    }

    private fun togglePasswordVisibility(editText: EditText, icon: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            icon.setImageResource(R.drawable.mata_biru_on) // Sesuaikan nama file icon mata terbuka Anda
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            icon.setImageResource(R.drawable.mata_biru_off)
        }
        editText.setSelection(editText.text.length)
    }

    private fun prosesDaftar() {
        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()
        val token = etToken.text.toString().trim()

        // Validasi form
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || token.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Kata sandi tidak cocok!", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Sandi minimal 6 karakter!", Toast.LENGTH_SHORT).show()
            return
        }

        // Update UI Loading
        btnDaftar.isEnabled = false
        btnDaftar.text = "Memproses..."

        // Eksekusi API
        val request = RegisterRequest(full_name = nama, email = email, password = password, token = token)

        RetrofitClient.instance.registerUser(request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                btnDaftar.isEnabled = true
                btnDaftar.text = "Daftar"

                if (response.isSuccessful) {
                    Toast.makeText(this@halaman_daftar_guru, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@halaman_daftar_guru, halaman_masuk_user::class.java))
                    finish()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gagal mendaftar"
                    Toast.makeText(this@halaman_daftar_guru, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                btnDaftar.isEnabled = true
                btnDaftar.text = "Daftar"
                Toast.makeText(this@halaman_daftar_guru, "Kesalahan Jaringan: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}