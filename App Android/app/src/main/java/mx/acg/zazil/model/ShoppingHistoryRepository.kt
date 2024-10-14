package mx.acg.zazil.model

/**
 * Repositorio para manejar la lógica relacionada con el historial de compras.
 *
 * Este repositorio se encarga de interactuar con la API [ShoppingHistoryApi] para obtener los
 * datos del historial de compras del usuario. Sirve como una capa intermedia entre el ViewModel
 * y la API, manejando las llamadas a la red y procesando la respuesta de la API.
 *
 * @param api Instancia de [ShoppingHistoryApi] utilizada para hacer las llamadas a la API.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ShoppingHistoryRepository(private val api: ShoppingHistoryApi) {
    // Obtiene el historial de compras de un usuario específico.
    suspend fun getShoppingHistory(uid: String): List<ShoppingHistory>? {
        val response = api.getShoppingHistoryByUid(uid)
        return if (response.isSuccessful) response.body() else null
    }
}