package mx.acg.zazil.model

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("/") // Obtener todos los productos
    suspend fun getProducts(): List<Product>

    @GET("/") // Obtener un producto por ID
    suspend fun getProductById(@Query("id") productId: Int): Product
}
