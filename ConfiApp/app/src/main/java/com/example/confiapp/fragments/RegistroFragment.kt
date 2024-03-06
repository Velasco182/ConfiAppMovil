package com.example.confiapp.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.confiapp.R
import com.example.confiapp.apiservice.ConfiAppApiClient
import com.example.confiapp.apiservice.ConfiAppApiManager
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.databinding.FragmentRegistroBinding
import com.example.confiapp.models.TutorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding
    private lateinit var apiManager: ConfiAppApiManager
    //lateinit var spinner: Spinner

    // Datos de ejemplo para el Spinner
    val items = arrayOf("Cédula de Ciudadanía", "Cédula de Extrajería", "Pasaporte")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding = FragmentRegistroBinding.bind(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentRegistroBinding.inflate(inflater, container, false)
        val view = binding.root

        // Creación del ApiService
        apiManager = ConfiAppApiManager(ConfiAppApiClient.createApiService())

        val spinner = binding.tipoDocumentoList

        // Datos de ejemplo para el Spinner
        //val items = arrayOf("Cédula de Ciudadanía", "Cédula de Extrajería", "Pasaporte")

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Establecer el diseño del adaptador para el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        spinner.adapter = adapter

        // Manejar eventos de selección (igual que en el ejemplo anterior)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                //sp = selectedItem
                Toast.makeText(requireContext(), "Seleccionaste: $selectedItem", Toast.LENGTH_SHORT)
                    .show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acción a realizar cuando no se selecciona ningún elemento
            }
        }

        val btnRegistro = binding.confirmarRegistroButton

        btnRegistro.setOnClickListener {

            //Registrar usuario
            registrar()

        }

        return view
    }

    fun registrar() {

        //var sp: String

        val nombre = binding.nombreInput.text.toString()
        val apellido = binding.apellidoInput.text.toString()
        val email = binding.correoInput.text.toString()
        val telefono = binding.telefonoInput.text.toString()
        val password = binding.contrasenaInput.text.toString()
        val nidentificacion = binding.numeroDocumentoInput.text.toString()

        val data = TutorItem(
            nombres = nombre,
            apellidos = apellido,
            email = email,
            telefono = telefono,
            password = password,
            tipoIdentificacion = "cedula",
            numerodeIdentificacion = nidentificacion)

        // Llamamos a la función insertarDatos en ApiManager de forma asincrónica con GlobalScope
        lifecycleScope.launch(Dispatchers.Main){
            try {
                // Enviamos los datos al servidor
                val result = apiManager.insertarDatos(data)
                Log.e(TAG, "${result}", )

                Log.i(TAG, "Solicitud POST exitosa, ${result}")

                // Manejar respuesta exitosa, por ejemplo, mostrar un mensaje al usuario
                Toast.makeText(
                    requireContext(),
                    "Datos insertados correctamente",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Log.e(TAG, "Error al procesar la solicitud POST: ${e.message}")

                // Manejar errores, por ejemplo, mostrar un mensaje de error al usuario
                Toast.makeText(
                    requireContext(),
                    "Error al insertar datos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}