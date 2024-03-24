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
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.confiapp.R
import com.example.confiapp.databinding.EditarPerfilDialogLayoutBinding
import com.example.confiapp.databinding.RegistroMenorDialogLayoutBinding

class EditarPerfilDialogFragment : DialogFragment() {

    private  lateinit var binding: EditarPerfilDialogLayoutBinding
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

        val nombreTutorInputE = binding.nombreTutorInputE
        val apellidoTutorInputE = binding.apellidoTutorInputE

        /*///Configuración Spinner
        val tipoDocumentoMenor = view.findViewById<Spinner>(R.id.tipoDocumentoMenorList)
        // Datos de ejemplo para el Spinner
        val items = arrayOf("Tarjeta de Identidad", "Registro Civil")
        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        // Asignar el adaptador al Spinner
        tipoDocumentoMenor.adapter = adapter*/

        val numeroDocumentoTutorInputE = binding.numeroDocumentoTutorInputE
        val edadTutorInputE = binding.edadTutorInputE
        val telefonoTutorInputE = binding.telefonoTutorInputE
        val correoTutorInputE = binding.correoTutorInputE

        //val confirmarRegistroMenor = dialogLayout.findViewById<EditText>(R.id.confirmarRegistroMenorButton)

        val acceptButton = binding.guardarDatosButton
        acceptButton.setOnClickListener {
            // Acción cuando se hace clic en Aceptar


            val nombreM = nombreTutorInputE.text.toString()
            val apellidoM = apellidoTutorInputE.text.toString()


            val numeroDocumentoM = numeroDocumentoTutorInputE.text.toString()
            val edadM = edadTutorInputE.text.toString()
            val telefonoM = telefonoTutorInputE.text.toString()
            val correoM = correoTutorInputE.text.toString()

            dismiss() // Cancela la acción
            Toast.makeText(context, "Menor Guardado", Toast.LENGTH_SHORT).show()
            //dismiss()  Cierra el diálogo
        }
    }
}