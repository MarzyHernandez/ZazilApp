package mx.acg.zazil.model

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de API para manejar las solicitudes relacionadas con las órdenes.
 *
 * Esta interfaz define métodos para interactuar con un servicio RESTful
 * que permite recuperar las órdenes asociadas a un usuario específico.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface OrderApi {
    /**
     * Obtiene la lista de órdenes asociadas al usuario mediante su UID.
     *
     * @param uid Identificador único del usuario.
     * @return Lista de objetos [Order] que representan las órdenes del usuario.
     */
    @GET("/")
    suspend fun getOrdersByUid(@Query("uid") uid: String): List<Order>
}
