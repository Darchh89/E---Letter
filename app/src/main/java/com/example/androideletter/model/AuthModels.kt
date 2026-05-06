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