package com.example.medisupplyapp.data.provider

import DailyRouteSerializer
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.medisupplyapp.data.serializers.AuthCacheSerializer
import com.example.medisupplyapp.datastore.AuthCacheProto
import com.example.medisupplyapp.datastore.RouteCacheProto

// La extensión del Context se define UNA SOLA VEZ aquí.
// Esto asegura que todos los componentes que accedan a esta propiedad usen la misma instancia de DataStore.
val Context.routeCacheDataStore: DataStore<RouteCacheProto> by dataStore(
    fileName = "daily_route.pb",
    serializer = DailyRouteSerializer
)

// DataStore para autenticación (NUEVO)
val Context.authCacheDataStore: DataStore<AuthCacheProto> by dataStore(
    fileName = "auth_cache.pb",
    serializer = AuthCacheSerializer
)