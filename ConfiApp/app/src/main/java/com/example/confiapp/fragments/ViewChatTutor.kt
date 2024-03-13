    package com.example.confiapp.fragments

    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import com.example.confiapp.R
    import com.example.confiapp.adapters.adaptersChat.ChatAdapter
    import com.example.confiapp.databinding.FragmentNotificacionesBinding
    import com.example.confiapp.databinding.ViewChatTutorBinding
    import com.example.confiapp.models.RetrofitInstance
    import com.google.ai.client.generativeai.Chat
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.FirebaseUser
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import com.google.gson.Gson
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import java.lang.Exception

    class ViewChatTutor : Fragment(R.layout.view_chat_tutor){

        private lateinit var binding: ViewChatTutorBinding
        private lateinit var firebaseUser: FirebaseUser
        private lateinit var chatList: MutableList<Chat>
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = ViewChatTutorBinding.bind(view)

            iniUI()



        }

        private fun iniUI() {
            firebaseUser = FirebaseAuth.getInstance().currentUser!!

            setupListener()
        }

        fun readMessage(senderId: String, receiverId: String) {
            val databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Chat")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (dataSnapShot: DataSnapshot in snapshot.children) {
                        val chat = dataSnapShot.getValue(Chat::class.java)

                        if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                            chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                        ) {
                            chatList.add(chat)
                        }
                    }

                    val chatAdapter = ChatAdapter(requireContext(), chatList)

                    binding.chatRecyclerView.adapter = chatAdapter
                }
            })
        }

        private fun setupListener() {
            binding.btnSendMessage.setOnClickListener {
                var message: String = binding.edtMessages.text.toString()

                if (message.isEmpty()) {
                    Toast.makeText(requireContext(), "message is empty", Toast.LENGTH_SHORT).show()
                    binding.edtMessages.setText("")
                } else {
                    sendMessage(firebaseUser!!.uid, userId, message)
                    binding.edtMessages.setText("")
                    topic = "/topics/$userId"
                    PushNotification(NotificationData( userName!!,message),
                        topic).also {
                        sendNotification(it)
                    }

                }
            }
        }

        private fun sendMessage(senderId: String, receiverId: String, message: String) {
            var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

            var hashMap: HashMap<String, String> = HashMap()
            hashMap["senderId"] = senderId
            hashMap["receiverId"] = receiverId
            hashMap["message"] = message

            reference!!.child("Chat").push().setValue(hashMap)
        }

        private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if(response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                }
            } catch(e: Exception) {
                Log.e("TAG", e.toString())
            }
        }

    }