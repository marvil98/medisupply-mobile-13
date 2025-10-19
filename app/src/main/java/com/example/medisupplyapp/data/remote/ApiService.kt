import com.example.medisupplyapp.data.model.ClientResponse
import com.example.medisupplyapp.data.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/users/clients")
    suspend fun getClients(): Response<ClientResponse>

    @GET("products/available")
    suspend fun getProducts(): Response<List<Product>>
}
