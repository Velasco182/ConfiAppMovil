package com.example.pruebaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebaapp.databinding.CardViewItemGridBinding
import com.example.pruebaapp.models.NoticiasItem

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

                icons.setImageResource(noticiasItem.image)
                titulo.text = noticiasItem.name
                descripcion.text = noticiasItem.description

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