package com.example.confiapp.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.confiapp.R
import com.example.confiapp.data.SharedPreferencesManager
import io.socket.client.IO
import io.socket.client.Socket

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferencesManager

    private lateinit var mSocket: Socket

    private val SPLASH_TIME_OUT: Long = 1500 // 1,5 segundos de demora
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val opts = IO.Options()
        opts.reconnectionAttempts = 10
        mSocket = IO.socket("ws://localhost:3000", opts)
        mSocket.connect()

        sharedPref = SharedPreferencesManager(this)

        ///Hacemos la vcalidacion boolean para inicio de sesion
        Handler().postDelayed({
            if (userLog()){
                // Este código se ejecutará después de SPLASH_TIME_OUT
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                val intent1 = Intent(this, LoginActivity::class.java)
                startActivity(intent1)

            }

        }, SPLASH_TIME_OUT);
    }

    private fun userLog(): Boolean{
        return sharedPref.getBool()
    }
}