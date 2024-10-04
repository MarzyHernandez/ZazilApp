package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Interfaz para la API del carrito
interface CartApi {
    @GET("/")
    suspend fun getCartByUid(@Query("uid") uid: String): Cart

    @POST("/addProduct")
    suspend fun addProductToCart(
        @Query("uid") uid: String,  // Aquí se pasa el uid como parámetro de la URL
        @Query("id_producto") productId: Int,  // ID del producto
        @Query("cantidad") quantity: Int  // Cantidad
    ): Response<Unit>
}