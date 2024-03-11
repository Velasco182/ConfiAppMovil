package com.example.pruebaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class Registro(var nombre: String, var descripcion: String) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }
}
