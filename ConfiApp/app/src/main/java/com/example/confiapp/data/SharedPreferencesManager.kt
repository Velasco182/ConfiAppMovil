package com.example.confiapp.data

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.models.TutorRespuesta
import com.google.gson.Gson

class SharedPreferencesManager(private val context: Context) {

    private val name_DB = "ConfiApp"

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
        //editor.putString("saveToken", token)

        //Aplicar cambios
        val success = editor.commit() // Cambiado a commit() para verificar si la operación de escritura tiene éxito
        if (!success) {
            // Manejo de errores: la operación de escritura falló
            Toast.makeText(context, "Error SharedPreferences", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error al guardar los datos del usuario y el token en SharedPreferences")
        }else{
            editor.apply()
        }

    }

    fun saveToken(token: String?){
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    //Obtener Token
    fun getToken(): String{
        return  sharedPreferences.getString("token", "Empty").toString()
    }

    fun saveTutorResponse(tutorRespuesta: ConfiAppApiService.LoginResponse?){
        val json = Gson().toJson(tutorRespuesta)
        val editor = sharedPreferences.edit()
        editor.putString("tutorDetails", json)

        //Aplicar cambios
        val success = editor.commit() // Cambiado a commit() para verificar si la operación de escritura tiene éxito
        if (!success) {
            // Manejo de errores: la operación de escritura falló
            Toast.makeText(context, "Error SharedPreferences", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error al guardar los datos del usuario y el token en SharedPreferences")
        }else{
            //editor.apply()
        }
    }

    // Función para obtener la respuesta del tutor guardada en SharedPreferences
    fun getTutorResponse(): ConfiAppApiService.LoginResponse? {
        val json = sharedPreferences.getString("tutorDetails", null)
        if (json != null) {
            val gson = Gson()
            return gson.fromJson(json, ConfiAppApiService.LoginResponse::class.java)
        }
        return null
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

    fun logOut(){
        val cerrar = sharedPreferences.edit()
        cerrar.clear()
        cerrar.apply()

        ///Cerrar sesión de google
        //Firebase.auth.signOut()
    }

}