package mx.acg.zazil.model

import retrofit2.Response
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
 * Repositorio para manejar la lógica relacionada con el historial de compras.
 *
 * Este repositorio se encarga de interactuar con la API [ShoppingHistoryApi] para obtener los
 * datos del historial de compras del usuario. Sirve como una capa intermedia entre el ViewModel
 * y la API, manejando las llamadas a la red y procesando la respuesta de la API.
 *
 * @param api Instancia de [ShoppingHistoryApi] utilizada para hacer las llamadas a la API.
 *
 * @author
 */
class ShoppingHistoryRepository(private val api: ShoppingHistoryApi) {
    // Obtiene el historial de compras de un usuario específico.
    suspend fun getShoppingHistory(uid: String): List<ShoppingHistory>? {
        val response = api.getShoppingHistory(uid)
        return if (response.isSuccessful) response.body() else null
    }
}
