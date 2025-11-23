package com.example.medisupplyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import kotlin.getValue
import com.example.medisupplyapp.screen.auth.RegisterViewModel

class MainActivity : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        setContent {
            AppNavigation(registerViewModel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val place = Autocomplete.getPlaceFromIntent(data)
            val address = place.address ?: ""
            val lat = place.latLng?.latitude ?: 0.0
            val lng = place.latLng?.longitude ?: 0.0

            registerViewModel.updateAddress(address, lat, lng)
        }
    }
}
