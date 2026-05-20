    package com.example.androideletter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// Data yang diterima dari server
data class AuthResponse(
    val message: String,
    val user_id: Int? = null
)

// Model untuk mengirim data Login

// Data spesifik pengguna yang dikembalikan server

data class StudentProfileResponse(
    val full_name: String?,
    val student_code: String?,
    val email: String?,
    val gender: String?,
    val class_name: String?,
    val total_izin_masuk: Int?,
    val total_izin_keluar: Int?
)

// Model untuk mengirim request PUT Edit Profil
data class UpdateProfileRequest(
    val full_name: String,
    val student_code: String,
    val gender: String,
    val class_id: Int?
)
// Model untuk response umum yang hanya berisi pesan
data class GeneralResponse(
    val message: String
)

// Model khusus untuk response upload tanda tangan
data class SignatureResponse(
    val message: String,
    val signature_url: String?
)

data class DepartmentResponse(
    val id: Int,
    val name: String
) {
    // TAMBAHAN: Agar di Spinner yang muncul adalah namanya, bukan kode objek
    override fun toString(): String {
        return name
    }
}
data class SuratIzinMasukRequest(
    val title: String,
    val department_id: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val description: String
)

// === UPDATED: Ditambahkan @Parcelize dan kolom tambahan ===
@Parcelize
data class RiwayatSuratResponse(
    val id: Int,
    val request_number: String?,
    val title: String?,
    val reason: String?,
    val request_date: String?,
    val start_time: String?,
    val end_time: String?,
    val status: String?,
    val created_at: String?,
    val type_code: String?,
    val type_label: String?,
    val student_name: String?,
    val class_name: String?
) : Parcelable



// Model untuk menerima hasil pencarian siswa
data class SearchStudentResponse(
    val student_id: Int?,
    val id: Int?,
    val full_name: String,
    val class_name: String?
)

// Model untuk mengirim pengajuan
data class CreateIzinKeluarRequest(
    val date: String,
    val start_time: String,
    val end_time: String,
    val reason: String,       
    val student_ids: List<Int>
)

data class StudentItem(
    val full_name: String,
    val class_name: String?
)

data class ApprovalItem(
    val approver_role: String,
    val status: String,
    val signature_url: String?,
    val approver_name: String?
)

data class DetailSuratResponse(
    val id: Int,
    val request_number: String?,
    val reason: String?,
    val request_date: String?,
    val start_time: String?,
    val end_time: String?,
    val status: String?,
    val created_at_formatted: String?,
    val type_label: String?,
    val student_signature_url: String?, // TTD Siswa
    val students: List<StudentItem>,    // Daftar Multi-Siswa
    val approvals: List<ApprovalItem>   // Daftar TTD Guru
)

data class DetailSiswa(
    val full_name: String,
    val class_name: String?
)

data class DetailApproval(
    val approver_role: String,
    val status: String,
    val signature_url: String?,
    val approver_name: String
)