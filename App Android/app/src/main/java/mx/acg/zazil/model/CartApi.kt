package mx.acg.zazil.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// Interfaz para la API del carrito
interface CartApi {
    @GET("/")
    suspend fun getCartByUid(@Query("uid") uid: String): Cart

    @PUT("https://updatecart-dztx2pd2na-uc.a.run.app")
    suspend fun updateCart(@Body cartUpdate: CartUpdate): Response<ResponseBody>
}
