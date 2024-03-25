package com.example.confiapp.fragments.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.confiapp.R
import com.example.confiapp.apiservice.ConfiAppApiClient
import com.example.confiapp.apiservice.ConfiAppApiManager
import com.example.confiapp.data.SharedPreferencesManager
import com.example.confiapp.databinding.EditarPerfilDialogLayoutBinding
import com.example.confiapp.databinding.RegistroMenorDialogLayoutBinding
import com.example.confiapp.models.TutorItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPerfilDialogFragment : DialogFragment() {

    private  lateinit var binding: EditarPerfilDialogLayoutBinding

    private lateinit var apiManager: ConfiAppApiManager

    private lateinit var sharedPre: SharedPreferencesManager
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.CustomDialog // Opcional: para animaciones personalizadas
        return dialog
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = EditarPerfilDialogLayoutBinding.inflate(inflater, container, false)

        val cerrar = binding.cerrarVentana
        cerrar.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = EditarPerfilDialogLayoutBinding.bind(view)

        //SharedPreferences
        sharedPre = SharedPreferencesManager(requireContext())

        //Retofit
        apiManager = ConfiAppApiManager(ConfiAppApiClient.createApiService())

        userProfile()

        //val confirmarRegistroMenor = dialogLayout.findViewById<EditText>(R.id.confirmarRegistroMenorButton)

        val acceptButton = binding.guardarDatosButton
        acceptButton.setOnClickListener {
            // Acción cuando se hace clic en Aceptar


            /*val nombreM = nombreTutorInputE.toString()
            val apellidoM = apellidoTutorInputE.toString()


            val numeroDocumentoM = numeroDocumentoTutorInputE.toString()
            val edadM = edadTutorInputE.toString()
            val telefonoM = telefonoTutorInputE.toString()
            val correoM = correoTutorInputE.toString()*/

            dismiss() // Cancela la acción
            Toast.makeText(context, "Menor Guardado", Toast.LENGTH_SHORT).show()
            //dismiss()  Cierra el diálogo
        }
    }

    fun userProfile(){

        val token = sharedPre.getToken()

        if (token != null) {

            apiManager.mostrarPerfil(token, object : Callback<TutorItem> {
                override fun onResponse(call: Call<TutorItem>, response: Response<TutorItem>) {
                    if (response.isSuccessful){
                        val userData = response.body()

                        Log.w("userData", "$userData")

                        val nombres = userData?.nombres
                        val apellidos = userData?.apellidos
                        val email = userData?.email
                        val telefono = userData?.telefono
                        val identificacion = userData?.numerodeIdentificacion

                        binding.nombreTutorInputE.setText(nombres)
                        binding.apellidoTutorInputE.setText(apellidos)

                        binding.numeroDocumentoTutorInputE.setText(identificacion)
                        //binding.edadTutorInputE.text =
                        binding.telefonoTutorInputE.setText(telefono)
                        binding.correoTutorInputE.setText(email)

                    }else{

                        Toast.makeText(requireContext(), "Token inválido", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<TutorItem>, t: Throwable) {
                    Log.w("userData", "Error: ${t.message}")
                }


            })

        }else{

            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()

        }

    }
}