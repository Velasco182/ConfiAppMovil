package com.example.confiapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.confiapp.R
import com.example.confiapp.adapters.NoticiasAdapter
import com.example.confiapp.apiservice.ConfiAppApiService
import com.example.confiapp.models.NoticiasRespuesta
import com.example.confiapp.databinding.FragmentNoticiasBinding
import com.example.confiapp.models.NoticiasItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NoticiasFragment : Fragment(R.layout.fragment_noticias) {

    private lateinit var binding: FragmentNoticiasBinding
    private lateinit var noticiasAdapter: NoticiasAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoticiasBinding.bind(view)

        initUI()

    }

    private fun initUI() {
        noticiasAdapter = NoticiasAdapter(addComment = { data -> addCommentFunction(data) })
        initList()
        loadData()
    }

    private fun addCommentFunction(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
    }


    private fun initList() {
        binding.rcvNoticias.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noticiasAdapter
        }
    }

    private fun loadData() {
        /*val listData = listOf(
            NoticiasItem(1, R.drawable.thumbnail, "Pepito Galindez", "Perdido el día 14 de febrero, visto por última vez en San Eduardo, usaba una camisa blanca, si lo haz visto deja tu comentario o comunícate al 3225421368"),
            NoticiasItem(2, R.drawable.thumbnail, "Juanito, Marce y Ana", "Última vez vistos juntos el sábado pasado en el parque central. Si tienes información sobre su paradero, por favor comunícate al 3225421368."),
            NoticiasItem(3, R.drawable.thumbnail, "Camila", "Desapareció el martes por la tarde cerca del colegio. Se le vio por última vez con uniforme escolar. Cualquier información, por favor contáctanos al 3225421368."),
            NoticiasItem(4, R.drawable.thumbnail, "Andres", "Desapareció el pasado domingo durante un paseo familiar en el bosque. Vestía pantalones cortos y una camiseta azul. Si tienes información, comunícate al 3225421368."),
            NoticiasItem(5, R.drawable.thumbnail, "Hermanos Gómez", "Los dos hermanos desaparecieron el viernes pasado después de salir de la escuela. Se les vio por última vez en la plaza del pueblo. Cualquier pista, por favor llamar al 3225421368."),
            NoticiasItem(6, R.drawable.thumbnail, "Juan y Beatriz", "Ambos desaparecieron hace tres días de su hogar. Se cree que salieron juntos. Si tienes alguna información, comunícate al 3225421368."),
            NoticiasItem(7, R.drawable.thumbnail, "Daniela", "Desapareció el lunes por la noche en el centro comercial. Vestía jeans y una chaqueta roja. Cualquier información, por favor contacta al 3225421368."),
            NoticiasItem(8, R.drawable.thumbnail, "Carlos", "Desapareció el miércoles por la tarde en el parque de diversiones. Se le vio por última vez cerca de los juegos mecánicos. Si lo has visto, por favor comunícate al 3225421368.")
        )

        noticiasAdapter.update(listData)*/

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://nuevomern-7y1b.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofitBuilder.create(ConfiAppApiService::class.java)

        val call = service.getNoticias() //Consulta el pokemon por ID

        call.enqueue(object : Callback<List<NoticiasItem>> {

            override fun onResponse(
                call: Call<List<NoticiasItem>>,
                response: Response<List<NoticiasItem>>
            ) {
                if (response.isSuccessful) {
                    val noticias: List<NoticiasItem>? = response.body()
                    if (noticias != null) {
                        noticiasAdapter.update(noticias)
                        //Toast.makeText(requireContext(), "El consumido es${response.body()}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "No hay noticias", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Manejar errores
                    Toast.makeText(requireContext(), "Errorr es$response", Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun onFailure(call: Call<List<NoticiasItem>>, t: Throwable) {
                //Manejar errores de la red
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                println(t)
            }

        })

    }


}