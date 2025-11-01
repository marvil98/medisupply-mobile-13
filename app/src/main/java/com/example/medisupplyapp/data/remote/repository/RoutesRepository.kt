package com.example.medisupplyapp.data.remote.repository

import DailyRouteSerializer.toDailyRoute
import DailyRouteSerializer.toRouteCacheProto
import androidx.datastore.core.DataStore
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.remote.api.RoutesApi
import com.example.medisupplyapp.datastore.DailyRouteProto
import com.example.medisupplyapp.datastore.RouteCacheProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.isInitialized
import kotlin.time.Duration.Companion.days

private val CACHE_EXPIRATION_MILLIS = 1.days.inWholeMilliseconds

class RoutesRepository(
    var api: RoutesApi,

    /**
     * Obtiene únicamente el número de visitas realizadas como un Flow.
     * Esto permite a la UI o al ViewModel reaccionar a los cambios en tiempo real.
     * @return Flow<Int> el número actual de visitas realizadas.
     */

    private val routeCacheDataStore: DataStore<RouteCacheProto>) {
    val visitsMadeFlow: Flow<Int> = routeCacheDataStore.data
        .map { cacheProto ->
            // Si el DailyRouteProto está inicializado, devuelve el valor.
            // Si no lo está (porque el caché está vacío), devuelve 0 como valor predeterminado.
            if (cacheProto.route.isInitialized) {
                cacheProto.route.visitsMade
            } else {
                0
            }
        }
    suspend fun getDailyRoute(): DailyRoute {
        val currentTime = System.currentTimeMillis()
        val cachedData = routeCacheDataStore.data.first()
        val isCacheValid = cachedData.route.isInitialized &&
                (currentTime - cachedData.timestamp < CACHE_EXPIRATION_MILLIS)
        if (isCacheValid && cachedData.route.visitsList.isNotEmpty()) {
            // CACHÉ VÁLIDA: Devolver los datos guardados
            println("Datos obtenidos desde la caché. Tiempo restante: ${(CACHE_EXPIRATION_MILLIS - (currentTime - cachedData.timestamp)) / 3600000} horas.")
            return cachedData.toDailyRoute()
        }

        try {
            val response = api.getSellerDailyRoute(1)

            if (response.isSuccessful) {
                val dailyRoute = response.body()

                if (dailyRoute != null) {
                    // 4. API EXITOSA: Guardar en DataStore y devolver
                    routeCacheDataStore.updateData {
                        // Guardar la nueva ruta con la marca de tiempo actual
                        dailyRoute.toRouteCacheProto(currentTime)
                    }
                    return dailyRoute
                }
            }

            // 5. API FALLIDA (respuesta no exitosa o body nulo): Devolver MOCK
            println("API falló o devolvió nulo. Devolviendo datos mock.")
            return DailyRoute(0, 0,emptyList())

        } catch (e: Exception) {
            // 6. ERROR DE CONEXIÓN: Devolver MOCK
            println("Error de conexión al API. Devolviendo datos mock.")
            return DailyRoute(0,0, emptyList())
        }
    }

    suspend fun updateVisitsMade(newVisitsMade: Int) {

        // El bloque updateData garantiza que la operación sea atómica (segura).
        routeCacheDataStore.updateData { currentCacheProto ->

            // 1. Verificación Inicial
            if (!currentCacheProto.route.isInitialized) {
                // Si la ruta no existe, devuelve el estado actual sin cambios.
                return@updateData currentCacheProto
            }

            val currentRouteProto = currentCacheProto.route
            val totalVisits = currentRouteProto.numberVisits // Obtener el límite superior

            // 2. Aplicar la Regla de Validación
            val finalVisitsMade = if (newVisitsMade <= totalVisits) {
                // Si el nuevo valor es menor o igual al total, usa el nuevo valor.
                newVisitsMade
            } else {
                // Si el nuevo valor excede el total, guárdalo como el total (máximo posible).
                totalVisits
            }

            // 3. Construir el nuevo DailyRouteProto con el valor validado
            val updatedRouteProto: DailyRouteProto = currentRouteProto.toBuilder()
                .setVisitsMade(finalVisitsMade) // <-- Usar el valor validado
                .setNumberVisits(totalVisits) // Mantener el total
                .addAllVisits(currentRouteProto.visitsList) // Mantener la lista de visitas
                .build()

            // 4. Construir y retornar el RouteCacheProto actualizado
            currentCacheProto.toBuilder()
                .setRoute(updatedRouteProto)
                .build()
        }
    }


}
