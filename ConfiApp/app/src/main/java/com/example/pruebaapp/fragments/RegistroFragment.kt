package com.example.pruebaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.example.pruebaapp.R

class RegistroFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registro, container, false)

        val spinner = view.findViewById<Spinner>(R.id.tipoDocumentoList)

        // Datos de ejemplo para el Spinner
        val items = arrayOf("Cédula de Ciudadanía", "Cédula de Extrajería", "Pasaporte")

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Establecer el diseño del adaptador para el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        spinner.adapter = adapter

        // Manejar eventos de selección (igual que en el ejemplo anterior)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                Toast.makeText(requireContext(), "Seleccionaste: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acción a realizar cuando no se selecciona ningún elemento
            }
        }

        return view
    }

}