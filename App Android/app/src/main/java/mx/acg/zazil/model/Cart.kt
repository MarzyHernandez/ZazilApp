package mx.acg.zazil.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

// Modelo del carrito
data class Cart(
    val id: String,
    val estado: Boolean,
    val monto_total: Double,
    val fecha_creacion: FechaCreacion,
    val uid: String,
    val productos: List<CartProduct>
)

data class FechaCreacion(
    val _seconds: Long,
    val _nanoseconds: Long
)

data class CartProduct(
    val id_producto: Int,
    val cantidad: Int
)


// Clase de datos que representa el cuerpo de la solicitud para actualizar el carrito
data class CartUpdate(
    val uid: String,
    val id_producto: Int,
    val cantidad: Int
)


// Interfaz para la API del carrito
interface CartApi {
    @GET("/")
    suspend fun getCartByUid(@Query("uid") uid: String): Cart

    @PUT("https://updatecart-dztx2pd2na-uc.a.run.app")
    suspend fun updateCart(@Body cartUpdate: CartUpdate): Response<ResponseBody>
}

// Singleton para Retrofit de la API del carrito
object CartRetrofitInstance {
    val cartApi: CartApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getactivecart-dztx2pd2na-uc.a.run.app/") // Base URL de la API del carrito
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApi::class.java)
    }
}
