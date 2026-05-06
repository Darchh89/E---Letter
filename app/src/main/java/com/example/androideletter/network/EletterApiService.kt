package com.example.androideletter.api

import com.example.androideletter.model.RegisterRequest
import com.example.androideletter.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.androideletter.model.LoginRequest
import com.example.androideletter.model.LoginResponse


interface EletterApiService {

    @POST("api/auth/register")
    fun registerStudent(
        @Body request: RegisterRequest
    ): Call<AuthResponse>

    @POST("api/auth/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}