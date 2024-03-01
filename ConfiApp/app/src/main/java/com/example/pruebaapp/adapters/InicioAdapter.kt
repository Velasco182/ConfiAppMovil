package com.example.pruebaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebaapp.databinding.CardViewInicioItemGridBinding
import com.example.pruebaapp.models.InicioItem

class InicioAdapter (
    private var rutas: List<InicioItem> = emptyList(),
    private var addComment: (String) -> Unit
) : RecyclerView.Adapter<InicioAdapter.InicioHolder>(){

    fun update(newDataInicio: List<InicioItem>){
        rutas = newDataInicio
        notifyDataSetChanged()
    }
    inner class InicioHolder(var binding: CardViewInicioItemGridBinding):
        RecyclerView.ViewHolder(binding.root){

        fun render(inicioItem: InicioItem, addComment: (String) -> Unit){
            binding.apply {

                menorImage.setImageResource(inicioItem.menorImage)
                menorName.text = inicioItem.menorName
                estimadoRuta.text = inicioItem.estimadoRuta
                pInicioRuta.text = inicioItem.inicioRuta
                pDestinoRuta.text = inicioItem.destinoRuta
                numeroRuta.text = inicioItem.numeroRuta
                horaRuta.text = inicioItem.horaRuta

                cardRuta.setOnClickListener{

                    val numero = numeroRuta.text.toString()

                    addComment(numero)

                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InicioHolder {
        return  InicioHolder(
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