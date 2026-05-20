package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.ClassResponse // <-- PERBAIKAN: Menggunakan ClassResponse
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.MasterDataResponse
import com.example.androideletter.model.UpdateProfileRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_edit_profil_siswa : AppCompatActivity() {

    private var selectedClassId: Int? = null
    private var daftarKelas = ArrayList<ClassResponse>() // <-- PERBAIKAN: Menggunakan ClassResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_edit_profil_siswa)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // DEKLARASI INPUT FORM
        val etNama = findViewById<EditText>(R.id.et_nama)
        val etNis = findViewById<EditText>(R.id.et_nis)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etTelepon = findViewById<EditText>(R.id.et_telepon)
        val spinnerJurusan = findViewById<Spinner>(R.id.spinner_keahlian)

        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // 1. Ambil Data Kelas
        muatDataKelas(token, spinnerJurusan)

        spinnerJurusan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedClassId = daftarKelas[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ==========================================
        // TOMBOL SIMPAN & LANJUT TTD
        // ==========================================
        val btnLanjut = findViewById<MaterialButton>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nis = etNis.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telepon = etTelepon.text.toString().trim()

            if (nama.isEmpty() || nis.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedClassId == null) {
                Toast.makeText(this, "Harap tunggu daftar kelas dimuat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLanjut.isEnabled = false
            btnLanjut.text = "Menyimpan..."

            // SINKRONISASI: Format Request yang dikirim sudah cocok dengan Backend
            val request = UpdateProfileRequest(nama, nis, email, telepon, selectedClassId)

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

    private fun muatDataKelas(token: String, spinner: Spinner) {
        RetrofitClient.instance.getClasses(token).enqueue(object : Callback<MasterDataResponse> {
            override fun onResponse(call: Call<MasterDataResponse>, response: Response<MasterDataResponse>) {
                val responseBody = response.body()

                // Pastikan HTTP berhasil, status 'success' true, dan 'data' tidak null
                if (response.isSuccessful && responseBody != null && responseBody.success) {

                    val listKelas = responseBody.data

                    if (listKelas != null && listKelas.isNotEmpty()) {
                        daftarKelas.clear()
                        daftarKelas.addAll(listKelas)

                        val adapter = ArrayAdapter(
                            this@halaman_edit_profil_siswa,
                            android.R.layout.simple_spinner_dropdown_item,
                            daftarKelas
                        )
                        spinner.adapter = adapter
                    } else {
                        Toast.makeText(this@halaman_edit_profil_siswa, "Data kelas kosong di database", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@halaman_edit_profil_siswa, "Gagal memuat kelas", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<MasterDataResponse>, t: Throwable) {
                // Tampilkan log atau Toast jika perlu
                Toast.makeText(this@halaman_edit_profil_siswa, "Gagal memuat kelas: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}