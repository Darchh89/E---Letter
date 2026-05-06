package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class LanjutkanSebagai : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memanggil file lanjutkan_sebagai.xml
        setContentView(R.layout.lanjutkan_sebagai)

        // Mencari tombol berdasarkan ID di XML
        val btnSiswa = findViewById<MaterialButton>(R.id.btn_siswa)

        // Berpindah ke halaman_masuk_siswa saat ditekan
        btnSiswa.setOnClickListener {
            val intent = Intent(this, halaman_masuk_siswa::class.java)
            startActivity(intent)
        }
    }
}