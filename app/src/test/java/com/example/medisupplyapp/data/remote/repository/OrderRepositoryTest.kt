package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.OrderStatus
import com.example.medisupplyapp.data.remote.api.OrdersApi // Asume que esta es la interfaz
import com.example.medisupplyapp.data.remote.dto.OrderResponse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.Date
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Debes crear este DTO ya que no lo proporcionaste, es la estructura de la respuesta JSON.
/**
 * Pruebas unitarias para OrdersRepository.
 * Estas pruebas se ejecutan localmente en la JVM (carpeta src/test).
 */
class OrdersRepositoryTest {

    // Mock de la interfaz de Retrofit para simular el servidor
    private val mockApi: OrdersApi = mock()

    // Instancia del repositorio que vamos a probar
    private lateinit var repository: OrdersRepository

    @Before
    fun setup() {
        repository = OrdersRepository(mockApi as OrdersApi)
    }

    // --- Pruebas para getOrders() ---

    @Test
    fun getOrders_success_mapsToCorrectDomainObjects() = runTest {
        // ARRANGE: Definir la respuesta simulada del servidor
        val apiResponse: List<OrderResponse>? = listOf(
            OrderResponse(
                numero_pedido = "123",
                fecha_creacion = Date(100),
                fecha_entrega_estimada = Date(200),
                fecha_ultima_actualizacion = Date(300),
                estado_nombre = "Pendiente de aprobación"
            ),
            OrderResponse(
                numero_pedido = "456",
                fecha_creacion = Date(400),
                fecha_entrega_estimada = null,
                fecha_ultima_actualizacion = Date(500),
                estado_nombre = "Entregado"
            )
        )

        // Simular el comportamiento de la API: devolver la lista simulada
        `when`(mockApi.getOrders(userId = "USER_55")).thenReturn(apiResponse)

        // ACT: Llamar a la función a probar
        val result = repository.getOrders("USER_55")

        // ASSERT: Verificar el resultado
        assertTrue(result.isSuccess, "La llamada debería haber sido exitosa")
        val orders = result.getOrNull()
        assertEquals(2, orders?.size, "Debería haber mapeado 2 órdenes")

        // Verificar el mapeo del primer objeto
        assertEquals("123", orders?.get(0)?.id)
        assertEquals(OrderStatus.PENDING_APPROVAL, orders?.get(0)?.status)

        // Verificar el mapeo del segundo objeto (incluyendo null en estimatedReleaseDate)
        assertEquals("456", orders?.get(1)?.id)
        assertEquals(OrderStatus.DELIVERED, orders?.get(1)?.status)
        assertTrue(orders?.get(1)?.estimatedReleaseDate == null, "La fecha estimada debe ser null")
    }

    @Test
    fun getOrders_emptyResponse_returnsEmptyList() = runTest {
        // ARRANGE: Simular que la API devuelve una lista vacía
        `when`(mockApi.getOrders(userId = "USER_55")).thenReturn(emptyList())

        // ACT
        val result = repository.getOrders("USER_55")

        // ASSERT
        assertTrue(result.isSuccess, "La llamada con respuesta vacía debe ser exitosa")
        assertTrue(result.getOrNull().isNullOrEmpty(), "Debe retornar una lista vacía")
    }

    @Test
    fun getOrders_http404Error_returnsEmptyList() = runTest {
        // ARRANGE: Simular un error HTTP 404
        val httpException = HttpException(Response.error<List<OrderResponse>>(
            404, mock(okhttp3.ResponseBody::class.java)
        ))
        `when`(mockApi.getOrders(userId = "USER_55")).thenAnswer { throw httpException }

        // ACT
        val result = repository.getOrders("USER_55")

        // ASSERT: Debe manejar el 404 y retornar una lista vacía
        assertTrue(result.isSuccess, "El error 404 debe manejarse como éxito con lista vacía")
        assertTrue(result.getOrNull().isNullOrEmpty(), "Debe retornar una lista vacía")
    }

    @Test
    fun getOrders_otherHttpError_returnsFailure() = runTest {
        // ARRANGE: Simular un error HTTP 500 (o cualquier otro que no sea 404)
        val httpException = HttpException(Response.error<List<OrderResponse>>(
            500, mock(okhttp3.ResponseBody::class.java)
        ))
        `when`(mockApi.getOrders(userId = "USER_55")).thenAnswer { throw httpException }

        // ACT
        val result = repository.getOrders("USER_55")

        // ASSERT
        assertTrue(result.isFailure, "Los errores HTTP que no son 404 deben retornar Failure")
        assertTrue(result.exceptionOrNull() is HttpException, "La excepción debe ser HttpException")
    }

    @Test
    fun getOrders_networkError_returnsFailure() = runTest {
        // ARRANGE: Simular un error de red o cualquier otra Exception
        val networkException = RuntimeException("Error de conexión de prueba")
        `when`(mockApi.getOrders(userId = "USER_55")).thenAnswer { throw networkException }

        // ACT
        val result = repository.getOrders("USER_55")

        // ASSERT
        assertTrue(result.isFailure, "Los errores de red deben retornar Failure")
        assertEquals("Error de conexión de prueba", result.exceptionOrNull()?.message)
    }

    // --- Pruebas para mapStatus() ---

    @Test
    fun mapStatus_validStatus_returnsCorrectEnum() {
        // ACT & ASSERT
        assertEquals(OrderStatus.PENDING_APPROVAL, repository.mapStatus("pendiente de aprobación"))
        assertEquals(OrderStatus.PROCESSING, repository.mapStatus("Procesando"))
        assertEquals(OrderStatus.IN_TRANSIT, repository.mapStatus("en camino"))
        assertEquals(OrderStatus.DELIVERED, repository.mapStatus("ENTREGADO"))
        assertEquals(OrderStatus.CANCELLED, repository.mapStatus("Cancelado"))
        assertEquals(OrderStatus.DELAYED, repository.mapStatus("demorado"))
    }

    @Test
    fun mapStatus_unknownStatus_returnsProcessingDefault() {
        // ACT & ASSERT
        assertEquals(OrderStatus.PROCESSING, repository.mapStatus("Estado Desconocido"))
        assertEquals(OrderStatus.PROCESSING, repository.mapStatus("invalid_state_123"))
    }
}
