package com.example.confiapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.confiapp.databinding.CardViewItemGridBinding
import com.example.confiapp.models.NoticiasItem
import com.squareup.picasso.Picasso

class NoticiasAdapter (
    private var noticias: List<NoticiasItem> = emptyList(),
    private var addComment: (String) -> Unit
) : RecyclerView.Adapter<NoticiasAdapter.NoticiasHolder>() {

    fun update(newData: List<NoticiasItem>) {
        noticias = newData
        notifyDataSetChanged()
    }

    inner class NoticiasHolder(var binding: CardViewItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun render(noticiasItem: NoticiasItem, addComment: (String) -> Unit) {
            binding.apply {

                Picasso.get()
                    .load(noticiasItem.foto)
                    .into(binding.icons)
                //icons.setImageResource(noticiasItem.image)
                titulo.text = noticiasItem.nombre
                descripcion.text = noticiasItem.descripcion

                button.setOnClickListener {

                    val editText = editText.text.toString()

                    if (editText.isNotEmpty()){

                        addComment(editText)

                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiasHolder {
        return NoticiasHolder(
            CardViewItemGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = noticias.size

    override fun onBindViewHolder(holder: NoticiasHolder, position: Int) {
        holder.render(noticias[position], addComment)
    }


}