package com.example.confiapp.apiservice

// 🡇🡇 RESPECTIVAS IMPORTACIONES 🡇🡇

import com.example.confiapp.models.NoticiasItem
import com.example.confiapp.models.RegistroRespuesta
import com.example.confiapp.models.TutorItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ConfiAppApiService {

    // fun

    //@GET("pokemon")

    @GET("noticias")
    fun getNoticias(): Call<List<NoticiasItem>>

    @GET("pokemon/{id}")
    fun getTutor(@Path("id") id: Int): Call<TutorItem>

    @POST("register")
    fun postTutor(@Body tutor: TutorItem): Response<RegistroRespuesta>

}