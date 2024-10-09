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
    val userId: MutableLiveData<String?> get() = _userId

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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
        onSuccess: () -> Unit,  // Este parámetro debe estar definido
        onFailure: (String) -> Unit
    ) {
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
                                        onSuccess()  // Aquí rediriges o realizas acciones después del login
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
                    } else {
                        onFailure("No se pudo obtener el usuario actual.")
                    }
                } else {
                    onFailure("Error en la autenticación: ${task.exception?.message}")
                }
            }
    }

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



}
