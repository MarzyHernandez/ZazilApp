package mx.acg.zazil.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * Interfaz que define los endpoints de la API del carrito de compras.
 * Esta interfaz usa Retrofit para manejar las solicitudes HTTP relacionadas con el carrito.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface CartApi {

    /**
     * Obtiene el carrito de compras activo asociado a un usuario específico.
     *
     * @param uid Identificador único del usuario.
     * @return El carrito de compras del usuario.
     */
    @GET("/")
    suspend fun getCartByUid(@Query("uid") uid: String): Cart

    /**
     * Actualiza el carrito de compras con nuevos productos o cantidades.
     *
     * @param cartUpdate Objeto que contiene los detalles del carrito y los productos a actualizar.
     * @return Respuesta HTTP que contiene el cuerpo de la respuesta.
     */
    @PUT("https://updatecart-dztx2pd2na-uc.a.run.app")
    suspend fun updateCart(@Body cartUpdate: CartUpdate): Response<ResponseBody>
}
