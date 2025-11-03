
package com.example.medisupplyapp.screen.visits

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.provider.routeCacheDataStore
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Sealed class para representar los diferentes estados
sealed class DailyRouteUiState {
    object Loading : DailyRouteUiState()
    data class Success(val dailyRoute: DailyRoute) : DailyRouteUiState()
    data class Error(val message: String) : DailyRouteUiState()
}

class DailyRouteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoutesRepository(
        api = ApiConnection.api_routes,
        routeCacheDataStore = application.routeCacheDataStore
    )

    private val _uiState = MutableStateFlow<DailyRouteUiState>(DailyRouteUiState.Loading)
    val uiState: StateFlow<DailyRouteUiState> = _uiState.asStateFlow()

    // Mantener el estado anterior para compatibilidad (opcional)
    private val _dailyRoute = MutableStateFlow(DailyRoute(0, 0, emptyList()))
    val dailyRoute: StateFlow<DailyRoute> = _dailyRoute.asStateFlow()

    init {
        loadDailyRoute()
    }

    private fun loadDailyRoute() {
        viewModelScope.launch {
            _uiState.value = DailyRouteUiState.Loading

            try {
                val dailyRoute = repository.getDailyRoute()
                _dailyRoute.value = dailyRoute
                _uiState.value = DailyRouteUiState.Success(dailyRoute)

                Log.d("DAILY_ROUTE", dailyRoute.visits.toString())

                if (dailyRoute.visits.isNotEmpty()) {
                    println("INFO: Se cargó la ruta diaria con ${dailyRoute.visits.size} puntos.")
                } else {
                    println("INFO: No se cargaron clientes.")
                }
            } catch (e: Exception) {
                Log.e("DAILY_ROUTE", "Error al cargar ruta: ${e.message}", e)
                _uiState.value = DailyRouteUiState.Error(
                    e.message ?: "Error desconocido al cargar la ruta"
                )
            }
        }
    }

    // Función para reintentar la carga
    fun retry() {
        loadDailyRoute()
    }
}