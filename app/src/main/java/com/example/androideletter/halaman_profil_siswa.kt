package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
        val ivFotoProfil = findViewById<ImageView>(R.id.iv_foto_profil) // DEKLARASI IMAGEVIEW
        val tvNama = findViewById<TextView>(R.id.tv_nama_profil)
        val tvNisn = findViewById<TextView>(R.id.tv_nisn_profil)
        val tvKelas = findViewById<TextView>(R.id.tv_kelas_value)
        val tvEmail = findViewById<TextView>(R.id.tv_email_value)

        // View tambahan untuk statistik surat
        val tvTotalMasuk = findViewById<TextView>(R.id.tv_total_izin_masuk)
        val tvTotalKeluar = findViewById<TextView>(R.id.tv_total_izin_keluar)

        // Panggil fungsi untuk mengambil data dari server (tambahkan parameter ivFotoProfil)

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
        ivFotoProfil: ImageView, // Parameter baru
        tvNama: TextView, tvNisn: TextView, tvKelas: TextView,
        tvGender: TextView, tvEmail: TextView,
        tvTotalMasuk: TextView, tvTotalKeluar: TextView
    ) {
        // 1. Ambil Token JWT yang tersimpan saat login
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // 2. Lakukan pemanggilan ke API (GET /api/student/profile)
        RetrofitClient.instance.getStudentProfile(token).enqueue(object : Callback<StudentProfileResponse> {
            override fun onResponse(call: Call<StudentProfileResponse>, response: Response<StudentProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()

                    // =========================================
                    // MENGISI DATA KE UI (Jika null/kosong = "-")
                    // =========================================

                    // Nama
                    tvNama.text = profile?.full_name?.takeIf { it.isNotEmpty() } ?: "-"

                    // NISN
                    val nisn = profile?.student_code?.takeIf { it.isNotEmpty() } ?: "-"
                    tvNisn.text = "NISN: $nisn"

                    // Email
                    tvEmail.text = profile?.email?.takeIf { it.isNotEmpty() } ?: "-"

                    // Kelas
                    tvKelas.text = profile?.class_name?.takeIf { it.isNotEmpty() } ?: "-"

                    // Jenis Kelamin & LOGIKA UBAH GAMBAR PROFIL
                    if (profile?.gender == "male") {
                        tvGender.text = "Laki-laki"
                        ivFotoProfil.setImageResource(R.drawable.foto_profil_pria) // Ganti gambar jadi pria
                    } else if (profile?.gender == "female") {
                        tvGender.text = "Perempuan"
                        ivFotoProfil.setImageResource(R.drawable.foto_profil_wanita) // Ganti gambar jadi wanita
                    } else {
                        tvGender.text = "-"
                        // Jika mau ada gambar default kalau kosong, masukkan di sini
                        // ivFotoProfil.setImageResource(R.drawable.logo_default)
                    }

                    // Total Surat
                    tvTotalMasuk.text = profile?.total_izin_masuk?.toString() ?: "0"
                    tvTotalKeluar.text = profile?.total_izin_keluar?.toString() ?: "0"

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