package com.example.confiapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.confiapp.R
import com.example.confiapp.databinding.FragmentMenorInicioBinding
import com.example.confiapp.databinding.FragmentMensajesMenorBinding
import com.example.confiapp.helpers.Llamadas
import com.example.confiapp.helpers.OnBackPressedListener

class MensajesMenorFragment : Fragment(R.layout.fragment_mensajes_menor), OnBackPressedListener {

    private lateinit var binding: FragmentMensajesMenorBinding
    override fun onBackPressed(): Boolean {
        // Este fragmento también podría manejar onBackPressed de manera personalizada
        // Si este fragmento no necesita manejar el evento, simplemente devuelve false
        mostrarFragmento(MenorInicioFragment())
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMensajesMenorBinding.bind(view)

        val llamarButton = binding.llamarTutorButton

        llamarButton.setOnClickListener {
            Llamadas.makePhoneCall(this, "1234567890")
        }

    }

    private fun mostrarFragmento(nuevoFragmento: Fragment) {
        val transaccion = fragmentManager?.beginTransaction()
        transaccion?.replace(R.id.menorFragmentContainer, nuevoFragmento)
        transaccion?.addToBackStack(null)
        transaccion?.commit()
    }
}