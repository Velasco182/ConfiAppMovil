package com.example.confiapp.fragments

import android.app.Dialog
import android.app.FragmentManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.confiapp.R
import com.example.confiapp.adapters.InicioAdapter
import com.example.confiapp.apiservice.ConfiAppApiClient
import com.example.confiapp.apiservice.ConfiAppApiManager
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.databinding.CrearMapaDialogLayoutBinding
import com.example.confiapp.databinding.FragmentInicioBinding
import com.example.confiapp.models.InicioItem
import com.example.confiapp.models.NoticiasItem
import com.example.confiapp.models.RouteResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InicioFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentInicioBinding
    private lateinit var inicioAdapter: InicioAdapter

    private lateinit var apiManager: ConfiAppApiManager

    // CREAR VARIABLE PARA ALMACENAR EL MAPA CUANDO CARGUE
    private lateinit var map: GoogleMap

    // private lateinit var buttonRuta: Button

    private var star: String = ""
    private var end: String = ""

    var poly: Polyline? = null
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

            star = ""
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
            }

            val mapFragment = SupportMapFragment.newInstance()

            // Cargar el MapFragment en el FragmentContainerView
            childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, mapFragment)
                .commit()
            }


            mapFragment.getMapAsync(this)

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
                        //Toast.makeText(requireContext(), "El consumido es${response.body()}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No hay rutas", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Manejar errores
                    Toast.makeText(requireContext(), "Errorr es$response", Toast.LENGTH_SHORT)
                        .show()

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

    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    fun createRoute() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ConfiAppApiService::class.java)
                .getRoute("5b3ce3597851110001cf62482012481d44b14326904e25820054ec73", star, end)
            if (call.isSuccessful) {
                drawRoute(call.body())
            } else {
                Log.i("aris", "KO")
            }
        }
    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        val polylineOptions = PolygonOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polylineOptions.add(LatLng(it[1], it[0]))
        }

        activity?.runOnUiThread {
            poly = map.addPolyline(polylineOptions)
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addCallAdapterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun Retrofit.Builder.addCallAdapterFactory(create: GsonConverterFactory): Retrofit.Builder {
        return addCallAdapterFactory(create)
    }

    private fun GoogleMap.addPolyline(polylineOptions: PolygonOptions): Polyline {
        return addPolyline(polylineOptions)
    }


    class MapsDialogFragment : DialogFragment(){
        private lateinit var binding: CrearMapaDialogLayoutBinding

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)

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

            // Crear un nuevo MapFragment
            //val mapFragment = SupportMapFragment.newInstance()


            // Notificar al MapFragment que está listo para inicializarse
           // mapFragment.getMapAsync(this) //{ googleMap ->
                // Aquí puedes configurar el mapa, añadir marcadores, etc.
                // Por ejemplo:
                //val location = LatLng(2.4449261743007327, -76.6001259041013)
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
            //}

            val puntoA = binding.puntoA
            val puntoB = binding.puntoB

            val acceptButton = binding.guardarRutaButton
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