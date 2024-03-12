package com.example.confiapp.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.confiapp.R
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.apiservice.RouteResponse
import com.example.confiapp.data.SharedPreferencesManager
import com.example.confiapp.databinding.ActivityMainBinding
import com.example.confiapp.fragments.InicioFragment
import com.example.confiapp.fragments.NoticiasFragment
import com.example.confiapp.fragments.NotificacionesFragment
import com.example.confiapp.fragments.PerfilFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val NUM_PAGES = 4

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // CREAR VARIABLE PARA ALMACENAR EL MAPA CUANDO CARGUE

    private lateinit var map: GoogleMap
   // private lateinit var buttonRuta: Button

    private var star: String = ""
    private var end: String = ""

    var poly: Polyline? = null

    //lateinit var cadena: String;
    //var cadena2 = "";
    //val miConst = "Esto es una cadena.";
    private lateinit var sharedPre: SharedPreferencesManager

    // Write a message to the database
    val database = Firebase.database
    val myRef = database.getReference("message")


    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ScreenSlidePagerAdapter

    ///Binding, extendemos binding con ":" del activity, aparece automáticamente la clase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myRef.setValue("Hello, World!")
        ///Binding

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        ///Require context

        setContentView(binding.root)

        // Inflar el layout del diálogo
        val dialogView = layoutInflater.inflate(R.layout.crear_mapa_dialog_layout, null)

        bottomNavigationView = binding.navigationBar
        viewPager = binding.viewPagerPrincipal
        viewPager.isUserInputEnabled = true

        Toast.makeText(this, "Bienvenido Tutor", Toast.LENGTH_SHORT).show()

        val buttonRuta = dialogView.findViewById<Button>(R.id.guardarRutaButton)
        buttonRuta.setOnClickListener {
            star = ""
            end = ""
            poly?.remove()
            poly = null
            Toast.makeText(this, "Selecciona Punto de Origen y Final", Toast.LENGTH_SHORT).show()
            if (::map.isInitialized) {
                map.setOnMapClickListener {
                    if (star.isEmpty()) {
                        // "12345645678915"
                        star = "${it.longitude},${it.latitude} "
                    } else if (end.isNotEmpty()) {
                        star = "${it.longitude},${it.latitude} "
                        createRoute()
                    }
                }
            }
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.cardMapa) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //val valor = sharedPre.getUser()
        //intent.getBooleanExtra() Para pasar booleanos

        //val textView = binding.textView.setText(valor)

        //Toast.makeText(this, "Bienvenido ", Toast.LENGTH_SHORT).show();

        //findViewById<Button>(R.id.button_1).setOnClickListener{

        //val input = findViewById<TextInputEditText>(R.id.textInputEditText).text;

        //}

        adapter = ScreenSlidePagerAdapter(supportFragmentManager, lifecycle)

        //val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            //currentItem = 0
            when (item.itemId) {
                R.id.menu_item_inicio -> viewPager.setCurrentItem(0, true)
                R.id.menu_item_Noticias -> viewPager.setCurrentItem(1, true)
                R.id.menu_item_Notificaciones -> viewPager.setCurrentItem(2, true)
                R.id.menu_item_Perfil -> viewPager.setCurrentItem(3, true)
            }
            true
        }

        ///Para sincronizar el viewPager y el bottomNavigationView

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val menuItem = bottomNavigationView.menu.getItem(position)
                bottomNavigationView.selectedItemId = menuItem.itemId
            }
        })


        /*loadFragmentInicio(InicioFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_inicio -> {
                    // Lógica para la primera opción del menú
                    loadFragment(InicioFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_Noticias -> {
                    // Lógica para la segunda opción del menú
                    loadFragment(NoticiasFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_Notificaciones -> {
                    // Lógica para la segunda opción del menú
                    loadFragment(NotificacionesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_Perfil -> {
                    // Lógica para la segunda opción del menú
                    loadFragment(PerfilFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                // Agregar más casos según sea necesario
                else -> false
            }
        }*/

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("¿Cerrar la aplicación?")
            alertDialog.setMessage("¿Estás seguro de que quieres salir de la aplicación?")
            alertDialog.setPositiveButton("Sí") { _, _ ->
                super.onBackPressed()
                // Cierra la aplicación
                //android.os.Process.killProcess(android.os.Process.myPid())
                finishAffinity()
            }
            alertDialog.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Cancela la acción
            }
            alertDialog.show()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }

        /*private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainContainerView, fragment)
        //transaction.addToBackStack(null) // Opcional: para habilitar la navegación hacia atrás
        transaction.commit()
    }
    private fun loadFragmentInicio(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentMainContainerView, fragment)
        //transaction.addToBackStack(null) // Opcional: para habilitar la navegación hacia atrás
        transaction.commit()
    }*/


    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentManager, li: Lifecycle) :
        FragmentStateAdapter(fa, li) {
        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> InicioFragment()
                1 -> NoticiasFragment()
                2 -> NotificacionesFragment()
                3 -> PerfilFragment()
                // Agrega más fragments según sea necesario
                else -> InicioFragment()
                //else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    // Éste método se llama cuando el mapa se haya cargado
    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    private fun createRoute() {
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
        runOnUiThread {
            poly = map.addPolyline(polylineOptions)
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addCallAdapterFactory(GsonConverterFactory.create())
            .build()
    }

}

private fun Retrofit.Builder.addCallAdapterFactory(create: GsonConverterFactory): Retrofit.Builder {
    return addCallAdapterFactory(create)
}

private fun GoogleMap.addPolyline(polylineOptions: PolygonOptions): Polyline {
    return addPolyline(polylineOptions)
}


