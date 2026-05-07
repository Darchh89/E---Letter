package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.UpdateProfileRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_edit_profil_siswa : AppCompatActivity() {

    // Simpan nilai sesuai enum database ('male', 'female')
    private var jenisKelaminDipilih = "male"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_edit_profil_siswa)

        // Hilangkan Navbar Bawaan HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val etNama = findViewById<EditText>(R.id.et_nama)
        val etNis = findViewById<EditText>(R.id.et_nis)

        // Dropdown Jurusan
        val spinnerJurusan = findViewById<Spinner>(R.id.spinner_keahlian)
        val daftarKeahlian = arrayOf("Rekayasa Perangkat Lunak", "Teknik Komputer Jaringan", "Multimedia")
        spinnerJurusan.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarKeahlian)

        // Toggle Jenis Kelamin
        val cardLaki = findViewById<MaterialCardView>(R.id.card_laki)
        val tvLaki = findViewById<TextView>(R.id.tv_laki)
        val cardPerempuan = findViewById<MaterialCardView>(R.id.card_perempuan)
        val tvPerempuan = findViewById<TextView>(R.id.tv_perempuan)

        cardLaki.setOnClickListener {
            jenisKelaminDipilih = "male"
            cardLaki.setCardBackgroundColor(Color.WHITE)
            cardLaki.cardElevation = 4f
            tvLaki.setTextColor(Color.parseColor("#3FA2F6"))

            cardPerempuan.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
            cardPerempuan.cardElevation = 0f
            tvPerempuan.setTextColor(Color.parseColor("#9E9E9E"))
        }

        cardPerempuan.setOnClickListener {
            jenisKelaminDipilih = "female"
            cardPerempuan.setCardBackgroundColor(Color.WHITE)
            cardPerempuan.cardElevation = 4f
            tvPerempuan.setTextColor(Color.parseColor("#3FA2F6"))

            cardLaki.setCardBackgroundColor(Color.parseColor("#F5F5F5"))
            cardLaki.cardElevation = 0f
            tvLaki.setTextColor(Color.parseColor("#9E9E9E"))
        }

        // ==========================================
        // TOMBOL LANJUT (SIMPAN PROFIL -> TTD)
        // ==========================================
        val btnLanjut = findViewById<MaterialButton>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nis = etNis.text.toString().trim()

            if (nama.isEmpty() || nis.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi Nama dan NIS", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLanjut.isEnabled = false
            btnLanjut.text = "Menyimpan..."

            // Ambil Token dari Session
            val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
            val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

            val request = UpdateProfileRequest(nama, nis, jenisKelaminDipilih)

            RetrofitClient.instance.updateStudentProfile(token, request).enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    btnLanjut.isEnabled = true
                    btnLanjut.text = "Lanjut ke Tanda Tangan →"

                    if (response.isSuccessful) {
                        Toast.makeText(this@halaman_edit_profil_siswa, "Data Profil Disimpan", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@halaman_edit_profil_siswa, halaman_tanda_tangan_siswa::class.java))
                    } else {
                        Toast.makeText(this@halaman_edit_profil_siswa, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    btnLanjut.isEnabled = true
                    btnLanjut.text = "Lanjut ke Tanda Tangan →"
                    Toast.makeText(this@halaman_edit_profil_siswa, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}