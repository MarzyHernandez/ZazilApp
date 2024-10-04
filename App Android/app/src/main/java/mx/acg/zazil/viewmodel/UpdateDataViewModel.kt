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


class UpdateDataViewModel : ViewModel() {

    private val _updateResult = MutableLiveData<String>()
    val updateResult: LiveData<String> get() = _updateResult

    private val updateApi: UpdateUserApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://updateuserprofilebyid-dztx2pd2na-uc.a.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UpdateUserApi::class.java)
    }

    // Función para actualizar los datos del usuario
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
