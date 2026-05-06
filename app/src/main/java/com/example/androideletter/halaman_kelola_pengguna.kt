package com.example.androideletter

import android.content.Intent
import android.graphics.Color
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
import com.google.android.material.card.MaterialCardView

class halaman_kelola_pengguna : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_kelola_pengguna)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val btnKeluar = findViewById<LinearLayout>(R.id.btn_keluar)
        val tabDasbor = findViewById<MaterialCardView>(R.id.tab_dasbor)
        val tabKelola = findViewById<MaterialCardView>(R.id.tab_kelola)
        val tabPersetujuan = findViewById<MaterialCardView>(R.id.tab_persetujuan) // Di XML bernama persetujuan, namun fungsinya unduh

        // LOGIKA TOMBOL KELUAR DINONAKTIFKAN
        btnKeluar.setOnClickListener {
            Toast.makeText(this, "Fungsi Keluar dinonaktifkan", Toast.LENGTH_SHORT).show()
        }

        // LOGIKA TAB
        tabDasbor.setOnClickListener {
            startActivity(Intent(this, halaman_beranda_admin    ::class.java))
            finish()
        }
        tabKelola.setOnClickListener {
            startActivity(Intent(this, halaman_kelola_surat::class.java))
            finish()
        }
        tabPersetujuan.setOnClickListener {
            startActivity(Intent(this, halaman_unduh_surat::class.java))
            finish()
        }

        // LOGIKA ACCORDION
        val header1 = findViewById<LinearLayout>(R.id.header_item1)
        val content1 = findViewById<LinearLayout>(R.id.content_item1)
        val arrow1 = findViewById<ImageView>(R.id.arrow_item1)
        val namaBiru1 = findViewById<TextView>(R.id.nama_biru_item1)

        val header2 = findViewById<LinearLayout>(R.id.header_item2)
        val content2 = findViewById<LinearLayout>(R.id.content_item2)
        val arrow2 = findViewById<ImageView>(R.id.arrow_item2)
        val namaBiru2 = findViewById<TextView>(R.id.nama_biru_item2)

        val header3 = findViewById<LinearLayout>(R.id.header_item3)
        val content3 = findViewById<LinearLayout>(R.id.content_item3)
        val arrow3 = findViewById<ImageView>(R.id.arrow_item3)
        val namaBiru3 = findViewById<TextView>(R.id.nama_biru_item3)

        fun toggleExpand(content: LinearLayout, arrow: ImageView, textNama: TextView) {
            if (content.visibility == View.GONE) {
                content.visibility = View.VISIBLE
                arrow.animate().rotation(180f).setDuration(200).start()
                textNama.setTextColor(Color.parseColor("#000000"))
            } else {
                content.visibility = View.GONE
                arrow.animate().rotation(0f).setDuration(200).start()
                textNama.setTextColor(Color.parseColor("#75BCED"))
            }
        }

        header1.setOnClickListener { toggleExpand(content1, arrow1, namaBiru1) }
        header2.setOnClickListener { toggleExpand(content2, arrow2, namaBiru2) }
        header3.setOnClickListener { toggleExpand(content3, arrow3, namaBiru3) }
    }
}