package com.example.pruebaapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.contentValuesOf

class SharedPreferencesManager(private val context: Context) {

    private val name_DB = "myDataBase"

    private val sharedPreferences : SharedPreferences by lazy {
        ///Solamente para el teléfono en el que se esta usando la aplicación (nombre de la db y el segundo el modo en que se va a manejar la base de datos)
        context.getSharedPreferences(name_DB, Context.MODE_PRIVATE)
    }

    // Función para almacenar los datos
    fun saveUser(user: String, pass: String){

        //Asignar una edición al SharedPreferences (editar)
        val editor = sharedPreferences.edit()

        //Asignar el Token o Clave
        editor.putString("saveUser", user)
        editor.putString("savePass", pass)

        //Aplicar cambios
        editor.apply()

    }

    fun saveBool(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("myBool", true)
        editor.apply()
    }

    // Función para mostrar los datos
    fun getUser(): String{

        // Retornar la clave que identifica el contenido de las SharedPreferences
        return sharedPreferences.getString("saveUser", "Empty").toString()
    }

    fun getPass(): String{

        // Retornar la clave que identifica el contenido de las SharedPreferences
        return sharedPreferences.getString("savePass", "Empty").toString()
    }

    fun getBool(): Boolean{
        return sharedPreferences.getBoolean("myBool", false)
    }

    // Crear función para obtener los datos del registro y así poder ingresar en el Inicio de Sesión
    // (Usuario o Cédula & Contraseña)
    fun saveRegistro(){

    }
}