package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

// API interface para la solicitud PUT
interface UpdateUserApi {
    @PUT
    fun updateUserProfile(
        @Url url: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Call<Void>
}