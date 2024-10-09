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

/**
 * ViewModel que maneja la lógica de inicio de sesión y comunicación con la API para la autenticación de usuarios.
 *
 * Este ViewModel se encarga de gestionar el inicio de sesión del usuario utilizando Firebase Authentication,
 * realizar solicitudes a la API para obtener el UID del usuario y manejar el envío de datos de usuario a la API.
 * También permite establecer mensajes de error y exponerlos a la vista.
 *
 * @property userId LiveData que contiene el UID del usuario autenticado.
 * @property errorMessage LiveData que contiene mensajes de error en caso de que ocurran.
 * @property googleUser Instancia de GoogleUser para el envío de datos de usuario.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 *
 */
class LoginViewModel : ViewModel() {

    // LiveData para el UID del usuario autenticado
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    // LiveData para el mensaje de error
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val googleUser = GoogleUser()

    // Crear instancia de Retrofit directamente en el ViewModel para la comunicación con la API
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

    // Crear la instancia del servicio para las solicitudes a la API
    private val userService: UserApi = retrofit.create(UserApi::class.java)

    /**
     * Inicia sesión utilizando el email y la contraseña proporcionados.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param onSuccess Callback a ejecutar si el inicio de sesión es exitoso.
     * @param onFailure Callback a ejecutar si el inicio de sesión falla, proporcionando el mensaje de error.
     */
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Verifica si los campos están vacíos y establece el mensaje de error si es así
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Los campos de correo y contraseña no pueden estar vacíos."
            onFailure("Los campos de correo y contraseña no pueden estar vacíos.")
            return
        }

        // Intentar iniciar sesión con Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si la autenticación es exitosa, obtener el usuario actual
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        viewModelScope.launch {
                            try {
                                // Realiza la solicitud a la API para obtener el UID del usuario
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
                            } catch (e: IOException) {
                                val error = "Error de red: Verifica tu conexión a Internet."
                                _errorMessage.value = error
                                onFailure(error)
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
     * Envía los datos del usuario a la API.
     *
     * @param email Correo electrónico del usuario.
     * @param uid UID del usuario.
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
     * Establece un mensaje de error personalizado en el LiveData.
     *
     * @param message Mensaje de error a establecer.
     */
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
