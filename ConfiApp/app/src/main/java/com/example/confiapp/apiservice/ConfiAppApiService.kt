package com.example.confiapp.apiservice

// 🡇🡇 RESPECTIVAS IMPORTACIONES 🡇🡇

import com.example.confiapp.models.TutorItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfiAppApiService {

    // fun

    //@GET("pokemon")

    @GET("noticias")
    fun getNoticias(): Call<NoticiasRespuesta>
    @GET("pokemon/{id}")
    fun getTutor(@Path("id") id: Int): Call<TutorItem>

}