package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.RegisterApi
import mx.acg.zazil.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

/**
 * Crea un cliente de Retrofit configurado con un interceptor de logging para depuración.
 *
 * @return Una instancia de Retrofit configurada con un interceptor de logging y un convertidor de JSON (GsonConverterFactory).
 * @author Melissa Mireles Rendón
 */
class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult

    private val registerApi: RegisterApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://registeruser-dztx2pd2na-uc.a.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RegisterApi::class.java)
    }

    /**
     * Lógica para registrar un nuevo usuario.
     * Realiza la llamada al API de registro y actualiza el resultado en `_registerResult`.
     * @param user Datos del usuario a registrar.
     */
    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                val response = registerApi.registerUser(user).execute()
                if (response.isSuccessful) {
                    _registerResult.value = "Registro exitoso"
                } else {
                    _registerResult.value = "Error en el registro: ${response.message()}"
                }
            } catch (e: Exception) {
                _registerResult.value = "Error de red: ${e.message}"
            }
        }
    }
}
