package com.example.confiapp.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfiAppApiClient {

    companion object {
        private const val BASE_URL = "https://nuevomern-7y1b.onrender.com/api/"

        // Creamos una función para crear una instancia de Retrofit.
        fun createApiService(): ConfiAppApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ConfiAppApiService::class.java)
        }
    }

}