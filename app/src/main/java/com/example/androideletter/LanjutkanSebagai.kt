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
        val btnGuru = findViewById<MaterialButton>(R.id.btn_guru)

        // Berpindah ke halaman_masuk_siswa saat ditekan
        btnGuru.setOnClickListener {
            val intent = Intent(this, halaman_masuk_user::class.java)
            intent.putExtra("ROLE_SEBELUMNYA", "guru") // Memberi tanda "guru"
            startActivity(intent)
        }
        btnSiswa.setOnClickListener {
            val intent = Intent(this, halaman_masuk_user::class.java)
            intent.putExtra("ROLE_SEBELUMNYA", "siswa") // Memberi tanda "siswa"
            startActivity(intent)
        }
    }
}