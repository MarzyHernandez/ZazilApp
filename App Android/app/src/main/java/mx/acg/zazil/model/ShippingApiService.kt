package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfaz Retrofit para obtener y guardar la dirección de envío.
 *
 * Esta interfaz define los endpoints necesarios para interactuar con el servidor
 * en relación con la dirección de envío del usuario. Incluye métodos para
 * obtener la última dirección de envío registrada y para guardar una nueva dirección.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface ShippingApiService {
    /**
     * Obtiene la última dirección de envío registrada para un usuario específico.
     *
     * @param uid El ID de usuario cuyo información de envío se desea obtener.
     * @return Un objeto [Response] que contiene un [ShippingResponse] con la dirección de envío
     *         asociada al usuario, o un error si no se encontró.
     */
    @GET("/")
    suspend fun getLastOrderShippingInfo(@Query("uid") uid: String): Response<ShippingResponse>

    /**
     * Guarda una nueva dirección de envío para un usuario específico.
     *
     * @param uid El ID de usuario para el cual se desea guardar la dirección de envío.
     * @param direccion Un objeto [ShippingResponse] que contiene la dirección de envío a guardar.
     * @return Un objeto [Response] que indica si la operación fue exitosa o no.
     */
    @POST("/")
    suspend fun saveShippingInfo(@Query("uid") uid: String, @Body direccion: ShippingResponse): Response<Void>
}
