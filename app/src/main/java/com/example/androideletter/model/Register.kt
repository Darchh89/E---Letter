package com.example.androideletter.model

import com.google.gson.annotations.SerializedName

// --- REQUEST MODEL ---
data class RegisterRequest(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String,
    @SerializedName("token") val token: String
)

// --- RESPONSE MODEL ---
data class RegisterResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: RegisterData?
)

data class RegisterData(
    @SerializedName("id") val id: Int,
    @SerializedName("login_code") val loginCode: String,
    @SerializedName("user_code") val userCode: String
)