package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.androideletter.LanjutkanSebagai
import com.example.androideletter.R

class LoadingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memanggil file loading_screen.xml
        setContentView(R.layout.loading_screen)

        // Delay 3 detik (3000ms) lalu pindah ke LanjutkanSebagai
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LanjutkanSebagai::class.java)
            startActivity(intent)
            finish() // Menutup halaman loading
        }, 3000)
    }
}