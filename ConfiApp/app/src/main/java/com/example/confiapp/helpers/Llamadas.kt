package com.example.confiapp.helpers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object Llamadas{

    private const val REQUEST_PHONE_CALL = 1

    fun makePhoneCall(fragmentOrActivity: Any, phoneNumber: String) {
        when (fragmentOrActivity) {
            is Fragment -> {
                val context = fragmentOrActivity.requireContext()
                if (checkPermission(context)) {
                    startPhoneCall(context, phoneNumber)
                } else {
                    requestPermission(fragmentOrActivity)
                }
            }
            is FragmentActivity -> {
                val context = fragmentOrActivity
                if (checkPermission(context)) {
                    startPhoneCall(context, phoneNumber)
                } else {
                    requestPermission(fragmentOrActivity)
                }
            }
            else -> throw IllegalArgumentException("El argumento debe ser un Fragment o FragmentActivity")
        }
    }

    private fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
    }

    private fun requestPermission(activity: FragmentActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CALL_PHONE),
            REQUEST_PHONE_CALL
        )
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_PHONE_CALL && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido, realizar la llamada
            // Puedes manejar la lógica aquí o llamar a makePhoneCall() nuevamente
        }
    }

    private fun startPhoneCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

}