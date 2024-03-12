package com.example.confiapp.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.confiapp.R
import com.example.confiapp.data.SharedPreferencesManager
import com.example.confiapp.databinding.ActivityMainBinding
import com.example.confiapp.fragments.InicioFragment
import com.example.confiapp.fragments.NoticiasFragment
import com.example.confiapp.fragments.NotificacionesFragment
import com.example.confiapp.fragments.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database

private const val NUM_PAGES = 4

class MainActivity : AppCompatActivity() {

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

        bottomNavigationView = binding.navigationBar
        viewPager = binding.viewPagerPrincipal
        viewPager.isUserInputEnabled = true

        Toast.makeText(this, "Bienvenido Tutor", Toast.LENGTH_SHORT).show()

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

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
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
        }else {
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

}