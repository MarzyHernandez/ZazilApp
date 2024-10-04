package mx.acg.zazil.model

import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit interface que maneja las peticiones relacionadas con las órdenes
public interface MakeOrderApiService {
    @POST("/")
    suspend fun makeOrder(@Body order: MakeOrder)
}
