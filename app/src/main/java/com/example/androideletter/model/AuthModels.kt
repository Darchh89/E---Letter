package com.example.androideletter.model

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

// ... (RegisterRequest & AuthResponse biarkan saja)

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

// Tambahkan di file model Anda
data class SuratIzinMasukRequest(
    val title: String,
    val department_id: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val description: String
)

data class RiwayatSuratResponse(
    val id: Int,
    val request_number: String,
    val title: String,
    val request_date: String,
    val status: String,
    val type_code: String, // contoh: 'izin_masuk', 'izin_keluar'
    val type_label: String // contoh: 'Izin Masuk'
)