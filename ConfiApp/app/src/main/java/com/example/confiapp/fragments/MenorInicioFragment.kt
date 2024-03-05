package com.example.confiapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.confiapp.R
import com.example.confiapp.databinding.FragmentMenorInicioBinding

class MenorInicioFragment : Fragment(R.layout.fragment_menor_inicio), OnBackPressedListener {
    override fun onBackPressed(): Boolean {
        // Aquí, implementa lo que quieras hacer cuando se presiona atrás
        // y devuelve true si quieres que este manejo sea el final (no quiere que se llame al comportamiento por defecto del sistema)
        // Por ejemplo, para volver al fragmento anterior podrías hacer una transacción de fragmento aquí
        // y luego devolver true.
        return false
    }

    private lateinit var binding: FragmentMenorInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenorInicioBinding.bind(view)

        val llamarButton = binding.llamarMenorButton
        val mensajeButton = binding.mensajeMenorButton

        val fragmentMensajes = MensajesMenorFragment()

        //mostrarFragmento(fragmentMensajes)

        mensajeButton.setOnClickListener {
            mostrarFragmento(fragmentMensajes)
            //cardView.visibility = View.GONE
        }

    }

    private fun mostrarFragmento(nuevoFragmento: Fragment) {
        val transaccion = fragmentManager?.beginTransaction()
        transaccion?.replace(R.id.menorFragmentContainer, nuevoFragmento)
        transaccion?.addToBackStack(null)
        transaccion?.commit()
    }

}