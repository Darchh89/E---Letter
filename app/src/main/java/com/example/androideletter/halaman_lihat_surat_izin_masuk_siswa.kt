package com.example.androideletter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class halaman_lihat_surat_izin_masuk_siswa : AppCompatActivity() {

    private var filterAktif = "Semua"
    private var kataKunciPencarian = ""

    data class SuratDummy(val viewId: Int, val status: String, val nomorSurat: String)

    // Perhatikan nomor suratnya menggunakan SRT-MSK
    private val daftarSurat = listOf(
        SuratDummy(R.id.item_surat_1, "Menunggu", "SRT-MSK-005"),
        SuratDummy(R.id.item_surat_2, "Menunggu", "SRT-MSK-004"),
        SuratDummy(R.id.item_surat_3, "Menunggu", "SRT-MSK-003"),
        SuratDummy(R.id.item_surat_4, "Disetujui", "SRT-MSK-002"),
        SuratDummy(R.id.item_surat_5, "Ditolak", "SRT-MSK-001")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_lihat_surat_izin_masuk_siswa)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Tombol Back ke Halaman Menu Utama
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            val intent = Intent(this, halaman_lihat_surat_siswa::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Pencarian (Search)
        val etCariSurat = findViewById<EditText>(R.id.et_cari_surat)
        etCariSurat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                kataKunciPencarian = s.toString().trim()
                jalankanPenyaringan()
            }
        })

        // Tombol Filter
        val btnFilter = findViewById<MaterialCardView>(R.id.btn_filter)
        btnFilter.setOnClickListener {
            tampilkanDialogFilter()
        }
    }

    private fun tampilkanDialogFilter() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.dialog_filter_surat)

        var filterPilihanSementara = filterAktif

        val btnTutup = bottomSheetDialog.findViewById<ImageView>(R.id.btn_tutup_filter)
        val btnTerapkan = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_terapkan_filter)
        val btnReset = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_reset_filter)

        val cardSemua = bottomSheetDialog.findViewById<MaterialCardView>(R.id.filter_semua)
        val cardMenunggu = bottomSheetDialog.findViewById<MaterialCardView>(R.id.filter_menunggu)
        val cardDisetujui = bottomSheetDialog.findViewById<MaterialCardView>(R.id.filter_disetujui)
        val cardDitolak = bottomSheetDialog.findViewById<MaterialCardView>(R.id.filter_ditolak)

        fun updateUIDialog() {
            val allCards = listOf(cardSemua, cardMenunggu, cardDisetujui, cardDitolak)
            for (card in allCards) {
                card?.setCardBackgroundColor(Color.parseColor("#F9FAFB"))
                card?.strokeColor = Color.parseColor("#E5E7EB")
                val rb = card?.getChildAt(0)?.findViewById<RadioButton>(card.getChildAt(0).resources.getIdentifier("rb_${card.resources.getResourceEntryName(card.id).substringAfter("_")}", "id", packageName))
                rb?.isChecked = false
            }

            val kartuAktif = when (filterPilihanSementara) {
                "Menunggu" -> cardMenunggu
                "Disetujui" -> cardDisetujui
                "Ditolak" -> cardDitolak
                else -> cardSemua
            }
            kartuAktif?.setCardBackgroundColor(Color.parseColor("#F0F8FF"))
            kartuAktif?.strokeColor = Color.parseColor("#3FA2F6")

            val rbAktif = when (filterPilihanSementara) {
                "Menunggu" -> bottomSheetDialog.findViewById<RadioButton>(R.id.rb_menunggu)
                "Disetujui" -> bottomSheetDialog.findViewById<RadioButton>(R.id.rb_disetujui)
                "Ditolak" -> bottomSheetDialog.findViewById<RadioButton>(R.id.rb_ditolak)
                else -> bottomSheetDialog.findViewById<RadioButton>(R.id.rb_semua)
            }
            rbAktif?.isChecked = true
        }

        updateUIDialog()

        cardSemua?.setOnClickListener { filterPilihanSementara = "Semua"; updateUIDialog() }
        cardMenunggu?.setOnClickListener { filterPilihanSementara = "Menunggu"; updateUIDialog() }
        cardDisetujui?.setOnClickListener { filterPilihanSementara = "Disetujui"; updateUIDialog() }
        cardDitolak?.setOnClickListener { filterPilihanSementara = "Ditolak"; updateUIDialog() }

        btnReset?.setOnClickListener {
            filterPilihanSementara = "Semua"
            updateUIDialog()
        }

        btnTutup?.setOnClickListener { bottomSheetDialog.dismiss() }

        btnTerapkan?.setOnClickListener {
            filterAktif = filterPilihanSementara
            jalankanPenyaringan()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun jalankanPenyaringan() {
        val query = kataKunciPencarian.lowercase()
        for (surat in daftarSurat) {
            val view = findViewById<MaterialCardView>(surat.viewId) ?: continue
            val cocokFilter = if (filterAktif == "Semua") true else surat.status == filterAktif
            val cocokPencarian = if (query.isEmpty()) true else surat.nomorSurat.lowercase().contains(query)

            if (cocokFilter && cocokPencarian) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}