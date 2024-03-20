package com.example.confiapp.apiservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConfiAppApiClient {

    companion object {
        private const val BASE_URL = "https://nuevomern-7y1b.onrender.com/api/"

        // Creamos una función para crear una instancia de Retrofit.
        fun createApiService(): ConfiAppApiService {

           // Puedes ajustar estos valores según necesites
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ConfiAppApiService::class.java)
        }
    }

}