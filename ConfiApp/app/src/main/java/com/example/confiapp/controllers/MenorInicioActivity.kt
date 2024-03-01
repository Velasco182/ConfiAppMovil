package com.example.confiapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.confiapp.R
import com.example.confiapp.databinding.ActivityMenorInicioBinding
import com.example.confiapp.fragments.LoginFragment
import com.example.confiapp.fragments.MensajesMenorFragment
import com.example.confiapp.fragments.PerfilFragment

class MenorInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenorInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenorInicioBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        //val contenedor = binding.menorFragmentContainer
        val fragmentMensajes = MensajesMenorFragment()

        val llamarButton = binding.llamarMenorButton
        val mensajeButton = binding.mensajeMenorButton

        // Iniciar una transacción de fragmentos
        val transaction = supportFragmentManager.beginTransaction()

        mensajeButton.setOnClickListener {
            transaction.replace(R.id.menorFragmentContainer, fragmentMensajes)
            transaction.commit()

            val cardView = binding.buttonsCardView
            cardView.visibility = View.INVISIBLE
        }

        if (super.onBackPressed() ==){

        }

    }
}