package com.example.confiapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.confiapp.databinding.CardViewInicioItemGridBinding
import com.example.confiapp.models.InicioItem
import com.squareup.picasso.Picasso

class InicioAdapter(
    private var rutas: List<InicioItem> = emptyList(),
    private var addComment: (String) -> Unit
) : RecyclerView.Adapter<InicioAdapter.InicioHolder>() {

    fun update(newDataInicio: List<InicioItem>) {
        rutas = newDataInicio
        notifyDataSetChanged()
    }

    inner class InicioHolder(var binding: CardViewInicioItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun render(inicioItem: InicioItem, addComment: (String) -> Unit) {
            binding.apply {

                Picasso.get()
                    .load(inicioItem.menorImage)
                    .into(menorImage)
                //menorImage.setImageResource(inicioItem.menorImage)
                menorName.text = inicioItem.menorName
                estimadoRuta.text = inicioItem.estimadoRuta
                pInicioRuta.text = inicioItem.inicioRuta
                pDestinoRuta.text = inicioItem.destinoRuta
                numeroRuta.text = inicioItem.numeroRuta
                horaRuta.text = inicioItem.horaRuta

                cardRuta.setOnClickListener {

                    val numero = numeroRuta.text.toString()

                    addComment(numero)

                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InicioHolder {
        return InicioHolder(
            CardViewInicioItemGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = rutas.size

    override fun onBindViewHolder(holder: InicioHolder, position: Int) {
        holder.render(rutas[position], addComment)
    }

}