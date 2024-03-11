package com.example.pruebaapp.models

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase

class AgregarRuta_PRUEBA : Application(){
    override fun onCreate(){
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

    }
}

