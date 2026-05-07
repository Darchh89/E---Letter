package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.SignatureResponse
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class halaman_tanda_tangan_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_tanda_tangan_siswa)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back_header).setOnClickListener { finish() }

        val signaturePad = findViewById<SignatureView>(R.id.signature_pad)
        val btnHapusTtd = findViewById<LinearLayout>(R.id.btn_hapus_ttd)
        val btnSelesai = findViewById<MaterialButton>(R.id.btn_selesai)
        val placeholderText = findViewById<TextView>(R.id.placeholder_text)

        signaturePad.setOnTouchListener { _, _ ->
            placeholderText.visibility = View.GONE
            false
        }

        btnHapusTtd.setOnClickListener {
            signaturePad.clear() // Asumsi fungsi clear() ada di class Anda
            placeholderText.visibility = View.VISIBLE
        }

        // Kembali ke Edit Profil
        findViewById<MaterialButton>(R.id.btn_kembali).setOnClickListener { finish() }

        // ==========================================
        // UPLOAD TANDA TANGAN & SELESAI
        // ==========================================
        btnSelesai.setOnClickListener {
            // Asumsi: isDrawn adalah boolean atau logic di SignatureView Anda untuk cek apakah kanvas diisi
            // Jika tidak ada fungsi ini, Anda bisa cek apakah bitmap kosong/null.
// BENAR (Memanggil fungsi)
            val signatureBitmap = signaturePad.getSignatureBitmap()

            if (signatureBitmap != null) {
                uploadSignatureToServer(signatureBitmap)
            } else {
                Toast.makeText(this, "Tanda tangan masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadSignatureToServer(bitmap: Bitmap) {
        val btnSelesai = findViewById<MaterialButton>(R.id.btn_selesai)
        btnSelesai.isEnabled = false
        btnSelesai.text = "Mengunggah..."

        // 1. Simpan bitmap ke Cache secara sementara
        val file = File(cacheDir, "signature_temp.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // 2. Siapkan file untuk dikirim via Retrofit
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("signature", file.name, requestFile)

        // 3. Ambil Token JWT
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // 4. Panggil API
        RetrofitClient.instance.uploadSignature(token, body).enqueue(object : Callback<SignatureResponse> {
            override fun onResponse(call: Call<SignatureResponse>, response: Response<SignatureResponse>) {
                btnSelesai.isEnabled = true
                btnSelesai.text = "Selesai & Masuk"

                if (response.isSuccessful) {
                    Toast.makeText(this@halaman_tanda_tangan_siswa, "Profil Selesai Diperbarui!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@halaman_tanda_tangan_siswa, halaman_beranda_siswa::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this@halaman_tanda_tangan_siswa, "Gagal Upload Tanda Tangan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignatureResponse>, t: Throwable) {
                btnSelesai.isEnabled = true
                btnSelesai.text = "Selesai & Masuk"
                Toast.makeText(this@halaman_tanda_tangan_siswa, "Error Jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}