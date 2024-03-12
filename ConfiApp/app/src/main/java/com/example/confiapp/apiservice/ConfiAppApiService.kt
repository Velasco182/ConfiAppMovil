package com.example.confiapp.apiservice

// 🡇🡇 RESPECTIVAS IMPORTACIONES 🡇🡇

import com.example.confiapp.models.InicioItem
import com.example.confiapp.models.NoticiasItem
import com.example.confiapp.models.RouteResponse
import com.example.confiapp.models.TutorItem
import com.example.confiapp.models.TutorLoginItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ConfiAppApiService {

    data class ResponseApi(
        val message: ArrayList<String>
    )

    data class LoginResponse(
        //val success: Boolean,
        val message: ArrayList<String>,
        val token: String?,
        val userId: String?
    )

    @GET("noticias")
    fun getNoticias(): Call<List<NoticiasItem>>

    @GET("tasks")
    fun getRutas(): Call<List<InicioItem>>

    @POST("register")
    fun postTutor(@Body tutor: TutorItem): Call<ResponseApi> // Ajuste para usar Call

    @POST("login")
    fun postTutorLogin(@Body tutorLogin: TutorLoginItem): Call<LoginResponse>

    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") key: String,
        @Query("start", encoded = true) start: String,
        @Query("end", encoded = true) end: String
    ): Response<RouteResponse>

}