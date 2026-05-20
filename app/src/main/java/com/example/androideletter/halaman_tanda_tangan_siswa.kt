package com.example.androideletter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androideletter.model.SignatureRequest
import com.example.androideletter.model.SignatureResponse
import com.example.androideletter.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            signaturePad.clear()
            placeholderText.visibility = View.VISIBLE
        }

        findViewById<MaterialButton>(R.id.btn_kembali).setOnClickListener { finish() }

        // ==========================================
        // UPLOAD TANDA TANGAN FORMAT SVG
        // ==========================================
        btnSelesai.setOnClickListener {
            if (signaturePad.isDrawn) {
                // Ambil hasil gambar dalam bentuk teks SVG, bukan gambar (bitmap)
                val svgString = signaturePad.getSignatureSvg()
                uploadSignatureToServer(svgString)
            } else {
                Toast.makeText(this, "Tanda tangan masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadSignatureToServer(svgDataUrl: String) {
        val btnSelesai = findViewById<MaterialButton>(R.id.btn_selesai)
        btnSelesai.isEnabled = false
        btnSelesai.text = "Mengunggah..."

        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val token = "Bearer " + sharedPref.getString("USER_TOKEN", "")

        // Bungkus teks SVG ke dalam Request JSON sesuai model
        val requestBody = SignatureRequest(svgDataUrl)

        // Panggil API Backend
        RetrofitClient.instance.uploadSignature(token, requestBody).enqueue(object : Callback<SignatureResponse> {
            override fun onResponse(call: Call<SignatureResponse>, response: Response<SignatureResponse>) {
                btnSelesai.isEnabled = true
                btnSelesai.text = "Selesai & Masuk"

                if (response.isSuccessful) {
                    Toast.makeText(this@halaman_tanda_tangan_siswa, "Profil Selesai Diperbarui!", Toast.LENGTH_SHORT).show()

                    // Arahkan ke beranda dan hapus riwayat halaman (Back stack)
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