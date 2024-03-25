package com.example.confiapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.confiapp.apiservice.ConfiAppApiClient
import com.example.confiapp.apiservice.ConfiAppApiManager
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.controllers.LoginActivity
import com.example.confiapp.databinding.FragmentRegistroBinding
import com.example.confiapp.models.TutorItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val contrasena = binding.confirmarCInput.text.toString()

        //val data = TutorItem(nombre, apellido, email, telefono, password, "cedula", nidentificacion)

        if (password == contrasena){

            val data = TutorItem(
                nombre,
                apellido,
                email,
                telefono,
                password,
                "cedula",
                nidentificacion
            )

            // Ahora llamamos a insertarDatos con un callback
            apiManager.insertarDatos(data, object : Callback<ConfiAppApiService.ResponseApi> {
                override fun onResponse(
                    call: Call<ConfiAppApiService.ResponseApi>,
                    response: Response<ConfiAppApiService.ResponseApi>
                ) {
                    if (response.isSuccessful) {
                        // Si la respuesta es exitosa
                        val result = response.body()
                        Toast.makeText(
                            requireContext(),
                            "Datos insertados correctamente: $result",
                            Toast.LENGTH_SHORT
                        ).show()

                        activity?.runOnUiThread {
                            // Tu código para limpiar y navegar aquí

                            binding.nombreInput.text?.clear()
                            binding.apellidoInput.text?.clear()
                            binding.numeroDocumentoInput.text?.clear()
                            binding.edadInput.text?.clear()
                            binding.telefonoInput.text?.clear()
                            binding.correoInput.text?.clear()
                            binding.contrasenaInput.text?.clear()
                            binding.confirmarCInput.text?.clear()

                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                    } else {
                        // Si obtenemos una respuesta del servidor pero no es exitosa (p.ej., error 404, 500)
                        Toast.makeText(
                            requireContext(),
                            "Respuesta no exitosa: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ConfiAppApiService.ResponseApi>, t: Throwable) {
                    // Error al realizar la llamada (p.ej., sin conexión)
                    Toast.makeText(
                        requireContext(),
                        "Error al insertar datos: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }else{
            Toast.makeText(
                requireContext(),
                "Los campos de contraseña deben coincidir",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}