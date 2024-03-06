package com.example.confiapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.confiapp.databinding.CardViewNotificacionesItemGridBinding
import com.example.confiapp.models.NotificacionesItem

class NotificacionesAdapter(
    private var notificaciones: List<NotificacionesItem> = emptyList(),
    private val addComment: (String) -> Unit
) : RecyclerView.Adapter<NotificacionesAdapter.NotificacionesHolder>() {

    fun update(newDataNotificaciones: List<NotificacionesItem>) {
        notificaciones = newDataNotificaciones
        notifyDataSetChanged()
    }

    inner class NotificacionesHolder(private val binding: CardViewNotificacionesItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun render(notificacionesItem: NotificacionesItem) {
            binding.apply {
                menorImagenNotificaciones.setImageResource(notificacionesItem.menorImagenNotificaciones)
                nombreMenorNotificaciones.text = notificacionesItem.nombreMenorNotificaciones
                mensajeNotificaciones.text = notificacionesItem.mensajeNotificaciones
                numeroRutaNotificaciones.text = notificacionesItem.numeroRutaNotificaciones

                descartarNotificacionButton.setOnClickListener {
                    addComment(numeroRutaNotificaciones.text.toString())
                }

                llamarMenorNotificacionesButton.setOnClickListener {
                    addComment(nombreMenorNotificaciones.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionesHolder {
        return NotificacionesHolder(
            CardViewNotificacionesItemGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = notificaciones.size

    override fun onBindViewHolder(holder: NotificacionesHolder, position: Int) {
        holder.render(notificaciones[position])
    }
}
