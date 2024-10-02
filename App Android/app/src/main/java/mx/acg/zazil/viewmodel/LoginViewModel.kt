package mx.acg.zazil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.UserApi
import mx.acg.zazil.model.UserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Crear instancia de Retrofit directamente en el ViewModel
    private val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://getuidbyemail-dztx2pd2na-uc.a.run.app/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Crear la instancia del servicio
    private val userService: UserApi = retrofit.create(UserApi::class.java)

    /**
     * Función para iniciar sesión utilizando el email y password.
     * @param email Correo del usuario
     * @param password Contraseña del usuario
     * @param onSuccess Callback que se llama cuando la autenticación es exitosa.
     */
    fun loginWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Realiza la solicitud de login utilizando la API
                val response: Response<UserResponse> = userService.getUserIdByEmail(email)

                if (response.isSuccessful) {
                    val userId = response.body()?.uid
                    if (userId != null) {
                        _userId.value = userId
                        onSuccess()
                    } else {
                        _errorMessage.value = "Error al obtener el UID: Respuesta vacía"
                    }
                } else {
                    _errorMessage.value = "Error al obtener el UID: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.message}"
            }
        }
    }
}
