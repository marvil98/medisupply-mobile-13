package com.example.medisupplyapp

import DailyRouteSerializer
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.RoutesRepository
import com.example.medisupplyapp.datastore.RouteCacheProto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


import com.example.medisupplyapp.data.provider.routeCacheDataStore
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoutesRepository(
        api = ApiConnection.api_routes,
        routeCacheDataStore = application.routeCacheDataStore
    )

    private val userRepository = ClientRepository(
        api = ApiConnection.api_users,
        application
    )

    // Estado observable para la UI (opcional)
    private val _dailyRoute = MutableStateFlow<DailyRoute>( DailyRoute(0,visits = emptyList()))
    val dailyRoute: StateFlow<DailyRoute> = _dailyRoute

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    private val _role = MutableStateFlow<String>("")
    val role: StateFlow<String> = _role

    private val _clientID = MutableStateFlow<Int>(1)
    val clientID: StateFlow<Int> = _clientID

    val visitsMade: StateFlow<Int> = repository.visitsMadeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0 // El valor inicial que quieres mostrar
        )
    init {
        // Se llama automáticamente al crear el ViewModel
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _userName.value = userRepository.getUserName()
            _role.value = userRepository.getUserRole()
            if(userRepository.getClientId() != null) {
                _clientID.value = userRepository.getClientId()!!
            }
            val sellerId = userRepository.getSellerId()

            when (_role.value) {
                "SELLER" -> loadSellerRoutes(sellerId!!)
            }

        }
    }

    private suspend fun loadSellerRoutes(sellerId: Int) {
        val dailyRoute = repository.getDailyRoute(sellerId)
        _dailyRoute.value = dailyRoute


        if (dailyRoute.visits.isNotEmpty()) {
            println("INFO: Se cargo la ruta diaria.")
        } else {
            println("INFO: No se cargaron clientes.")
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
