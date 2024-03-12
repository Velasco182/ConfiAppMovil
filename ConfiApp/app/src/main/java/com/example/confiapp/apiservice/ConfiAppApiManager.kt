package com.example.confiapp.apiservice

import com.example.confiapp.models.TutorItem
import com.example.confiapp.models.TutorLoginItem
import retrofit2.Callback

class ConfiAppApiManager(private val apiService: ConfiAppApiService) {

    fun insertarDatos(userDataList: TutorItem, callback: Callback<ConfiAppApiService.ResponseApi>) {
        // Llamada a la API usando Retrofit con un callback
        apiService.postTutor(userDataList).enqueue(callback)
    }

    fun insertarLogin(userDataList: TutorLoginItem, callback: Callback<ConfiAppApiService.LoginResponse>){
        apiService.postTutorLogin(userDataList).enqueue(callback)
    }
}