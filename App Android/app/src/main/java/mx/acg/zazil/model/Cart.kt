package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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

data class AddToCartRequest(
    val uid: String,
    val id_producto: Int,
    val cantidad: Int
)


