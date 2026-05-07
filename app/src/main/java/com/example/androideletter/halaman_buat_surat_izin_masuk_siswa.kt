package com.example.androideletter

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.DepartmentResponse
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.SuratIzinMasukRequest
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class halaman_buat_surat_izin_masuk_siswa : AppCompatActivity() {

    private lateinit var spinnerKeahlian: Spinner
    private lateinit var etJudul: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etWaktuMulai: EditText
    private lateinit var etWaktuSelesai: EditText
    private lateinit var etKeterangan: EditText

    private val departmentList = mutableListOf<DepartmentResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_izin_masuk_siswa)

        // HILANGKAN NAVBAR BAWAAN HP
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // Inisialisasi View
        etJudul = findViewById(R.id.et_judul_keterangan)
        etTanggal = findViewById(R.id.et_tanggal)
        etWaktuMulai = findViewById(R.id.et_waktu_mulai)
        etWaktuSelesai = findViewById(R.id.et_waktu_selesai)
        etKeterangan = findViewById(R.id.et_keterangan)
        spinnerKeahlian = findViewById(R.id.spinner_keahlian)

        // Load Data Dropdown dari Server
        muatDataKonsentrasiKeahlian()

        // Logika DatePicker
        val calendar = Calendar.getInstance()
        etTanggal.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal YYYY-MM-DD agar mudah disimpan di database MySQL
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etTanggal.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // Format Jam
        formatJamOtomatis(etWaktuMulai)
        formatJamOtomatis(etWaktuSelesai)

        // Tombol Ajukan (Validasi Form)
        findViewById<MaterialButton>(R.id.btn_ajukan).setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val tanggal = etTanggal.text.toString().trim()
            val waktuMulai = etWaktuMulai.text.toString().trim()
            val waktuSelesai = etWaktuSelesai.text.toString().trim()
            val keterangan = etKeterangan.text.toString().trim()
            val selectedIndex = spinnerKeahlian.selectedItemPosition

            // Validasi Input Kosong
            if (judul.isEmpty() || tanggal.isEmpty() || waktuMulai.isEmpty() || waktuSelesai.isEmpty() || keterangan.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data form", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Dropdown (Index 0 adalah "-- Pilih --")
            if (selectedIndex == 0 || departmentList.isEmpty()) {
                Toast.makeText(this, "Harap pilih Konsentrasi Keahlian", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jika semua valid, tampilkan dialog konfirmasi
            tampilkanDialogKonfirmasi()
        }

        // Tombol Reset
        findViewById<MaterialButton>(R.id.btn_reset).setOnClickListener {
            etJudul.setText("")
            etTanggal.setText("")
            etWaktuMulai.setText("")
            etWaktuSelesai.setText("")
            etKeterangan.setText("")
            if (spinnerKeahlian.adapter != null) spinnerKeahlian.setSelection(0)
        }
    }

    // ==========================================
    // LOGIKA API: MENGIRIM DATA KE SERVER
    // ==========================================
    private fun kirimDataKeServer() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        val judul = etJudul.text.toString().trim()
        val tanggal = etTanggal.text.toString().trim()
        val waktuMulai = etWaktuMulai.text.toString().trim()
        val waktuSelesai = etWaktuSelesai.text.toString().trim()
        val keterangan = etKeterangan.text.toString().trim()

        // Ambil ID Jurusan (Index dikurangi 1 karena index 0 adalah "-- Pilih --")
        val selectedIndex = spinnerKeahlian.selectedItemPosition
        val idJurusanTerpilih = departmentList[selectedIndex - 1].id

        val request = SuratIzinMasukRequest(
            title = judul,
            department_id = idJurusanTerpilih,
            date = tanggal,
            start_time = waktuMulai,
            end_time = waktuSelesai,
            description = keterangan
        )

        RetrofitClient.instance.buatSuratIzinMasuk(token, request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.isSuccessful) {
                    tampilkanDialogBerhasil() // Munculkan dialog sukses
                } else {
                    Toast.makeText(this@halaman_buat_surat_izin_masuk_siswa, "Gagal membuat surat. Cek data Anda.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                Toast.makeText(this@halaman_buat_surat_izin_masuk_siswa, "Koneksi bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ==========================================
    // MENGISI DROPDOWN KONSENTRASI KEAHLIAN
    // ==========================================
    private fun muatDataKonsentrasiKeahlian() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        RetrofitClient.instance.getClasses(token).enqueue(object : Callback<List<DepartmentResponse>> {            override fun onResponse(call: Call<List<DepartmentResponse>>, response: Response<List<DepartmentResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    departmentList.clear()
                    departmentList.addAll(response.body()!!)

                    val namaKeahlianList = mutableListOf("-- Pilih --")
                    namaKeahlianList.addAll(departmentList.map { it.name })

                    val adapter = ArrayAdapter(
                        this@halaman_buat_surat_izin_masuk_siswa,
                        android.R.layout.simple_spinner_dropdown_item,
                        namaKeahlianList
                    )
                    spinnerKeahlian.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<DepartmentResponse>>, t: Throwable) {}
        })
    }

    // ==========================================
    // FUNGSI DIALOG
    // ==========================================
    private fun tampilkanDialogKonfirmasi() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_konfirmasi_pengajuan_masuk)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<MaterialButton>(R.id.btn_periksa_lagi).setOnClickListener {
            dialog.dismiss() // Tutup dialog dan kembali ke form
        }

        dialog.findViewById<MaterialButton>(R.id.btn_ya_buat).setOnClickListener {
            dialog.dismiss()
            kirimDataKeServer() // Panggil API setelah konfirmasi ditekan
        }
        dialog.show()
    }

    private fun tampilkanDialogBerhasil() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_surat_berhasil_masuk)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<MaterialButton>(R.id.btn_kembali_dialog).setOnClickListener {
            dialog.dismiss()
            finish() // Kembali ke halaman utama/sebelumnya
        }

        dialog.findViewById<MaterialButton>(R.id.btn_lihat_surat).setOnClickListener {
            dialog.dismiss()
            // Arahkan ke halaman riwayat surat
            val intent = Intent(this, halaman_riwayat_siswa::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

    // ==========================================
    // FUNGSI FORMAT JAM
    // ==========================================
    private fun formatJamOtomatis(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                var str = s.toString().replace("[^\\d]".toRegex(), "")

                if (str.length >= 2) {
                    var jam = str.substring(0, 2).toInt()
                    if (jam > 23) jam = 23
                    str = String.format("%02d", jam) + str.substring(2)
                }
                if (str.length >= 4) {
                    var menit = str.substring(2, 4).toInt()
                    if (menit > 59) menit = 59
                    str = str.substring(0, 2) + String.format("%02d", menit)
                }
                if (str.length > 4) str = str.substring(0, 4)
                if (str.length > 2) str = str.substring(0, 2) + ":" + str.substring(2)

                editText.setText(str)
                editText.setSelection(str.length)
                isUpdating = false
            }
        })
    }
}