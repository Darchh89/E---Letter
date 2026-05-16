package com.example.androideletter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.androideletter.LanjutkanSebagai
import com.example.androideletter.R
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class LoadingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memanggil file loading_screen.xml
        setContentView(R.layout.loading_screen)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Delay 3 detik (3000ms) lalu pindah ke LanjutkanSebagai
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LanjutkanSebagai::class.java)
            startActivity(intent)

            // Tambahkan kode ini untuk animasi Fade In dan Fade Out
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish() // Menutup halaman loading
        }, 3000)
    }
}