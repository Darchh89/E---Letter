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
import com.example.androideletter.model.DepartmentResponse
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.UpdateProfileRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class halaman_edit_profil_siswa : AppCompatActivity() {

    private var jenisKelaminDipilih = "male"
    private var selectedClassId: Int? = null

    // Menggunakan DepartmentResponse sesuai yang ada di ApiService dan model Anda
    private var daftarKelas = ArrayList<DepartmentResponse>()

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
        val spinnerJurusan = findViewById<Spinner>(R.id.spinner_keahlian)

        // Ambil Token Session
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // 1. Ambil Data Kelas dari API
        muatDataKelas(token, spinnerJurusan)

        // 2. Event Listener Spinner Kelas
        spinnerJurusan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                // Simpan ID dari kelas yang dipilih
                selectedClassId = daftarKelas[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 3. Toggle Jenis Kelamin (Tanpa ImageView)



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

            if (selectedClassId == null) {
                Toast.makeText(this, "Harap tunggu daftar kelas dimuat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLanjut.isEnabled = false
            btnLanjut.text = "Menyimpan..."

            // Masukkan data request termasuk ID kelas (class_id)
            val request = UpdateProfileRequest(nama, nis, jenisKelaminDipilih, selectedClassId)

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

    // Fungsi fetch data kelas menggunakan DepartmentResponse
    private fun muatDataKelas(token: String, spinner: Spinner) {
        RetrofitClient.instance.getClasses(token).enqueue(object : Callback<List<DepartmentResponse>> {
            override fun onResponse(call: Call<List<DepartmentResponse>>, response: Response<List<DepartmentResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    daftarKelas.clear()
                    daftarKelas.addAll(response.body()!!)

                    // Masukkan ke Spinner menggunakan Adapter
                    val adapter = ArrayAdapter(
                        this@halaman_edit_profil_siswa,
                        android.R.layout.simple_spinner_dropdown_item,
                        daftarKelas
                    )
                    spinner.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<DepartmentResponse>>, t: Throwable) {
                Toast.makeText(this@halaman_edit_profil_siswa, "Gagal memuat daftar kelas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}