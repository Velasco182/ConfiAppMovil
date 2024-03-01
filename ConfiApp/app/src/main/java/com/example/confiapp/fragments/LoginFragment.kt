package com.example.confiapp.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.confiapp.R
import com.example.confiapp.controllers.MainActivity
import com.example.confiapp.controllers.RegistroRContrasenaActivity
import com.example.confiapp.data.SharedPreferencesManager
import com.example.confiapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
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

    private lateinit var activity: Activity


    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    lateinit var  buttonOpenActivity : Button
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/
    //Firebase google auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        activity = requireActivity()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = auth.getCurrentUser()
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        ///No sé que poner aquí
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
            }
        }

    }

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

        oneTapClient = Identity.getSignInClient(requireActivity())

        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        val googleButton = binding.googleButton

        googleButton.setOnClickListener {

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
