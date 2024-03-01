package com.example.pruebaapp.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.pruebaapp.R
//import androidx.databinding.DataBindingUtil
import com.example.pruebaapp.controllers.MainActivity
import com.example.pruebaapp.controllers.RegistroRContrasenaActivity
import com.example.pruebaapp.data.SharedPreferencesManager
import com.example.pruebaapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

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
    //Firebase google auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = auth.getCurrentUser()
        updateUI(currentUser);
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        ///No sé que poner aquí
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            updateUI(null)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Guardar datos google

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

        val inicioGoogle = binding.googleButton

        inicioGoogle.setOnClickListener {
            //configuración
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build()
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

    enum class ProviderType{
        BASIC,
        GOOGLE
    }

}
