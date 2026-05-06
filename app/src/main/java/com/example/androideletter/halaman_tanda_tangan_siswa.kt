package com.example.androideletter

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

class halaman_tanda_tangan_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_tanda_tangan_siswa)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // ==========================================
        // LOGIKA KANVAS TANDA TANGAN
        // ==========================================
        val signaturePad = findViewById<SignatureView>(R.id.signature_pad)
        val btnHapus = findViewById<LinearLayout>(R.id.btn_hapus_ttd)
        val btnSimpan = findViewById<LinearLayout>(R.id.btn_simpan_ttd)
        val placeholderText = findViewById<TextView>(R.id.placeholder_text)

        // Menghilangkan tulisan placeholder saat kanvas disentuh
        signaturePad.setOnTouchListener { _, _ ->
            placeholderText.visibility = View.GONE
            false
        }

        // Hapus coretan
        btnHapus.setOnClickListener {
            signaturePad.clear()
            placeholderText.visibility = View.VISIBLE
        }

        // Simpan coretan
        btnSimpan.setOnClickListener {
            if (signaturePad.isDrawn) {
                // Di sini kode Anda untuk menyimpan 'signaturePad.getSignatureBitmap()'
                Toast.makeText(this, "Tanda tangan Anda berhasil disimpan!", Toast.LENGTH_SHORT).show()

                // Jika ingin langsung menuju halaman profil atau selesai:
                // startActivity(Intent(this, halaman_profil_siswa::class.java))
                // finish()
            } else {
                Toast.makeText(this, "Tanda tangan masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun SignatureView.setOnTouchListener(function: Any) {}
