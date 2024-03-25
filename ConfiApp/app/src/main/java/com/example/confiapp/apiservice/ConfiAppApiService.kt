package com.example.confiapp.apiservice

import com.example.confiapp.models.InicioItem
import com.example.confiapp.models.NoticiasItem
import com.example.confiapp.models.TutorItem
import com.example.confiapp.models.TutorLoginItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ConfiAppApiService {

    data class ResponseApi(
        val message: ArrayList<String>
    )

   data class LoginResponse(
        val message: ArrayList<String>,
        val token: String
    )

    data class HistorialResponse(
        val message: ArrayList<String>,
        val token: String
    )

    @GET("noticias")
    fun getNoticias(): Call<List<NoticiasItem>>

    @GET("tasks")
    fun getRutas(): Call<List<InicioItem>>

    @POST("register")
    fun postTutor(@Body tutor: TutorItem): Call<ResponseApi> // Ajuste para usar Call

    @GET("profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<TutorItem>

    @POST("login")
    fun postTutorLogin(@Body tutorLogin: TutorLoginItem): Call<LoginResponse>

}