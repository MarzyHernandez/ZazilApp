package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Query

/**
 * Interfaz que define el endpoint para la eliminación de cuentas de usuario.
 * Esta interfaz usa Retrofit para manejar la solicitud HTTP DELETE que elimina una cuenta de usuario.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface DeleteAccountApi {

    /**
     * Elimina la cuenta de un usuario específico utilizando su UID.
     *
     * @param uid Identificador único del usuario cuya cuenta será eliminada.
     * @return Respuesta HTTP que indica si la operación fue exitosa.
     */
    @DELETE("/")
    suspend fun deleteAccount(@Query("uid") uid: String): Response<Void>
}
