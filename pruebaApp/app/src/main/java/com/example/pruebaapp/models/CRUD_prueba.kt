package com.example.pruebaapp.models

import android.widget.ListView
import com.example.pruebaapp.Registro
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception


// es como el manager
class CRUD_prueba {
    private val database:DatabaseReference= FirebaseDatabase.getInstance().reference

    fun crearRegistro( nombre: String , descripcion: String){
        val nuevoregistro= database.child("registros").push()
        nuevoregistro.child("nombre").setValue(nombre)
        nuevoregistro.child("descripcion").setValue(descripcion)

    }
    fun leerRegistros(
        onSuccess: (List<Registro>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child("registros").get().addOnSuccessListener { snapshot ->
            val registros = mutableListOf<Registro>()
            for (registroSnapshot in snapshot.children) {
                val nombre = registroSnapshot.child("nombre").value.toString()
                val descripcion = registroSnapshot.child("descripcion").value.toString() // Aquí estaba el error
                registros.add(Registro(nombre, descripcion))
            }
            onSuccess(registros)
        }.addOnFailureListener(onFailure)
    }



    fun actualizarregistro(registroId: String, nuevoNombre: String, nuevadescripcion: String) {
    database.child("registros").child(registroId).child("nombre").setValue(nuevoNombre)
    database.child("registros").child(registroId).child("descripcion").setValue(nuevadescripcion)
}

fun eliminarregistro(registroId: String) {
    database.child("registros").child(registroId).removeValue()
}

}