package com.example.confiapp.fragments.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.confiapp.R
import com.example.confiapp.databinding.RegistroMenorDialogLayoutBinding

class RegistrarMenorDialogFragment : DialogFragment() {

    private lateinit var binding: RegistroMenorDialogLayoutBinding

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

        binding = RegistroMenorDialogLayoutBinding.inflate(inflater, container, false)

        val cerrar = binding.cerrarVentana
        cerrar.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = RegistroMenorDialogLayoutBinding.bind(view)

        val nombreMenor = binding.nombreMenorInput
        val apellidoMenor = binding.apellidoMenorInput

        ///Configuración Spinner
        val tipoDocumentoMenor = binding.tipoDocumentoMenorList
        // Datos de ejemplo para el Spinner
        val items = arrayOf("Tarjeta de Identidad", "Registro Civil")
        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        // Asignar el adaptador al Spinner
        tipoDocumentoMenor.adapter = adapter

        val numeroDocumentoMenor = binding.numeroDocumentoMenorInput
        val edadMenor = binding.edadMenorInput
        val telefonoMenor = binding.telefonoMenorInput
        val correoMenor = binding.correoMenorInput

        //val confirmarRegistroMenor = dialogLayout.findViewById<EditText>(R.id.confirmarRegistroMenorButton)

        val acceptButton = binding.acceptButton
        acceptButton.setOnClickListener {
            // Acción cuando se hace clic en Aceptar


            val nombreM = nombreMenor.text.toString()
            val apellidoM = apellidoMenor.text.toString()


            val numeroDocumentoM = numeroDocumentoMenor.text.toString()
            val edadM = edadMenor.text.toString()
            val telefonoM = telefonoMenor.text.toString()
            val correoM = correoMenor.text.toString()

            dismiss() // Cancela la acción
            Toast.makeText(context, "Menor Guardado", Toast.LENGTH_SHORT).show()
            //dismiss()  Cierra el diálogo
        }
    }
}