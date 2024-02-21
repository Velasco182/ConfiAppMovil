package com.example.pruebaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pruebaapp.fragments.RegistroFragment

class RegistroRContrasenaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_rcontrasena)

        // Crear una instancia del fragmento que deseas inflar
        val fragment = RegistroFragment()

        // Iniciar una transacción de fragmentos
        val transaction = supportFragmentManager.beginTransaction()

        // Reemplazar el contenido del FragmentContainer con el fragmento
        transaction.replace(R.id.fragmentContainerRegistroRContraseña, fragment)

        // Confirmar la transacción
        transaction.commit()
    }
}