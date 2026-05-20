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
    @GET("user/profile")
    fun getStudentProfile(
        @Header("Authorization") token: String
    ): Call<StudentProfileResponse>

    // POST /api/v1/user/update (Backend pakai POST, bukan PUT)
    @POST("user/update")
    fun updateStudentProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Call<GeneralResponse>

    // POST /api/v1/user/signature (Backend menerima JSON string SVG, bukan File Multipart)
    @POST("user/signature")
    fun uploadSignature(
        @Header("Authorization") token: String,
        @Body request: SignatureRequest // <- Pakai SignatureRequest, bukan MultipartBody.Part
    ): Call<SignatureResponse>

    // --- DATA & SURAT ---
// Ganti getClasses yang lama menjadi ini:
    @GET("classes")
    fun getClasses(
        @Header("Authorization") token: String
    ): Call<MasterDataResponse>

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