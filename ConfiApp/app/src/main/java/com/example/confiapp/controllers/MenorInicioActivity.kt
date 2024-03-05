package com.example.confiapp.controllers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.confiapp.R
import com.example.confiapp.databinding.ActivityMenorInicioBinding
import com.example.confiapp.fragments.LoginFragment
import com.example.confiapp.fragments.MenorInicioFragment
import com.example.confiapp.fragments.MensajesMenorFragment
import com.example.confiapp.fragments.OnBackPressedListener
import com.example.confiapp.fragments.PerfilFragment

class MenorInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenorInicioBinding
    lateinit var cardView : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenorInicioBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        //val contenedor = binding.menorFragmentContainer
        val fragmentInicio = MenorInicioFragment()

        mostrarFragmento(fragmentInicio)

    }

    // Iniciar una transacción de fragmentos
    private fun mostrarFragmento(nuevoFragmento: Fragment) {
        val transaccion = supportFragmentManager.beginTransaction()
        transaccion.replace(R.id.menorFragmentContainer, nuevoFragmento)
        transaccion.addToBackStack(null)
        transaccion.commit()
    }

    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.menorFragmentContainer)
        if (fragment is OnBackPressedListener) {
            val handled = fragment.onBackPressed()
            if (handled) {
                // El fragmento manejó el evento onBackPressed
                return
            }
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("¿Cerrar la aplicación?")
        alertDialog.setMessage("¿Estás seguro de que quieres salir de la aplicación?")

        alertDialog.setPositiveButton("Sí") { _, _ ->

            super.onBackPressed()
                // Cierra la aplicación
                //android.os.Process.killProcess(android.os.Process.myPid())
                finishAffinity()
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Cancela la acción
        }

        alertDialog.show()

    }
}
