package mx.acg.zazil.model

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Query

// API de eliminación de cuenta
interface DeleteAccountApi {
    @DELETE("/")
    suspend fun deleteAccount(@Query("uid") uid: String): Response<Void>
}
