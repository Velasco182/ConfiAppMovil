package com.example.pruebaapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebaapp.R
import com.example.pruebaapp.adapters.InicioAdapter
import com.example.pruebaapp.databinding.FragmentInicioBinding
import com.example.pruebaapp.models.InicioItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class InicioFragment : Fragment() {

    private lateinit var binding: FragmentInicioBinding
    private lateinit var inicioAdapter: InicioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment // Implemnetación de databinding en fragments
        binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.root

        val botonflotante = binding.floatingActionButton

        botonflotante.setOnClickListener {
            // Crea y muestra el diálogo cuando se hace clic en el botón
            val dialogFragment = MapsDialogFragment()
            dialogFragment.show(parentFragmentManager, "maps_dialog")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

    }

    private fun initUI() {
        inicioAdapter = InicioAdapter(addComment = {data -> addCommentFunction(data)})
        initList()
        loadData()
    }

    private fun addCommentFunction(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
    }

    private fun initList() {
        binding.recyclerInicio.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inicioAdapter
        }
    }

    private fun loadData() {
        val rutasList = listOf(
            InicioItem(1, R.drawable.thumbnail, "Pepito ", "10 mins",
                "San Eduardo", "Campanario", "4567", "10:00 PM"),
            InicioItem(1, R.drawable.thumbnail, "Juanito", "15 mins",
                "Centro", "Sena", "7890", "2:00 PM")

        )

        inicioAdapter.update(rutasList)
    }



    class MapsDialogFragment : DialogFragment() {

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
            return inflater.inflate(R.layout.crear_mapa_dialog_layout, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Crear un nuevo MapFragment
            val mapFragment = SupportMapFragment.newInstance()

            // Cargar el MapFragment en el FragmentContainerView
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit()

            // Notificar al MapFragment que está listo para inicializarse
            mapFragment.getMapAsync { googleMap ->
                // Aquí puedes configurar el mapa, añadir marcadores, etc.
                // Por ejemplo:
                val location = LatLng(2.4449261743007327, -76.6001259041013)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
            }

            val puntoA = view.findViewById<EditText>(R.id.puntoA)
            val puntoB = view.findViewById<EditText>(R.id.puntoB)

            val acceptButton = view.findViewById<Button>(R.id.guardarRutaButton)
            acceptButton.setOnClickListener {
                // Acción cuando se hace clic en Aceptar


                val puntoAA = puntoA.text.toString()
                val puntoBB = puntoB.text.toString()

                dismiss() // Cancela la acción
                Toast.makeText(requireContext(), "Ruta Creada", Toast.LENGTH_SHORT).show()
                //dismiss()  Cierra el diálogo
            }
        }
    }

}