package com.example.confiapp.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.confiapp.controllers.LoginActivity
import com.example.confiapp.data.SharedPreferencesManager
import com.example.confiapp.databinding.FragmentPerfilBinding
import com.example.confiapp.fragments.dialogs.EditarPerfilDialogFragment
import com.example.confiapp.fragments.dialogs.RegistrarMenorDialogFragment

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding

    private lateinit var sharedPre: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment // Implemnetación de databinding en fragments
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val view = binding.root

        // ---> Para activities se usa así
        sharedPre = SharedPreferencesManager(requireContext())

        //Retofit

        val name = binding.tutorName
        val nombre = sharedPre.getUser()

        name.text = nombre

        val agregarMenor = binding.agregarMenor

        agregarMenor.setOnClickListener {
            // Crea y muestra el diálogo cuando se hace clic en el botón
            val dialogFragment = RegistrarMenorDialogFragment()
            dialogFragment.show(parentFragmentManager, "my_dialog")
        }

        val editarPerfil = binding.editarPerfilButton
        editarPerfil.setOnClickListener {
            val dialogFragmentEditarPerfil = EditarPerfilDialogFragment()
            dialogFragmentEditarPerfil.show(parentFragmentManager, "editar_perfil")
        }

        val cerrarSesion = binding.cerrarSesion
        cerrarSesion.setOnClickListener {

            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("¿Quieres cerrar Sesión?")
            alertDialog.setMessage("¿Estás seguro de que quieres salir de la aplicación?")

            alertDialog.setPositiveButton("Sí") { _, _ ->
                sharedPre.logOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)

            }

            alertDialog.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Cancela la acción
            }

            alertDialog.show()
    }

    return view
}

/*class MyDialogFragment : DialogFragment() {

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
        return inflater.inflate(R.layout.registro_menor_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreMenor = view.findViewById<EditText>(R.id.nombreMenorInput)
        val apellidoMenor = view.findViewById<EditText>(R.id.apellidoMenorInput)

        ///Configuración Spinner
        val tipoDocumentoMenor = view.findViewById<Spinner>(R.id.tipoDocumentoMenorList)
        // Datos de ejemplo para el Spinner
        val items = arrayOf("Tarjeta de Identidad", "Registro Civil")
        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        // Asignar el adaptador al Spinner
        tipoDocumentoMenor.adapter = adapter

        val numeroDocumentoMenor = view.findViewById<EditText>(R.id.numeroDocumentoMenorInput)
        val edadMenor = view.findViewById<EditText>(R.id.edadMenorInput)
        val telefonoMenor = view.findViewById<EditText>(R.id.telefonoMenorInput)
        val correoMenor = view.findViewById<EditText>(R.id.correoMenorInput)

        //val confirmarRegistroMenor = dialogLayout.findViewById<EditText>(R.id.confirmarRegistroMenorButton)

        val acceptButton = view.findViewById<Button>(R.id.acceptButton)
        acceptButton.setOnClickListener {
            // Acción cuando se hace clic en Aceptar


            val nombreM = nombreMenor.text.toString()
            val apellidoM = apellidoMenor.text.toString()


            val numeroDocumentoM = numeroDocumentoMenor.text.toString()
            val edadM = edadMenor.text.toString()
            val telefonoM = telefonoMenor.text.toString()
            val correoM = correoMenor.text.toString()

            dismiss() // Cancela la acción
            Toast.makeText(requireContext(), "Menor Guardado", Toast.LENGTH_SHORT).show()
            //dismiss()  Cierra el diálogo
        }
    }
}*/

}