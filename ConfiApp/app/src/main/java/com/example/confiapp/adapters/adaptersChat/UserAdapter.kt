package com.example.confiapp.adapters.adaptersChat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.confiapp.R
import com.example.confiapp.fragments.NotificacionesFragment
import com.google.firebase.firestore.auth.User

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.userName

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, NotificacionesFragment::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("userName", user.userName)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
        // Agrega aquí el código para el ImageView si es necesario
    }
}