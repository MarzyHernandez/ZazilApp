package mx.acg.zazil.model

import retrofit2.http.DELETE
import retrofit2.http.Query

/**
 * Interfaz que define el servicio para la eliminación de una cuenta de usuario a través de una solicitud HTTP DELETE.
 * Usa Retrofit para interactuar con el servicio backend que gestiona la eliminación de cuentas.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface DeleteAccountApiService {

    /**
     * Elimina la cuenta del usuario especificado a través de su UID.
     *
     * @param uid Identificador único del usuario cuya cuenta será eliminada.
     */
    @DELETE("/")
    suspend fun deleteAccount(@Query("uid") uid: String)
}
