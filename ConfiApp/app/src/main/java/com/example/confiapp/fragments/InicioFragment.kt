package com.example.confiapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.confiapp.R
import com.example.confiapp.adapters.InicioAdapter
import com.example.confiapp.apiservice.ConfiAppApiClient
import com.example.confiapp.apiservice.ConfiAppApiManager
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.apiservice.googlemapsapi.DirectionsResponse
import com.example.confiapp.apiservice.googlemapsapi.DirectionsService
import com.example.confiapp.databinding.CrearMapaDialogLayoutBinding
import com.example.confiapp.databinding.FragmentInicioBinding
import com.example.confiapp.models.InicioItem
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class InicioFragment : Fragment() {

    private lateinit var binding: FragmentInicioBinding
    private lateinit var inicioAdapter: InicioAdapter

    private lateinit var apiManager: ConfiAppApiManager

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


            /*star = ""
            end = ""
            poly?.remove()
            poly = null
            Toast.makeText(
                requireContext(),
                "Selecciona Punto de Origen y Final",
                Toast.LENGTH_SHORT
            ).show()
            if (::map.isInitialized) {
                map.setOnMapClickListener {
                    if (star.isEmpty()) {
                        // "12345645678915"
                        star = "${it.longitude},${it.latitude} "
                    } else if (end.isNotEmpty()) {
                        end = "${it.longitude},${it.latitude} "

                    }
                }
            }*/

            // Cargar el MapFragment en el FragmentContainerView = SupportMapFragment.newInstance()
            /*childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, mapFragment)
                .commit()
            }


            mapFragment.getMapAsync(this)*/

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding = FragmentInicioBinding.bind(view)

        initUI()

    }

    private fun initUI() {
        inicioAdapter = InicioAdapter(addComment = { data -> addCommentFunction(data) })
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
        /*val rutasList = listOf(
            InicioItem(
                1, R.drawable.thumbnail, "Pepito ", "10 mins",
                "San Eduardo", "Campanario", "4567", "10:00 PM"
            ),
            InicioItem(
                1, R.drawable.thumbnail, "Juanito", "15 mins",
                "Centro", "Sena", "7890", "2:00 PM"
            )

        )*/

        // Creación del ApiService
        apiManager = ConfiAppApiManager(ConfiAppApiClient.createApiService())

        apiManager.mostrarHistorial(object : Callback<List<InicioItem>> {

            override fun onResponse(
                call: Call<List<InicioItem>>,
                response: Response<List<InicioItem>>
            ) {
                if (response.isSuccessful) {
                    val rutas: List<InicioItem>? = response.body()
                    if (rutas != null) {
                        inicioAdapter.update(rutas)

                        //val historial = Callback<List<ConfiAppApiService.HistorialResponse>>
                        //Toast.makeText(requireContext(), "El consumido es${response.body()}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No hay rutas", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Manejar errores
                    Toast.makeText(requireContext(), "Errorr es$response", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("Error historial", "$response")

                }
            }

            override fun onFailure(call: Call<List<InicioItem>>, t: Throwable) {
                //Manejar errores de la red
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                println(t)
            }

        })

        //inicioAdapter.update(rutasList)
    }


    class MapsDialogFragment : DialogFragment(), OnMapReadyCallback {
        private lateinit var binding: CrearMapaDialogLayoutBinding

        // CREAR VARIABLE PARA ALMACENAR EL MAPA CUANDO CARGUE
        private lateinit var map: GoogleMap
        private lateinit var placesClient: PlacesClient
        private lateinit var autoCompleteAdapterA: ArrayAdapter<String>
        private lateinit var autoCompleteAdapterB: ArrayAdapter<String>

        private var placeA: String? = null
        private var placeB: String? = null

        // Declarar variables para almacenar las coordenadas de origen y destino
        private var originCoordinates: LatLng? = null
        private var destinationCoordinates: LatLng? = null

        // Definir una longitud mínima de consulta para iniciar la búsqueda de autocompletado
        private var MIN_QUERY_LENGTH = 1


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)

            // Inicialización de Google Maps
            MapsInitializer.initialize(requireContext())

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.attributes?.windowAnimations =
                R.style.CustomDialog // Opcional: para animaciones personalizadas

            return dialog
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            binding = CrearMapaDialogLayoutBinding.inflate(inflater, container, false)

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding = CrearMapaDialogLayoutBinding.bind(view)

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.fragmentContainer) as SupportMapFragment
            mapFragment.getMapAsync(this)

            // Inicializa la API de Google Places
            Places.initialize(requireContext(), "AIzaSyAFwVvdV2JmsSzik6Dx5M17hoewBKEakoY")
            placesClient = Places.createClient(requireContext())

            val origin = binding.puntoA
            val destination = binding.puntoB


            // Inicializa el ArrayAdapter
            autoCompleteAdapterA =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)

            // Configura un TextWatcher en tu campo de entrada
            origin.setAdapter(autoCompleteAdapterA)
            origin.addTextChangedListener(object : TextWatcher {

                private var lastQuery = ""

                override fun afterTextChanged(s: Editable?) {

                    val newText = s.toString().trim()

                    if (newText == lastQuery) return  // Evita realizar consultas repetidas

                    lastQuery = newText

                    // Llama a la API de Autocompletado solo si el texto de entrada tiene longitud suficiente
                    if (newText.length >= MIN_QUERY_LENGTH) {

                        val request = FindAutocompletePredictionsRequest.builder()
                            // Establece la preferencia de ubicación en tu ciudad
                            .setLocationBias(
                                RectangularBounds.newInstance(
                                    LatLng(2.424, -76.629),
                                    LatLng(2.471, -76.580)
                                )
                            )
                            .setQuery(s.toString())
                            .build()

                        placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener { response ->

                                val prediction = response.autocompletePredictions.map {
                                    it.getFullText(null).toString()
                                }

                                //autoCompleteAdapterA.clear()
                                autoCompleteAdapterA.addAll(prediction)

                                for (predictions in response.autocompletePredictions) {
                                    Log.w("Punto A", predictions.getFullText(null).toString())
                                }

                            }.addOnFailureListener { exception ->
                                if (exception is ApiException) {
                                    Log.e(
                                        "Punto A",
                                        "Error al obtener las predicciones de autocompletado: ${exception.statusCode}"
                                    )
                                }
                            }
                    } else {
                        // Si el texto es demasiado corto, limpia la lista de sugerencias
                        autoCompleteAdapterA.clear()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            })

            // Inicializa el ArrayAdapter
            autoCompleteAdapterB =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)

            destination.setAdapter(autoCompleteAdapterB)
            destination.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {

                    // Llama a la API de Autocompletado cuando el usuario cambia el texto
                    val request = FindAutocompletePredictionsRequest.builder()
                        // Establece la preferencia de ubicación en tu ciudad
                        .setLocationBias(
                            RectangularBounds.newInstance(
                                LatLng(2.424, -76.629),
                                LatLng(2.471, -76.580)
                            )
                        )
                        .setQuery(s.toString())
                        .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->

                            val prediction = response.autocompletePredictions.map {
                                it.getFullText(null).toString()
                            }

                            //autoCompleteAdapter.clear()
                            autoCompleteAdapterB.addAll(prediction)

                            for (predictions in response.autocompletePredictions) {
                                Log.w("Punto B", predictions.getFullText(null).toString())
                            }

                        }.addOnFailureListener { exception ->
                            if (exception is ApiException) {
                                Log.e(
                                    "Punto B",
                                    "Error al obtener las predicciones de autocompletado: ${exception.statusCode}"
                                )
                            }
                        }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            })

            // Configurar el listener para el AutoCompleteTextView
            origin.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    // Obtener el texto seleccionado
                    placeA = parent.getItemAtPosition(position).toString()
                }

            // Configurar el listener para el AutoCompleteTextView
            destination.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    // Obtener el texto seleccionado
                    placeB = parent.getItemAtPosition(position).toString()
                }

            //Toast.makeText(requireContext(), "Ruta Creada, $puntoA", Toast.LENGTH_SHORT).show()


            val acceptButton = binding.guardarRutaButton
            acceptButton.setOnClickListener {

                val puntoA = origin.text.toString()
                val puntoB = destination.text.toString()

                val geocoder = Geocoder(requireContext())

                originCoordinates = geocoder.getFromLocationName(puntoA, 1)?.firstOrNull()?.let {
                    LatLng(it.latitude, it.longitude)
                }

                destinationCoordinates =
                    geocoder.getFromLocationName(puntoB, 1)?.firstOrNull()?.let {
                        LatLng(it.latitude, it.longitude)
                    }

                // Acción cuando se hace clic en Aceptar
                if (puntoA.isNotBlank() && puntoB.isNotBlank()) {


                    if (originCoordinates != null) {
                        destinationCoordinates?.let {
                            saveRouteToFirebase(
                                puntoA,
                                puntoB,
                                originCoordinates!!, it
                            )
                        }
                    }
                    searchRoute(puntoA, puntoB)
                }

                //
                Log.w("validación", "$placeA, $placeB")
                Toast.makeText(requireContext(), "Ruta Creada, $placeA", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Ruta Creada, $placeB", Toast.LENGTH_SHORT).show()

                //dismiss() // Cancela la acción

            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
        }

        private fun searchRoute(origin: String, destination: String) {

            Log.e("origin", originCoordinates.toString())
            Log.e("destination", destinationCoordinates.toString())

            if (originCoordinates != null && destinationCoordinates != null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(DirectionsService::class.java)

                val call = service.getDirections(
                    origin = "${originCoordinates!!.latitude},${originCoordinates!!.longitude}",
                    destination = "${destinationCoordinates!!.latitude},${destinationCoordinates!!.longitude}",
                    key = "AIzaSyAFwVvdV2JmsSzik6Dx5M17hoewBKEakoY"
                )

                call.enqueue(object : Callback<DirectionsResponse> {
                    override fun onResponse(
                        call: Call<DirectionsResponse>,
                        response: Response<DirectionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val route = response.body()?.routes?.firstOrNull()
                            if (route?.overviewPolyline != null) {
                                // Dibuja la ruta en el mapa
                                drawRoute(PolyUtil.decode(route.overviewPolyline.encodedPath ?: ""))

                                // Agrega marcadores en el inicio y final de la ruta
                                val originMarker =
                                    MarkerOptions().position(originCoordinates!!).title("Origen")
                                val destinationMarker = MarkerOptions().position(
                                    destinationCoordinates!!
                                ).title("Destino")
                                map.addMarker(originMarker)
                                map.addMarker(destinationMarker)

                                // Ajusta la cámara para que muestre toda la ruta
                                val boundsBuilder = LatLngBounds.builder()
                                boundsBuilder.include(originCoordinates!!)
                                boundsBuilder.include(destinationCoordinates!!)

                                val bounds = boundsBuilder.build()
                                val padding = 100 // Margen en píxeles alrededor de la ruta
                                val cameraUpdate =
                                    CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                map.animateCamera(cameraUpdate)

                                // Guardar la ruta en Firebase solo si se obtuvo una ruta válida
                                saveRouteToFirebase(
                                    origin,
                                    destination,
                                    originCoordinates!!,
                                    destinationCoordinates!!
                                )
                            } else {
                                // No se encontró una ruta
                                Toast.makeText(
                                    requireContext(),
                                    "No se encontró una ruta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Manejo de errores
                            Log.e("InicioFragment", "Error al obtener la ruta: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                        // Manejo de errores
                        Log.e("InicioFragment", "Error al obtener la ruta", t)
                    }
                })
            } else {
                // No se pudo geocodificar las direcciones
                Toast.makeText(
                    requireContext(),
                    "No se pudo geocodificar las direcciones",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun drawRoute(routePoints: List<LatLng>) {
            val polylineOptions = PolylineOptions()
                .addAll(routePoints)
                .color(Color.RED)
                .width(5f)

            val polyline = map.addPolyline(polylineOptions)
            polyline.isClickable = true
        }


        private fun saveRouteToFirebase(
            origin: String,
            destination: String,
            originCoordinates: LatLng,
            destinationCoordinates: LatLng
        ) {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("rutas")

            val nuevaRuta = mapOf<String, Any>(
                "origen" to origin,
                "destino" to destination,
                "originCoordinates" to originCoordinates.toString(), // Convertimos LatLng a String
                "destinationCoordinates" to destinationCoordinates.toString() // Convertimos LatLng a String
                // Aquí podemos agregar más campos de la ruta si es necesario
            )

            // Generar un ID único para la nueva ruta
            val nuevaRutaId = reference.push().key

            // Guardar la nueva ruta en la base de datos
            if (nuevaRutaId != null) {
                reference.child(nuevaRutaId).setValue(nuevaRuta)
                    .addOnSuccessListener {
                        // Ruta guardada exitosamente
                        Toast.makeText(
                            requireContext(),
                            "Ruta guardada exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        // Error al guardar la ruta
                        Toast.makeText(
                            requireContext(),
                            "Error al guardar la ruta: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

    }

}