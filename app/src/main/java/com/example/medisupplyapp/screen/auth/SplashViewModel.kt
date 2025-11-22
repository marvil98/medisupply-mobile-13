package com.example.medisupplyapp.screen.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.launch


// ViewModel para el Splash
class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ClientRepository(
        api = ApiConnection.api_users,
        context = application
    )

    suspend fun checkSession(): Boolean {
        return repository.isSessionValid()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}