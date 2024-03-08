package com.example.confiapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.confiapp.R
class MensajesMenorFragment : Fragment(), OnBackPressedListener {
    override fun onBackPressed(): Boolean {
        // Este fragmento también podría manejar onBackPressed de manera personalizada
        // Si este fragmento no necesita manejar el evento, simplemente devuelve false
        mostrarFragmento(MenorInicioFragment())
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mensajes_menor, container, false)
    }

    private fun mostrarFragmento(nuevoFragmento: Fragment) {
        val transaccion = fragmentManager?.beginTransaction()
        transaccion?.replace(R.id.menorFragmentContainer, nuevoFragmento)
        transaccion?.addToBackStack(null)
        transaccion?.commit()
    }
}