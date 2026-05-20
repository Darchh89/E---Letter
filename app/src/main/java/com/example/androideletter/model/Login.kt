package com.example.androideletter.model

import com.google.gson.annotations.SerializedName

// --- 1. REQUEST MODEL ---
data class LoginRequest(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String
)

// --- 2. RESPONSE MODEL ---
data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: LoginData?
)

data class LoginData(
    @SerializedName("user") val user: UserData,
    @SerializedName("accessToken") val accessToken: String
)

data class UserData(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("role") val role: String,
    @SerializedName("login_code") val loginCode: String
)