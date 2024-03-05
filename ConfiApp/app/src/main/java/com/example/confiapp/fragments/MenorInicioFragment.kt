package com.example.confiapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.example.confiapp.R
import com.example.confiapp.databinding.FragmentMenorInicioBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class MenorInicioFragment : Fragment(R.layout.fragment_menor_inicio), OnBackPressedListener, OnMapReadyCallback {
    override fun onBackPressed(): Boolean {
        // Aquí, implementa lo que quieras hacer cuando se presiona atrás
        // y devuelve true si quieres que este manejo sea el final (no quiere que se llame al comportamiento por defecto del sistema)
        // Por ejemplo, para volver al fragmento anterior podrías hacer una transacción de fragmento aquí
        // y luego devolver true.
        return false
    }

    private lateinit var binding: FragmentMenorInicioBinding
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenorInicioBinding.bind(view)

        val llamarButton = binding.llamarMenorButton
        val mensajeButton = binding.mensajeMenorButton

        val fragmentMensajes = MensajesMenorFragment()

        mensajeButton.setOnClickListener {
            mostrarFragmento(fragmentMensajes)
        }

        // Esto llamará a `onMapReady` cuando el mapa esté listo para usarse.
        mapView = binding.menorMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    private fun mostrarFragmento(nuevoFragmento: Fragment) {
        val transaccion = fragmentManager?.beginTransaction()
        transaccion?.replace(R.id.menorFragmentContainer, nuevoFragmento)
        transaccion?.addToBackStack(null)
        transaccion?.commit()
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map
        val location = LatLng(2.4449261743007327, -76.6001259041013)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))

    }

    // Asegúrate de sobrescribir y llamar a los métodos del ciclo de vida del MapView
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}