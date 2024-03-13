package com.example.confiapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.confiapp.R
import com.example.confiapp.adapters.adaptersChat.ChatAdapter
import com.example.confiapp.databinding.ActivityLoginBinding
import com.example.confiapp.databinding.FragmentNotificacionesBinding
import com.example.confiapp.models.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.ai.client.generativeai.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class NotificacionesFragment : Fragment(R.layout.fragment_notificaciones) {

    private lateinit var binding: FragmentNotificacionesBinding

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var chatList: MutableList<Chat>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificacionesBinding.bind(view)

        chatList = mutableListOf()

        binding.rcvNotificaciones.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)



        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.toString())




        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@NotificacionesFragment).load(user.profileImage).into(imgProfile)
                }
            }
        })



        readMessage(firebaseUser!!.uid, userId)
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error al leer mensajes: ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat?.senderId.equals(senderId) && chat?.receiverId.equals(receiverId) ||
                        chat?.senderId.equals(receiverId) && chat?.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat!!)
                    }
                }
                binding.rcvNotificaciones.adapter?.notifyDataSetChanged()
            }
        })
    }









}


}


