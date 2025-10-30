package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.model.RoutePoint
import com.example.medisupplyapp.data.remote.api.RoutesApi

class RoutesRepository(var api: RoutesApi) {
    suspend fun getDailyRoute(): DailyRoute {

        // TODO: remove for production

        val visits = listOf<RoutePoint>(
            RoutePoint("Carrera 50 # 8-15, Bogotá", 3, 4.58012000, "Farmacia Los Olivos Principal", "Carlos Preciado",-74.10356000),
            RoutePoint("Carrera 15 # 82-45, Cali", 10, 3.44781000, "Clínica Odontológica Sonrisa", "Rigoberto Hernandez",-76.51475000),
            RoutePoint("Calle 90 # 19C-33, Barranquilla", 11, 10.99800000, "Óptica Claridad Total", "Vladimir Ramírez", -74.80900000),
        )
        val mockRoutes = DailyRoute(3, visits)

        val response = api.getSellerDailyRoute(1)
        if (response.isSuccessful) {

            return response.body() ?: mockRoutes
        } else {
            return mockRoutes
            //throw Exception("Error al obtener rutas: ${response.code()}")
        }
    }
}
