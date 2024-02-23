package com.example.pruebaapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
//import androidx.databinding.DataBindingUtil
import com.example.pruebaapp.controllers.MainActivity
import com.example.pruebaapp.controllers.RegistroRContrasenaActivity
import com.example.pruebaapp.data.SharedPreferencesManager
import com.example.pruebaapp.databinding.FragmentLoginBinding

///Binding: Primero modficar el bluid.gradle (Module:app) y dentro de android  al final buildFeatures {
//        viewBinding true
//    }
//} private lateinit var binding: ResultProfileBinding
class LoginFragment : Fragment() {
    //Binding en fragments
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    ///sharedPreferences
    private lateinit var sharedPre: SharedPreferencesManager

    lateinit var  buttonOpenActivity : Button
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*by lazy {
            DataBindingUtil.inflate(LayoutInflater, R.layout.fragment_login, )
        }*/

        // ---> Para activities se usa así
        sharedPre = SharedPreferencesManager(requireContext())
        val userrrr = sharedPre.getUser()
        val boolll = sharedPre.getBool()

        Toast.makeText(activity, boolll.toString(), Toast.LENGTH_SHORT).show()
        Toast.makeText(activity, userrrr, Toast.LENGTH_SHORT).show()


        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //val view = DataBindingUtil.inflate(R.layout.fragment_login, container, false)

        val buttonOpenRegisterActivity = binding.registroButton

        val user = binding.userInput
        val pass = binding.passInput

         buttonOpenActivity = binding.inicioButton

        buttonOpenActivity.setOnClickListener {
            val u = user.text.toString()
            val p = pass.text.toString()

            // ---> Guardar datos del usuario con SharedPreferences para Activities
            sharedPre.saveUser(u, p)
            sharedPre.saveBool()

            validar(u, p)
        }

        buttonOpenRegisterActivity.setOnClickListener {
            val intent1 = Intent(activity, RegistroRContrasenaActivity::class.java)
            startActivity(intent1)
        }

        return binding.root
    }
    fun validar(u: String, p: String) {

        if (u == sharedPre.getUser() && p == sharedPre.getPass() ) {

            val intent = Intent(activity, MainActivity::class.java)
            //intent.putExtra("user", u)
            startActivity(intent)

        }else{

            Toast.makeText(activity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();

        }

    }

}
