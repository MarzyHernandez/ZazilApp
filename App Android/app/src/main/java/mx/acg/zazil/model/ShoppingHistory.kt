package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Data class que representa el historial de compras de un usuario.
 *
 * @param orderId El ID del pedido.
 * @param productImage La URL o ruta de la imagen del producto.
 * @param productQuantity La cantidad de productos en el pedido.
 * @param totalPrice El precio total del pedido.
 * @param date La fecha en que se realizó el pedido.
 *
 * @author Melissa Mireles Rendón
 */
data class ShoppingHistory(
    val orderId: String,
    val productImage: String,
    val productQuantity: Int,
    val totalPrice: Double,
    val date: String
)

/**
 * Interfaz de servicio para interactuar con el historial de compras usando Retrofit.
 * Define los endpoints para obtener los pedidos de un usuario.
 *
 * @param uid El ID de usuario (UID) para el cual se desea obtener el historial de compras.
 * @return Un objeto Call que contiene una lista de objetos ShoppingHistory con los detalles de cada pedido.
 */
interface ShoppingService {
    // Método para obtener la lista de pedidos asociados a un usuario mediante su UID
    @GET("/")
    fun getOrdersByUid(@Query("uid") uid: String): Call<List<ShoppingHistory>>
}
