package com.example.pruebaapp.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pruebaapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() , OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    /*Dependencias para usar maps en el build.gradle(Project:pruebaApp*/
    //implementation ("com.google.android.gms:play-services-maps:18.2.0")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {

        if (map != null) {

            googleMap = map

            // Mover la cámara a una ubicación específica (por ejemplo, latitud y longitud)
            val location = LatLng(37.09024, -95.712891)
            googleMap.addMarker(MarkerOptions().position(location).title("Robert Downey Sr. (2022)"))

            // Agregar más marcadores según sea necesario
            val location1 = LatLng(23.634501, -102.552784)
            googleMap.addMarker(MarkerOptions().position(location1).title("Cartel Land (2015)"))

            val location2 = LatLng(35.86166, 104.195397)
            googleMap.addMarker(MarkerOptions().position(location2).title("Last Train Home (2009)"))

            val location3 = LatLng(-75.250973, -0.071389)
            googleMap.addMarker(MarkerOptions().position(location3).title("Antártida: Un mensaje de otro planeta (2021)"))

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location3, 1f))
        }
    }
}