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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoutesRepository(
        api = ApiConnection.api_routes,
        routeCacheDataStore = application.routeCacheDataStore
    )

    // Estado observable para la UI (opcional)
    private val _dailyRoute = MutableStateFlow<DailyRoute>( DailyRoute(0,visits = emptyList()))
    val dailyRoute: StateFlow<DailyRoute> = _dailyRoute

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
            val dailyRoute = repository.getDailyRoute()
            _dailyRoute.value = dailyRoute

            if (dailyRoute.visits.isNotEmpty()) {
                println("INFO: Se cargo la ruta diaria.")
            } else {
                println("INFO: No se cargaron clientes.")
            }
        }
    }
}
