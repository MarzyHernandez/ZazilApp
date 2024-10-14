package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

/**
 * Interfaz de API para actualizar el perfil del usuario.
 *
 * Esta interfaz define un endpoint para realizar una solicitud PUT que permite
 * actualizar los detalles del perfil de un usuario. Utiliza Retrofit para facilitar
 * la comunicación con el backend.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface UpdateUserApi {
    /**
     * Método para actualizar el perfil del usuario.
     *
     * Realiza una solicitud PUT a la URL proporcionada para actualizar la información del usuario.
     *
     * @param url La URL del endpoint para la actualización del perfil del usuario.
     * @param updateUserRequest Un objeto de tipo [UpdateUserRequest] que contiene
     *                          los datos que se desean actualizar en el perfil.
     * @return Un objeto [Call] que encapsula la respuesta del servidor.
     */
    @PUT
    fun updateUserProfile(
        @Url url: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Call<Void>
}
