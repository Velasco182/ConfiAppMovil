package com.example.confiapp.fragments

import android.os.Bundle
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
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.databinding.FragmentRegistroBinding
import com.example.confiapp.models.TutorItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding
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

        val spinner = binding.tipoDocumentoList

        // Datos de ejemplo para el Spinner
        val items = arrayOf("Cédula de Ciudadanía", "Cédula de Extrajería", "Pasaporte")

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Establecer el diseño del adaptador para el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        spinner.adapter = adapter



        val btnRegistro = binding.confirmarRegistroButton

        btnRegistro.setOnClickListener {

            var datosAEnviar : TutorItem
            var sp : String

            val nombre = binding.nombreInput.text.toString()
            val apellido = binding.apellidoInput.text.toString()
            val email = binding.correoInput.text.toString()
            val telefono = binding.telefonoInput.text.toString()
            val password = binding.contrasenaInput.text.toString()
            val nidentificacion = binding.numeroDocumentoInput.text.toString()

            // Manejar eventos de selección (igual que en el ejemplo anterior)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = items[position]
                    sp = selectedItem
                    Toast.makeText(requireContext(), "Seleccionaste: $selectedItem", Toast.LENGTH_SHORT).show()

                    datosAEnviar = TutorItem(id = 1, nombre, apellido, edad ="20", nacionalidad = "Colombiano", direccion = "Calle13", profesion = "Profesor", email, telefono.toInt(), password, sp, nidentificacion)
                    lifecycleScope.launch {
                        registrar(datosAEnviar)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Acción a realizar cuando no se selecciona ningún elemento
                }
            }


        }

        return view
    }

    fun registrar(tutor : TutorItem){

        //Retofit
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://nuevomern.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofitBuilder.create(ConfiAppApiService::class.java)

        try{

            val call = service.postTutor(tutor)

            if (call.isSuccessful){

                val tutor = call.body()
                //var listaTutores = tutor!!.results

                //binding.nombreInput.text = tutor.nombres
            //binding.correo.text = tutor?.email

                }else{

                }
        } catch (e: Exception){

        }

    }



}