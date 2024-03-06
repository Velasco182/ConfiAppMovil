package com.example.confiapp.apiservice

import android.util.Log
import android.widget.Toast
import com.example.confiapp.models.TutorItem

class ConfiAppApiManager(private val apiService: ConfiAppApiService) {

    // Creamos una clase que actúa como un administrador para realizar solicitudes a la API.
    suspend fun insertarDatos(userDataList: TutorItem): ConfiAppApiService.ResponseApi? {
        return try {

            val result = apiService.postTutor(userDataList)
            Log.e("TAG", "de: ${result} ")
            result
        } catch (e: Exception) {
            null
        }
    }
}
