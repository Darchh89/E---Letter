package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.StudentProfileResponse
import com.example.androideletter.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_profil_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_profil_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ==========================================
        // DEKLARASI VIEW UNTUK DATA
        // ==========================================
        val tvNama = findViewById<TextView>(R.id.tv_nama_profil)
        val tvNisn = findViewById<TextView>(R.id.tv_nisn_profil)
        val tvKelas = findViewById<TextView>(R.id.tv_kelas_value)
        val tvGender = findViewById<TextView>(R.id.tv_gender_value)
        val tvEmail = findViewById<TextView>(R.id.tv_email_value)

        // Panggil fungsi untuk mengambil data dari server
        muatDataProfilSiswa(tvNama, tvNisn, tvKelas, tvGender, tvEmail)

        // ==========================================
        // DEKLARASI MENU AKUN
        // ==========================================
        val btnEditProfil = findViewById<LinearLayout>(R.id.btn_edit_profil)

        btnEditProfil.setOnClickListener {
            val intent = Intent(this, halaman_edit_profil_siswa::class.java)
            startActivity(intent)
        }

        // ==========================================
        // DEKLARASI NAVIGASI BAWAH
        // ==========================================
        findViewById<LinearLayout>(R.id.nav_beranda).setOnClickListener {
            startActivity(Intent(this, halaman_beranda_siswa::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_panduan).setOnClickListener {
            startActivity(Intent(this, halaman_panduan_siswa::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_riwayat).setOnClickListener {
            startActivity(Intent(this, halaman_riwayat_siswa::class.java))
            finish()
        }
    }

    // ==========================================
    // FUNGSI UNTUK MENGAMBIL DATA PROFIL
    // ==========================================
    private fun muatDataProfilSiswa(
        tvNama: TextView, tvNisn: TextView, tvKelas: TextView, tvGender: TextView, tvEmail: TextView
    ) {
        // 1. Ambil Token JWT yang tersimpan saat login
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // 2. Lakukan pemanggilan ke API (GET /api/student/profile)
        RetrofitClient.instance.getStudentProfile(token).enqueue(object : Callback<StudentProfileResponse> {
            override fun onResponse(call: Call<StudentProfileResponse>, response: Response<StudentProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()

                    // Mengecek dan memasukkan data. Jika kosong/null, ganti jadi "-"

                    // Nama
                    tvNama.text = if (!profile?.full_name.isNullOrEmpty()) profile?.full_name else "-"

                    // NISN (Mengambil dari student_code)
                    val nisn = if (!profile?.student_code.isNullOrEmpty()) profile?.student_code else "-"
                    tvNisn.text = "NISN: $nisn"

                    // Email
                    tvEmail.text = if (!profile?.email.isNullOrEmpty()) profile?.email else "-"

                    // Jenis Kelamin (Konversi dari male/female ke Bahasa Indonesia)
                    tvGender.text = when (profile?.gender) {
                        "male" -> "Laki-laki"
                        "female" -> "Perempuan"
                        else -> "-"
                    }

                    // Kelas (Di database belum ada tabel/kolom khusus kelas, jadi sementara isi "-")
                    tvKelas.text = "-"

                } else {
                    Toast.makeText(this@halaman_profil_siswa, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentProfileResponse>, t: Throwable) {
                Toast.makeText(this@halaman_profil_siswa, "Koneksi bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}