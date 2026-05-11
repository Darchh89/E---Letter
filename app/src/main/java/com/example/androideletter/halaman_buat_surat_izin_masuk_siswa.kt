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
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.CreateIzinKeluarRequest
import com.example.androideletter.model.GeneralResponse
import com.example.androideletter.model.SearchStudentResponse
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class halaman_buat_surat_izin_masuk_siswa : AppCompatActivity() {

    private lateinit var etCariSiswa: AutoCompleteTextView
    private lateinit var etKelasSiswa: EditText
    private lateinit var btnTambahSiswa: LinearLayout
    private lateinit var llDaftarSiswa: LinearLayout
    private lateinit var llEmptyState: LinearLayout
    private lateinit var tvJumlahSiswa: TextView

    private lateinit var etTanggal: EditText
    private lateinit var etWaktuMulai: EditText
    private lateinit var etWaktuSelesai: EditText
    private lateinit var etKeterangan: EditText

    private var token: String = ""
    private var selectedStudentId: Int? = null
    private var selectedStudentClass: String = ""
    private val listStudentIds = mutableListOf<Int>()
    private val mapSiswa = HashMap<String, SearchStudentResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_buat_surat_izin_masuk_siswa)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        initViews()
        setupSearchAutocomplete()
        setupTambahSiswaLogic()
        setupDateTimePickers()

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // Tombol Ajukan -> Muncul Dialog
        findViewById<MaterialButton>(R.id.btn_ajukan).setOnClickListener {
            if (validasiForm()) {
                tampilkanDialogKonfirmasi()
            }
        }

        // Tombol Reset
        findViewById<MaterialButton>(R.id.btn_reset).setOnClickListener {
            resetForm()
        }
    }

    private fun initViews() {
        etCariSiswa = findViewById(R.id.et_cari_siswa)
        etKelasSiswa = findViewById(R.id.et_kelas_siswa)
        btnTambahSiswa = findViewById(R.id.btn_tambah_siswa)
        llDaftarSiswa = findViewById(R.id.ll_daftar_siswa)
        llEmptyState = findViewById(R.id.ll_empty_state)
        tvJumlahSiswa = findViewById(R.id.tv_jumlah_siswa)

        etTanggal = findViewById(R.id.et_tanggal)
        etWaktuMulai = findViewById(R.id.et_waktu_mulai)
        etWaktuSelesai = findViewById(R.id.et_waktu_selesai)
        etKeterangan = findViewById(R.id.et_keterangan)
    }

    // ==========================================
    // AUTOCOMPLETE & LOGIKA TAMBAH SISWA
    // ==========================================
    private fun setupSearchAutocomplete() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        etCariSiswa.setAdapter(adapter)

        etCariSiswa.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.length >= 2 && !mapSiswa.containsKey(query) && selectedStudentId == null) {
                    RetrofitClient.instance.searchStudent(token, query).enqueue(object : Callback<List<SearchStudentResponse>> {
                        override fun onResponse(call: Call<List<SearchStudentResponse>>, response: Response<List<SearchStudentResponse>>) {
                            if (response.isSuccessful) {
                                val results = response.body() ?: emptyList()
                                adapter.clear()
                                mapSiswa.clear()

                                val displayList = mutableListOf<String>()
                                for (item in results) {
                                    val displayName = "${item.full_name} - ${item.class_name ?: "Belum ada kelas"}"
                                    mapSiswa[displayName] = item
                                    displayList.add(displayName)
                                }
                                adapter.addAll(displayList)
                                adapter.notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<List<SearchStudentResponse>>, t: Throwable) {}
                    })
                } else if (query.isEmpty()) {
                    etKelasSiswa.text.clear()
                    selectedStudentId = null
                    selectedStudentClass = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etCariSiswa.setOnItemClickListener { parent, _, position, _ ->
            val selectedString = parent.getItemAtPosition(position) as String
            val selectedData = mapSiswa[selectedString]
            if (selectedData != null) {
                selectedStudentId = selectedData.student_id ?: selectedData.id ?: 0
                selectedStudentClass = selectedData.class_name ?: "Belum ada kelas"
                etCariSiswa.setText(selectedData.full_name, false)
                etKelasSiswa.setText(selectedStudentClass)
            }
        }
    }

    private fun setupTambahSiswaLogic() {
        btnTambahSiswa.setOnClickListener {
            val namaSiswa = etCariSiswa.text.toString()
            val kelasSiswa = etKelasSiswa.text.toString()
            val sId = selectedStudentId

            if (sId != null && sId != 0 && namaSiswa.isNotEmpty()) {
                if (listStudentIds.contains(sId)) {
                    Toast.makeText(this, "Siswa sudah ditambahkan", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val viewItem = layoutInflater.inflate(R.layout.item_siswa_terpilih, llDaftarSiswa, false)
                viewItem.findViewById<TextView>(R.id.tv_item_nama).text = namaSiswa
                viewItem.findViewById<TextView>(R.id.tv_item_kelas).text = kelasSiswa

                // Ubah tint ikon tempat sampah agar sesuai tema ungu
                val imgDelete = viewItem.findViewById<ImageView>(R.id.btn_hapus_item)
                imgDelete.setColorFilter(Color.parseColor("#AF72E3"))

                imgDelete.setOnClickListener {
                    llDaftarSiswa.removeView(viewItem)
                    listStudentIds.remove(sId)
                    updateJumlahSiswaUI()
                }

                llDaftarSiswa.addView(viewItem)
                listStudentIds.add(sId)
                updateJumlahSiswaUI()

                selectedStudentId = null
                selectedStudentClass = ""
                etCariSiswa.setText("", false)
                etKelasSiswa.text.clear()
            } else {
                Toast.makeText(this, "Pilih siswa dari dropdown terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateJumlahSiswaUI() {
        val jumlah = listStudentIds.size
        tvJumlahSiswa.text = "$jumlah Siswa terdaftar"
        if (jumlah == 0) {
            llDaftarSiswa.visibility = View.GONE
            llEmptyState.visibility = View.VISIBLE
        } else {
            llDaftarSiswa.visibility = View.VISIBLE
            llEmptyState.visibility = View.GONE
        }
    }

    // ==========================================
    // VALIDASI, FORMAT WAKTU & DIALOG
    // ==========================================
    private fun setupDateTimePickers() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            etTanggal.setText(sdf.format(calendar.time))
        }

        etTanggal.setOnClickListener {
            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        formatJamOtomatis(etWaktuMulai)
        formatJamOtomatis(etWaktuSelesai)
    }

    private fun formatJamOtomatis(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating || s.isNullOrEmpty()) return
                isUpdating = true

                var cleanString = s.toString().replace("[^\\d]".toRegex(), "")
                if (cleanString.length >= 2) {
                    var hours = cleanString.substring(0, 2).toIntOrNull() ?: 0
                    if (hours > 23) cleanString = "23" + if (cleanString.length > 2) cleanString.substring(2) else ""
                }
                if (cleanString.length >= 4) {
                    var minutes = cleanString.substring(2, 4).toIntOrNull() ?: 0
                    if (minutes > 59) cleanString = cleanString.substring(0, 2) + "59"
                }

                val formatted = java.lang.StringBuilder()
                for (i in cleanString.indices) {
                    if (i == 2) formatted.append(":")
                    if (i < 4) formatted.append(cleanString[i])
                }

                editText.setText(formatted.toString())
                editText.setSelection(formatted.length)
                isUpdating = false
            }
        })
    }

    private fun validasiForm(): Boolean {
        if (listStudentIds.isEmpty()) {
            Toast.makeText(this, "Tambahkan minimal 1 siswa!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etTanggal.text.isEmpty() || etWaktuMulai.text.isEmpty() || etWaktuSelesai.text.isEmpty() || etKeterangan.text.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua form", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun resetForm() {
        etCariSiswa.setText("", false)
        etKelasSiswa.text.clear()
        etTanggal.text.clear()
        etWaktuMulai.text.clear()
        etWaktuSelesai.text.clear()
        etKeterangan.text.clear()
        llDaftarSiswa.removeAllViews()
        listStudentIds.clear()
        updateJumlahSiswaUI()
    }

    // ==========================================
    // API CALL & DIALOG SUCESS
    // ==========================================
    private fun tampilkanDialogKonfirmasi() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_konfirmasi_pengajuan_masuk)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<MaterialButton>(R.id.btn_periksa_lagi).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<MaterialButton>(R.id.btn_ya_buat).setOnClickListener {
            dialog.dismiss()
            kirimDataKeServer()
        }
        dialog.show()
    }

    private fun kirimDataKeServer() {
        val request = CreateIzinKeluarRequest(
            date = etTanggal.text.toString(),
            start_time = etWaktuMulai.text.toString(),
            end_time = etWaktuSelesai.text.toString(),
            reason = etKeterangan.text.toString(),
            student_ids = listStudentIds
        )

        RetrofitClient.instance.buatSuratIzinMasukMulti(token, request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.isSuccessful) {
                    tampilkanDialogBerhasil()
                } else {
                    Toast.makeText(this@halaman_buat_surat_izin_masuk_siswa, "Gagal membuat surat.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                Toast.makeText(this@halaman_buat_surat_izin_masuk_siswa, "Koneksi bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanDialogBerhasil() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_surat_berhasil_masuk)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<MaterialButton>(R.id.btn_kembali_dialog).setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.findViewById<MaterialButton>(R.id.btn_lihat_surat).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, halaman_riwayat_siswa::class.java))
            finish()
        }
        dialog.show()
    }
}