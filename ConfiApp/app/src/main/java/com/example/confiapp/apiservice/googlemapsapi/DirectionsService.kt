package com.example.confiapp.apiservice.googlemapsapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsService {

    @GET("maps/api/directions/json")

    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String

    ): Call<DirectionsResponse>

}