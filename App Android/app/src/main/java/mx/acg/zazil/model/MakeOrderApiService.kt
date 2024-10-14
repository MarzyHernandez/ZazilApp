package mx.acg.zazil.model

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit que maneja las peticiones relacionadas con las órdenes de compra.
 *
 * Esta interfaz define los métodos que se utilizarán para interactuar con la API
 * de creación de órdenes. En particular, se define el método para enviar una nueva orden
 * al servidor.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface MakeOrderApiService {
    /**
     * Método para enviar una nueva orden al servidor.
     *
     * @param order Objeto [MakeOrder] que contiene los detalles de la orden a crear.
     * @return Una respuesta de la API que indica el resultado de la operación.
     */
    @POST("/")
    suspend fun makeOrder(@Body order: MakeOrder)
}
