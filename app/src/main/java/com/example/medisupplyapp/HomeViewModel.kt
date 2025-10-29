package com.example.medisupplyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeViewModel : ViewModel() {

    private val repository = RoutesRepository(api = ApiConnection.api_routes)

    // Estado observable para la UI (opcional)
    private val _dailyRoute = MutableStateFlow<DailyRoute>( DailyRoute(0, emptyList()))
    val dailyRoute: StateFlow<DailyRoute> = _dailyRoute


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "api_cache")
    private val VISITS_MADE_CACHE_KEY = stringPreferencesKey("visits_made")

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
