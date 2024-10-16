package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Modelo de datos que representa la respuesta de la API cuando se solicita el UID
 * basado en el correo electrónico del usuario.
 *
 * Esta clase es utilizada para mapear la respuesta de la API que contiene el UID del usuario
 * correspondiente al correo electrónico proporcionado en la solicitud.
 *
 * @param uid El UID del usuario retornado por la API.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class UserResponse(
    val uid: String
)
