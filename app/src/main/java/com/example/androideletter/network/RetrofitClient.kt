package com.example.androideletter.network

import com.example.androideletter.api.EletterApiService // Sesuaikan dengan lokasi interface API Anda
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://172.19.233.99:3000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: EletterApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL )
            .client(client) // Memasang interceptor ke Retrofit untuk melihat log data[cite: 1]
            .addConverterFactory(GsonConverterFactory.create()) // Memasang Gson untuk konversi JSON[cite: 1]
            .build()

        retrofit.create(EletterApiService::class.java)
    }
}