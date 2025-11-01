// DailyRouteSerializer.kt

import androidx.datastore.core.Serializer
import com.example.medisupplyapp.data.model.DailyRoute
import com.example.medisupplyapp.data.model.RoutePoint
import com.google.protobuf.InvalidProtocolBufferException
import com.example.medisupplyapp.datastore.DailyRouteProto
import com.example.medisupplyapp.datastore.RouteCacheProto
import com.example.medisupplyapp.datastore.RoutePointProto
import java.io.InputStream
import java.io.OutputStream

// Definir el estado inicial si no hay datos guardados
object DailyRouteSerializer : Serializer<RouteCacheProto> {
    override val defaultValue: RouteCacheProto = RouteCacheProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RouteCacheProto {
        try {
            return RouteCacheProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw InvalidProtocolBufferException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: RouteCacheProto, output: OutputStream) = t.writeTo(output)

    // 1. Convertir Proto a Modelo Kotlin
    fun RouteCacheProto.toDailyRoute(): DailyRoute {
        // Retorna solo el objeto DailyRoute, ignorando el timestamp
        return route.toDailyRoute()
    }

    // 2. Convertir Modelo Kotlin a Proto Cache (Añade el tiempo al guardar)
    fun DailyRoute.toRouteCacheProto(timestamp: Long): RouteCacheProto {
        return RouteCacheProto.newBuilder()
            .setTimestamp(timestamp)
            .setRoute(this.toDailyRouteProto())
            .build()
    }
}

// 1. Convertir Proto a Modelo Kotlin
fun DailyRouteProto.toDailyRoute(): DailyRoute {
    return DailyRoute(
        numberVisits = numberVisits,
        visitsMade = visitsMade,
        visits = visitsList.map { it.toRoutePoint() }
    )
}

fun RoutePointProto.toRoutePoint(): RoutePoint {
    return RoutePoint(
        address = address,
        id = visitId,
        latitude = latitude,
        name = name,
        client = client,
        longitude = longitude
    )
}

// 2. Convertir Modelo Kotlin a Proto
fun DailyRoute. toDailyRouteProto(): DailyRouteProto {
    return DailyRouteProto.newBuilder()
        .setNumberVisits(numberVisits)
        .setVisitsMade(visitsMade)
        .addAllVisits(visits.map { it.toRoutePointProto() })
        .build()
}

fun RoutePoint.toRoutePointProto(): RoutePointProto {
    return RoutePointProto.newBuilder()
        .setAddress(address)
        .setVisitId(id)
        .setLatitude(latitude)
        .setName(name)
        .setClient(client)
        .setLongitude(longitude)
        .build()
}