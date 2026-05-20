package com.example.androideletter

import android.content.Context
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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.LoginRequest
import com.example.androideletter.model.LoginResponse
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_masuk_user : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_masuk_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val etId = findViewById<EditText>(R.id.et_id) // Menggunakan ini untuk input Email/ID
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnMasuk = findViewById<MaterialButton>(R.id.btn_masuk)
        val tvDaftar = findViewById<TextView>(R.id.tv_daftar)
        val tvLupaSandi = findViewById<TextView>(R.id.tv_lupa_sandi)
        val ivTogglePassword = findViewById<ImageView>(R.id.iv_toggle_password)
        val btnBack = findViewById<LinearLayout>(R.id.btn_back)

        // LOGIKA TOMBOL KEMBALI
        btnBack.setOnClickListener {
            val intent = Intent(this, LanjutkanSebagai::class.java)
            startActivity(intent)
            finish()
        }

        // LOGIKA LUPA KATA SANDI
        tvLupaSandi.setOnClickListener {
            val intent = Intent(this, halaman_lupa_kata_sandi::class.java)
            startActivity(intent)
        }

        // LOGIKA INTIP KATA SANDI
        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.mata_oren_on)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.mata_oren_off)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // =======================================================
        // LOGIKA LOGIN MENGGUNAKAN API RETROFIT
        // =======================================================
        btnMasuk.setOnClickListener {
            val inputId = etId.text.toString().trim()
            val inputPassword = etPassword.text.toString().trim()

            if (inputId.isEmpty()) {
                etId.error = "ID/Email tidak boleh kosong"
                return@setOnClickListener
            }
            if (inputPassword.isEmpty()) {
                etPassword.error = "Kata sandi tidak boleh kosong"
                return@setOnClickListener
            }

            // Ubah tampilan tombol saat proses API
            btnMasuk.isEnabled = false
            btnMasuk.text = "Memproses..."

            // Buat request body untuk API Retrofit
            val request = LoginRequest(inputId, inputPassword)

            RetrofitClient.instance.loginUser(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    // KEMBALIKAN STATE TOMBOL
                    btnMasuk.isEnabled = true
                    btnMasuk.text = "MASUK"

                    val responseBody: LoginResponse? = response.body()

                    if (response.isSuccessful && responseBody != null && responseBody.success) {

                        // 1. Ambil data secara bertahap dari response
                        val data = responseBody.data
                        val user = data?.user

                        val roleDariBackend: String? = user?.role
                        val namaLengkap: String? = user?.fullName
                        val jwtToken: String? = data?.accessToken

                        // 2. Simpan JWT Token dan Data User ke SharedPreferences (Gunakan penulisan camelCase yang presisi)
                        if (jwtToken != null && roleDariBackend != null && namaLengkap != null) {
                            saveUserSession(jwtToken, roleDariBackend, namaLengkap)
                        }

                        Toast.makeText(this@halaman_masuk_user, "Login Berhasil: $namaLengkap", Toast.LENGTH_SHORT).show()

                        // 3. Pindah halaman spesifik berdasarkan role dari backend
                        val intentBeranda = when (roleDariBackend) {
                            "student" -> Intent(this@halaman_masuk_user, halaman_beranda_siswa::class.java)
                            "teacher" -> Intent(this@halaman_masuk_user, halaman_beranda_guru::class.java)
                            "admin" -> Intent(this@halaman_masuk_user, halaman_beranda_admin::class.java)
                            "kepala_sekolah" -> Intent(this@halaman_masuk_user, halaman_beranda_kepsek::class.java)
                            else -> Intent(this@halaman_masuk_user, halaman_beranda_siswa::class.java)
                        }

                        startActivity(intentBeranda)
                        finish()
                    } else {
                        Toast.makeText(this@halaman_masuk_user, "Login Gagal: ID atau Sandi Salah", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // KEMBALIKAN STATE TOMBOL JIKA KONEKSI GAGAL
                    btnMasuk.isEnabled = true
                    btnMasuk.text = "MASUK"

                    Toast.makeText(this@halaman_masuk_user, "Gagal koneksi ke server: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        tvDaftar.setOnClickListener {
            // Ambil data kiriman dari halaman sebelumnya
            val roleAkses = intent.getStringExtra("ROLE_SEBELUMNYA")

            if (roleAkses == "guru") {
                val intent = Intent(this, halaman_daftar_guru::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, halaman_daftar_siswa::class.java)
                startActivity(intent)
            }
        }
    }

    // Fungsi untuk menyimpan Token dan Data User ke SharedPreferences
    private fun saveUserSession(token: String?, role: String?, name: String?) {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("USER_TOKEN", token)
        editor.putString("USER_ROLE", role)
        editor.putString("USER_NAME", name)
        editor.apply()
    }
}