package com.example.medisupplyapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Crea y configura el cliente HTTP de OkHttp.
 * Añadimos el HttpLoggingInterceptor para ver las solicitudes y respuestas
 * completas (incluidos errores HTTP 4xx y 5xx) en el Logcat.
 */
object OkHttpClientBuilder {

    // Configura el interceptor para que muestre el cuerpo completo de la solicitud/respuesta
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Construye y devuelve un OkHttpClient configurado para la aplicación.
     */
    fun build(): OkHttpClient {
        return OkHttpClient.Builder()
            // Configura un tiempo de espera razonable (opcional)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            // Añade el interceptor de logs como último interceptor de la aplicación
            .addInterceptor(loggingInterceptor)
            .build()
    }
}