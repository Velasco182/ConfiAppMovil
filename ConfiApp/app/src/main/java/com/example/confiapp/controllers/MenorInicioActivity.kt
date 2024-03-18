package com.example.confiapp.controllers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.confiapp.R
import com.example.confiapp.databinding.ActivityMenorInicioBinding
import com.example.confiapp.fragments.MenorInicioFragment
import com.example.confiapp.helpers.OnBackPressedListener

class MenorInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenorInicioBinding
    lateinit var cardView : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenorInicioBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si el permiso no ha sido concedido, solicita permiso al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            // Si el permiso ya ha sido concedido, puedes realizar las acciones que requieran el permiso.
            // Por ejemplo, iniciar la obtención de la ubicación del usuario.

        }

        //val contenedor = binding.menorFragmentContainer
        val fragmentInicio = MenorInicioFragment()

        mostrarFragmento(fragmentInicio)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MainActivity.REQUEST_CODE_LOCATION_PERMISSION -> {
                // Verifica si el usuario ha concedido el permiso de ubicación
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // El usuario ha concedido el permiso de ubicación, puedes realizar las acciones que requieran el permiso.
                    // Por ejemplo, iniciar la obtención de la ubicación del usuario.


                } else {
                    // El usuario ha denegado el permiso de ubicación, puedes mostrar un mensaje indicando que la funcionalidad no estará disponible.
                    Toast.makeText(
                        this,
                        "Permiso de ubicación denegado. La funcionalidad de ubicación no estará disponible.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
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
