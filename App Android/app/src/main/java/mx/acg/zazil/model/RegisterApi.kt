package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de la API para realizar el registro de usuarios usando Retrofit.
 * Define los endpoints relacionados con las operaciones de registro.
 *
 * @param user Un objeto de tipo [User] que contiene los datos del usuario a registrar.
 * @return Un objeto [Call] que encapsula la respuesta del servidor en un objeto [RegisterResponse].
 *
 * @author Melissa Mireles Rendón
 */
interface RegisterApi {
    // Método para registrar un nuevo usuario en el servidor.
    @POST("/")
    fun registerUser(@Body user: User): Call<RegisterResponse>
}
