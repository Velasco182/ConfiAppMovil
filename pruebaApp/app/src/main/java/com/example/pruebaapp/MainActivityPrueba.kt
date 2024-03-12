package com.example.pruebaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pruebaapp.databinding.ActivityMainPruebaBinding
import com.google.firebase.FirebaseApp
import java.lang.Exception

class MainActivityPrueba : AppCompatActivity() {

    private lateinit var binding: ActivityMainPruebaBinding
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPruebaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Dentro de onCreate de MainActivityPrueba
        FirebaseApp.initializeApp(this)



        // Aquí se referencia correctamente el botón y los EditText
        binding.botonEnviar.setOnClickListener {
            val nombre = binding.nombre.text.toString()
            val descripcion = binding.descripcion.text.toString()
            if (nombre.isNotEmpty() && descripcion.isNotEmpty()) {
                crearRegistro(nombre, descripcion)
                Toast.makeText(this, "Registro creado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa un nombre y una descripción", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun crearRegistro(nombre: String, descripcion: String) {
        val nuevoRegistro = database.child("registros").push()
        nuevoRegistro.child("nombre").setValue(nombre)
        nuevoRegistro.child("descripcion").setValue(descripcion)
    }

    fun leerRegistros(
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child("registros").get().addOnSuccessListener { snapshot ->
            val registros = mutableListOf<Pair<String, String>>()
            for (registroSnapshot in snapshot.children) {
                val nombre = registroSnapshot.child("nombre").value.toString()
                val descripcion = registroSnapshot.child("descripcion").value.toString()
                registros.add(nombre to descripcion)
            }
            onSuccess(registros)
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun actualizarRegistro(registroId: String, nuevoNombre: String, nuevaDescripcion: String) {
        val registroRef = database.child("registros").child(registroId)
        registroRef.child("nombre").setValue(nuevoNombre)
        registroRef.child("descripcion").setValue(nuevaDescripcion)
    }

    fun eliminarRegistro(registroId: String) {
        database.child("registros").child(registroId).removeValue()
    }

}

//data class Registro()