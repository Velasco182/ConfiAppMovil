package com.example.confiapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.confiapp.R
import com.example.confiapp.controllers.MenorInicioActivity
import com.example.confiapp.databinding.FragmentMenorLoginBinding

class MenorLoginFragment : Fragment() {

    private lateinit var binding: FragmentMenorLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenorLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val buttonOpenActivity = binding.menorLoginButton

        buttonOpenActivity.setOnClickListener {
            val intent = Intent(activity, MenorInicioActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}