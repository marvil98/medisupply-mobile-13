package com.example.medisupplyapp.screen.visits

import androidx.lifecycle.ViewModel
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.model.RoutePoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DailyRouteVireModel : ViewModel() {

    private val _dailyRoute = MutableStateFlow<DailyRoute>( DailyRoute(0, emptyList()))
    val dailyRoute: StateFlow<DailyRoute> = _dailyRoute

    init {
        // Cargar rutas de
        val visits = listOf<RoutePoint>(
            RoutePoint("Carrera 50 # 8-15, Bogotá", 3, "4.58012000", "-74.10356000","Farmacia Los Olivos Principal"),
            RoutePoint("Carrera 15 # 82-45, Cali", 10, "3.44781000", "-76.51475000","Clínica Odontológica Sonrisa"),
            RoutePoint("Calle 90 # 19C-33, Barranquilla", 11, "10.99800000", "-74.80900000","Óptica Claridad Total"),
        )
        val mockRoutes = DailyRoute(3, visits)
        _dailyRoute.value  = mockRoutes
    }
}