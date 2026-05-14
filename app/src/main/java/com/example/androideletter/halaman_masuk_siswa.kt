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

class halaman_masuk_siswa : AppCompatActivity() {

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
        // LOGIKA LOGIN MENGGUNAKAN API RETROFIT & HARDCODE BYPASS
        // =======================================================
        btnMasuk.setOnClickListener {
            val email = etId.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi Input Kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi Email/ID dan Kata Sandi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- TAMBAHAN LOGIKA JALAN PINTAS (HARDCODE) ---
            if (email == "123" && password == "12345") {
                Toast.makeText(this, "Login Siswa Berhasil!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, halaman_beranda_siswa::class.java))
                finish()
                return@setOnClickListener // Hentikan proses agar tidak memanggil API
            } else if (email == "G123" && password == "12345") {
                Toast.makeText(this, "Login Guru Berhasil!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, halaman_beranda_guru::class.java))
                finish()
                return@setOnClickListener // Hentikan proses agar tidak memanggil API
            } else if (email == "admin123@gmail.com" && password == "12345") { // <--- BAGIAN INI DIUBAH
                Toast.makeText(this, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, halaman_beranda_admin::class.java))
                finish()
                return@setOnClickListener // Hentikan proses agar tidak memanggil API
            }
            // -----------------------------------------------

            // Ubah tampilan tombol saat proses API
            btnMasuk.isEnabled = false
            btnMasuk.text = "Memproses..."

            // Buat request body untuk API Retrofit
            val request = LoginRequest(email, password)

            RetrofitClient.instance.loginUser(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    // Kembalikan status tombol
                    btnMasuk.isEnabled = true
                    btnMasuk.text = "Masuk"

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val userRole = loginResponse?.user?.role
                        val userName = loginResponse?.user?.full_name

                        Toast.makeText(this@halaman_masuk_siswa, "Selamat datang, $userName", Toast.LENGTH_SHORT).show()

                        // Simpan sesi user
                        saveUserSession(loginResponse?.token, userRole, userName)

                        // Navigasi halaman berdasarkan Role dari server
                        when (userRole) {
                            "student" -> {
                                startActivity(Intent(this@halaman_masuk_siswa, halaman_beranda_guru::class.java))
                                finish()
                            }
                            "teacher" -> {
                                startActivity(Intent(this@halaman_masuk_siswa, halaman_beranda_guru::class.java))
                                finish()
                            }
                            "admin" -> {
                                startActivity(Intent(this@halaman_masuk_siswa, halaman_beranda_admin::class.java))
                                finish()
                            }
                            "kepala_sekolah" -> {
                                startActivity(Intent(this@halaman_masuk_siswa, halaman_beranda_kepsek::class.java))
                                finish()
                            }
                            else -> {
                                Toast.makeText(this@halaman_masuk_siswa, "Role tidak dikenali sistem", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Jika server mengembalikan error 401 atau lainnya
                        Toast.makeText(this@halaman_masuk_siswa, "Email atau Kata Sandi salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnMasuk.isEnabled = true
                    btnMasuk.text = "Masuk"
                    Toast.makeText(this@halaman_masuk_siswa, "Gagal koneksi ke server: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        tvDaftar.setOnClickListener {
            val intent = Intent(this, halaman_daftar_siswa::class.java)
            startActivity(intent)
        }
    }

    // Fungsi kecil untuk menyimpan Token dan Data User (SharedPreferences)
    private fun saveUserSession(token: String?, role: String?, name: String?) {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("USER_TOKEN", token)
        editor.putString("USER_ROLE", role)
        editor.putString("USER_NAME", name)
        editor.apply()
    }
}