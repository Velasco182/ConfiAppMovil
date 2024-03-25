package com.example.confiapp.models

data class TutorItem(
    //val id: String,
    val nombres: String,
    val apellidos: String,
    //val edad: String, val id: Int, val nacionalidad: String, val direccion: String, val profesion: String,
    val email: String,
    val telefono: String,
    val password: String,
    var tipoIdentificacion: String,
    val numerodeIdentificacion: String
)
