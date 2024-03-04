package com.example.confiapp.models

data class NoticiasItem(var id:Long, var nombre: String, var foto: String, var descripcion: String, var responses:List<String>? = null)