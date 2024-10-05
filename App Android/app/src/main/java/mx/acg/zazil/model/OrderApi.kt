package mx.acg.zazil.model

import retrofit2.http.GET
import retrofit2.http.Query

interface OrderApi {
    @GET("/")
    suspend fun getOrdersByUid(@Query("uid") uid: String): List<Order>
}
