package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class LanjutkanSebagai : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memanggil file lanjutkan_sebagai.xml
        setContentView(R.layout.lanjutkan_sebagai)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Mencari tombol berdasarkan ID di XML
        val btnSiswa = findViewById<MaterialButton>(R.id.btn_siswa)
        val btnGuru = findViewById<MaterialButton>(R.id.btn_guru)

        // Berpindah ke halaman_masuk_user saat ditekan (Guru)
        btnGuru.setOnClickListener {
            val intent = Intent(this, halaman_masuk_user::class.java)
            intent.putExtra("ROLE_SEBELUMNYA", "guru") // Memberi tanda "guru"
            startActivity(intent)

            // Menghilangkan animasi transisi
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay)        }

        // Berpindah ke halaman_masuk_user saat ditekan (Siswa)
        btnSiswa.setOnClickListener {
            val intent = Intent(this, halaman_masuk_user::class.java)
            intent.putExtra("ROLE_SEBELUMNYA", "siswa") // Memberi tanda "siswa"
            startActivity(intent)

            // Menghilangkan animasi transisi
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay)        }
    }
}