package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de Retrofit que maneja la solicitud para obtener el UID del usuario
 * basado en su correo electrónico.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface UserApi {
    @GET("/")
    suspend fun getUserIdByEmail(
        @Query("email") email: String
    ): Response<UserResponse>
}

