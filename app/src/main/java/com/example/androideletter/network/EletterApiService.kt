package com.example.androideletter.api

import com.example.androideletter.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EletterApiService {

    // --- AUTHENTICATION ---
    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register") // Pastikan endpoint ini sesuai dengan API backend Anda (misal "auth/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    // --- PROFILE ---
    @GET("api/student/profile")
    fun getStudentProfile(
        @Header("Authorization") token: String
    ): Call<StudentProfileResponse>

    @PUT("api/student/profile")
    fun updateStudentProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Call<GeneralResponse>

    @Multipart
    @POST("api/student/signature")
    fun uploadSignature(
        @Header("Authorization") token: String,
        @Part signature: MultipartBody.Part
    ): Call<SignatureResponse>

    // --- DATA & SURAT ---
    @GET("api/classes")
    fun getClasses(
        @Header("Authorization") token: String
    ): Call<List<DepartmentResponse>>

    @GET("api/student/history")
    fun getHistorySurat(
        @Header("Authorization") token: String
    ): Call<List<RiwayatSuratResponse>>

    @GET("api/student/search")
    fun searchStudent(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<List<SearchStudentResponse>>

    @POST("api/student/surat-izin-masuk")
    fun buatSuratIzinMasuk(
        @Header("Authorization") token: String,
        @Body request: SuratIzinMasukRequest
    ): Call<GeneralResponse>

    @POST("api/student/surat-izin-keluar")
    fun buatSuratIzinKeluarMulti(
        @Header("Authorization") token: String,
        @Body request: CreateIzinKeluarRequest
    ): Call<GeneralResponse>

    @POST("api/student/surat-izin-masuk")
    fun buatSuratIzinMasukMulti(
        @Header("Authorization") token: String,
        @Body request: CreateIzinKeluarRequest
    ): Call<GeneralResponse>

    @GET("api/student/request/{id}")
    fun getDetailSurat(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<DetailSuratResponse>
}