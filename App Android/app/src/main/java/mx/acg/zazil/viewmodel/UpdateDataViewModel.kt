package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import mx.acg.zazil.model.UpdateUserApi
import mx.acg.zazil.model.UpdateUserRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel para manejar la lógica de actualización de los datos del usuario.
 * Interactúa con la API para enviar una solicitud de actualización de los datos del perfil del usuario.
 *
 * @property updateResult LiveData que contiene el resultado de la operación de actualización.
 * @property updateApi Interfaz de la API utilizada para realizar la solicitud de actualización.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class UpdateDataViewModel : ViewModel() {

    private val _updateResult = MutableLiveData<String>()
    val updateResult: LiveData<String> get() = _updateResult

    private val updateApi: UpdateUserApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://updateuserprofilebyid-dztx2pd2na-uc.a.run.app") // URL base de la API
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UpdateUserApi::class.java)
    }

    /**
     * Función para actualizar los datos del usuario.
     * Envía una solicitud a la API con los nuevos datos del usuario y actualiza el resultado.
     *
     * @param uid UID del usuario cuya información se desea actualizar.
     * @param nombres Nombres del usuario.
     * @param apellidos Apellidos del usuario.
     * @param email Correo electrónico del usuario.
     * @param telefono Número de teléfono del usuario.
     * @param password Contraseña del usuario.
     */
    fun updateUserData(uid: String, nombres: String, apellidos: String, email: String, telefono: String, password: String) {
        val request = UpdateUserRequest(
            uid = uid,
            nombres = nombres,
            apellidos = apellidos,
            telefono = telefono,
            email = email,
            password = password,
            foto_perfil = "" // Se pasa vacío por ahora
        )

        updateApi.updateUserProfile("?uid=$uid", request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _updateResult.value = "Datos actualizados exitosamente"
                } else {
                    _updateResult.value = "Error al actualizar: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _updateResult.value = "Error de red: ${t.message}"
            }
        })
    }
}
