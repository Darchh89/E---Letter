package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.RegisterRequest
import com.example.androideletter.model.RegisterResponse
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_daftar_siswa : AppCompatActivity() {

    private lateinit var btnBack: LinearLayout
    private lateinit var tvMasuk: TextView
    private lateinit var btnDaftar: MaterialButton

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var ivTogglePassword: ImageView
    private lateinit var ivToggleConfirmPassword: ImageView

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_daftar_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

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

        ivTogglePassword = findViewById(R.id.iv_toggle_password)
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_password)
    }

    private fun setupListener() {
        btnBack.setOnClickListener { finish() }
        tvMasuk.setOnClickListener {
            startActivity(Intent(this, halaman_masuk_user::class.java))
            finish()
        }

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, ivTogglePassword, isPasswordVisible)
        }

        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(etConfirmPassword, ivToggleConfirmPassword, isConfirmPasswordVisible)
        }

        btnDaftar.setOnClickListener {
            prosesDaftar()
        }
    }

    private fun togglePasswordVisibility(editText: EditText, icon: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            icon.setImageResource(R.drawable.mata_biru_on)
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

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
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

        btnDaftar.isEnabled = false
        btnDaftar.text = "Memproses..."

        // Siswa menggunakan role "student" dan token kosong
        val request = RegisterRequest(fullName = nama, email = email, password = password, role = "student", token = "")

        RetrofitClient.instance.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                btnDaftar.isEnabled = true
                btnDaftar.text = "Daftar"

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@halaman_daftar_siswa, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@halaman_daftar_siswa, halaman_masuk_user::class.java))
                    finish()
                } else {
                    Toast.makeText(this@halaman_daftar_siswa, "Gagal Mendaftar (Email mungkin sudah terpakai)", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                btnDaftar.isEnabled = true
                btnDaftar.text = "Daftar"
                Toast.makeText(this@halaman_daftar_siswa, "Kesalahan Jaringan: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}