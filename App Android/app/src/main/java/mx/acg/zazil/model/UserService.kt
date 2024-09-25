package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Definir la interfaz para la API
interface UserService {
    @GET("/")
    fun getUserIdByEmail(
        @Query("email") email: String
    ): Call<UserResponse>
}

// Modelo de respuesta (ajústalo según el JSON de respuesta que recibas)
data class UserResponse(
    val uid: String
)
