package com.example.confiapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.confiapp.R
import com.example.confiapp.adapters.NotificacionesAdapter
import com.example.confiapp.databinding.FragmentNotificacionesBinding
import com.example.confiapp.models.NotificacionesItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificacionesFragment : Fragment() {

    private lateinit var binding: FragmentNotificacionesBinding
    private lateinit var notificacionesAdapter: NotificacionesAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUserId: String
    private lateinit var chatRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""
        databaseReference = FirebaseDatabase.getInstance().reference
        chatRef = FirebaseDatabase.getInstance().reference.child("chats")

        initUI()
    }

    private fun initUI() {
        notificacionesAdapter = NotificacionesAdapter { data ->
            Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
        }

        binding.rcvNotificaciones.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificacionesAdapter
        }

        loadData()
    }

    private fun loadData() {
        val notificacionesList = listOf(
            NotificacionesItem(1, R.drawable.thumbnail, "Pepito", "Desvío en campanario", "Ruta: 1"),
            NotificacionesItem(2, R.drawable.thumbnail, "Juanito", "Desvío en el Sena", "Ruta: 2")
        )

        notificacionesAdapter.update(notificacionesList)
    }
}
