package com.example.pruebaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebaapp.R
import com.example.pruebaapp.adapters.NotificacionesAdapter
import com.example.pruebaapp.databinding.FragmentNotificacionesBinding
import com.example.pruebaapp.models.NotificacionesItem

class NotificacionesFragment : Fragment() {

    private lateinit var binding: FragmentNotificacionesBinding
    private lateinit var notificacionesAdapter: NotificacionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificacionesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        notificacionesAdapter = NotificacionesAdapter(addComment = {data -> addCommentFunction(data)})
        initList()
        loadData()
    }

    private fun addCommentFunction(data: String) {

        Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()

    }

    private fun initList(){

        binding.rcvNotificaciones.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificacionesAdapter
        }
    }

    private fun loadData(){
        val notificacionesList = listOf(

            NotificacionesItem(1, R.drawable.thumbnail, "Pepito", "Desvío en campanario", "Ruta: 1"),
            NotificacionesItem(2, R.drawable.thumbnail, "Juanito", "Desvío en el Sena", "Ruta: 2")

        )

        notificacionesAdapter.update(notificacionesList)

    }

}