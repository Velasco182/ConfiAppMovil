package com.example.pruebaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebaapp.databinding.CardViewNotificacionesItemGridBinding
import com.example.pruebaapp.models.NotificacionesItem

class NotificacionesAdapter (
    private var notificaciones: List<NotificacionesItem> = emptyList(),
    private var addComment: (String) -> Unit
) : RecyclerView.Adapter<NotificacionesAdapter.NotificacionesHolder>(){

    fun update(newDataNotificaciones: List<NotificacionesItem>){
        notificaciones = newDataNotificaciones
        notifyDataSetChanged()
    }
    inner class NotificacionesHolder(var binding: CardViewNotificacionesItemGridBinding):
    RecyclerView.ViewHolder(binding.root){

        fun render(notificacionesItem: NotificacionesItem, addComment: (String) -> Unit){
            binding.apply {

                menorImagenNotificaciones.setImageResource(notificacionesItem.menorImagenNotificaciones)
                nombreMenorNotificaciones.text = notificacionesItem.nombreMenorNotificaciones
                mensajeNotificaciones.text = notificacionesItem.mensajeNotificaciones
                numeroRutaNotificaciones.text = notificacionesItem.numeroRutaNotificaciones

                descartarNotificacionButton.setOnClickListener{

                    val numero = numeroRutaNotificaciones.text.toString()
                    addComment(numero)

                }

                llamarMenorNotificacionesButton.setOnClickListener {

                    val nombre = nombreMenorNotificaciones.text.toString()
                    addComment(nombre)

                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionesHolder {
        return  NotificacionesHolder(
            CardViewNotificacionesItemGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = notificaciones.size

    override fun onBindViewHolder(holder: NotificacionesAdapter.NotificacionesHolder, position: Int) {
        holder.render(notificaciones[position], addComment)
    }
}