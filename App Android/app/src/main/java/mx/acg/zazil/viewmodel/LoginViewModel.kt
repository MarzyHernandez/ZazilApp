package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.GoogleUser
import mx.acg.zazil.model.UserApi
import mx.acg.zazil.model.UserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import java.io.IOException

class LoginViewModel : ViewModel() {

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val googleUser = GoogleUser()

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
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Verifica si los campos están vacíos
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Los campos de correo y contraseña no pueden estar vacíos."
            onFailure("Los campos de correo y contraseña no pueden estar vacíos.")
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Usuario autenticado correctamente
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        viewModelScope.launch {
                            try {
                                // Realiza la solicitud de login utilizando la API
                                val response: Response<UserResponse> = userService.getUserIdByEmail(email)

                                if (response.isSuccessful) {
                                    val userId = response.body()?.uid
                                    if (userId != null) {
                                        _userId.value = userId
                                        _errorMessage.value = null // Limpiar el mensaje de error
                                        onSuccess() // Login exitoso
                                    } else {
                                        val error = "Error al obtener el UID: Respuesta vacía"
                                        _errorMessage.value = error
                                        onFailure(error)
                                    }
                                } else {
                                    val error = "Error al obtener el UID: ${response.errorBody()?.string()}"
                                    _errorMessage.value = error
                                    onFailure(error)
                                }
                            } catch (e: Exception) {
                                val error = "Error de red: ${e.message}"
                                _errorMessage.value = error
                                onFailure(error)
                            }
                        }
                    } else {
                        val error = "No se pudo obtener el usuario actual."
                        _errorMessage.value = error
                        onFailure(error)
                    }
                } else {
                    val error = task.exception?.message ?: "Error desconocido en la autenticación."
                    _errorMessage.value = error
                    onFailure(error)
                }
            }
    }

    /**
     * Función para enviar datos del usuario a la API.
     */
    fun sendUserDataToApi(email: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = googleUser.sendUserData(email, uid)
                Log.d("UserApi", "Response: $response")
            } catch (e: IOException) {
                Log.e("UserApi", "Error sending user data: ${e.message}")
            }
        }
    }

    /**
     * Establece un mensaje de error personalizado.
     */
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
