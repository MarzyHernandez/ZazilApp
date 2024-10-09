package mx.acg.zazil.model

import retrofit2.http.DELETE
import retrofit2.http.Query

interface DeleteAccountApiService {
    @DELETE("/")
    suspend fun deleteAccount(@Query("uid") uid: String)
}
