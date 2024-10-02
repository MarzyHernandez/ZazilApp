package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import mx.acg.zazil.model.ShoppingHistory

/**
 * Interfaz de API para interactuar con el historial de compras usando Retrofit.
 * Define los endpoints para obtener el historial de compras de un usuario mediante su UID.
 *
 * @param uid El ID único del usuario (UID) cuyo historial de compras se desea obtener.
 * @return Un objeto Response que contiene una lista de objetos ShoppingHistory con los detalles de los pedidos.
 *
 * @author Melissa Mireles Rendón
 */
interface ShoppingHistoryApi {
    @GET("/")
    suspend fun getShoppingHistoryByUid(@Query("uid") uid: String): Response<List<ShoppingHistory>>
}

