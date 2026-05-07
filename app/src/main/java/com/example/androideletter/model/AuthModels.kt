package com.example.androideletter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data yang dikirim ke server
data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val token: String? = null // Bisa null karena backend akan otomatis set untuk siswa
)

// Data yang diterima dari server
data class AuthResponse(
    val message: String,
    val user_id: Int? = null
)

// Model untuk mengirim data Login
data class LoginRequest(
    val email: String,
    val password: String
)

// Model untuk menerima respon Login dari server
data class LoginResponse(
    val message: String,
    val token: String?,
    val user: UserData?
)

// Data spesifik pengguna yang dikembalikan server
data class UserData(
    val id: Int,
    val email: String,
    val role: String,
    val full_name: String
)

data class StudentProfileResponse(
    val email: String?,
    val full_name: String?,
    val student_code: String?,
    val gender: String?,
    val phone: String?,
    val signature_url: String?
)

// Model untuk mengirim request PUT Edit Profil
data class UpdateProfileRequest(
    val full_name: String,
    val student_code: String,
    val gender: String // Diisi 'male' atau 'female' sesuai database
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
)

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