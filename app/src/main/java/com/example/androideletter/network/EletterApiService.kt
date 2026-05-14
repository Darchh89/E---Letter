package com.example.androideletter.api

import com.example.androideletter.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EletterApiService {
        // Ubah dari registerStudent menjadi registerUser agar universal
        // SATU FUNGSI UNTUK SEMUA REGISTRASI (GURU & SISWA)
    @POST("api/auth/register")
    fun registerUser(
    @Body request: RegisterRequest): Call<GeneralResponse> // Konsisten menggunakan GeneralResponse

    @POST("api/auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // 1. Ambil Profil Siswa
    @GET("api/student/profile")
    fun getStudentProfile(
        @Header("Authorization") token: String
    ): Call<StudentProfileResponse>

    // 2. Update Profil Siswa (Teks)
    @PUT("api/student/profile")
    fun updateStudentProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Call<GeneralResponse>

    // 3. Upload Tanda Tangan (File Gambar)
    @Multipart
    @POST("api/student/signature")
    fun uploadSignature(
        @Header("Authorization") token: String,
        @Part signature: MultipartBody.Part
    ): Call<SignatureResponse>

    @GET("api/classes") // <-- Berubah menjadi api/classes
    fun getClasses(
        @Header("Authorization") token: String
    ): Call<List<DepartmentResponse>>

    // Endpoint Buat Surat Izin Masuk
    @POST("api/student/surat-izin-masuk")
    fun buatSuratIzinMasuk(
        @Header("Authorization") token: String,
        @Body request: SuratIzinMasukRequest
    ): Call<GeneralResponse> // Menggunakan GeneralResponse yang sudah ada

    @GET("api/student/history")
    fun getHistorySurat(
        @Header("Authorization") token: String
    ): Call<List<RiwayatSuratResponse>>

    @GET("api/student/search")
    fun searchStudent(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<List<SearchStudentResponse>>

    @POST("api/student/surat-izin-keluar")
    fun buatSuratIzinKeluarMulti(
        @Header("Authorization") token: String,
        @Body request: CreateIzinKeluarRequest
    ): Call<GeneralResponse>


    @POST("api/student/surat-izin-masuk")
    fun buatSuratIzinMasukMulti(
        @Header("Authorization") token: String,
        @Body request: CreateIzinKeluarRequest // Reusable model request
    ): Call<GeneralResponse>

    @GET("api/student/request/{id}")
    fun getDetailSurat(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<DetailSuratResponse>
}
