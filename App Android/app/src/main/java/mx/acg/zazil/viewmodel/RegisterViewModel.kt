package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import mx.acg.zazil.model.RegisterApi
import mx.acg.zazil.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import mx.acg.zazil.model.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel para manejar el registro de usuarios.
 * Se encarga de interactuar con la API de registro y gestionar el resultado de la operación.
 *
 * @property _registerResult Estado interno que almacena el resultado del registro.
 * @property registerResult LiveData que expone el resultado del registro a la interfaz de usuario.
 * @property registerApi Instancia de la interfaz RegisterApi para realizar llamadas a la API de registro.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<String?>()
    val registerResult: MutableLiveData<String?> get() = _registerResult

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
     *
     * @param user Datos del usuario a registrar.
     */
    fun registerUser(user: User) {
        registerApi.registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _registerResult.value = "Registro exitoso"
                } else {
                    // Si no es exitoso, intenta extraer el mensaje de error del cuerpo de la respuesta
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val jsonObject = errorBody?.let { JSONObject(it) }
                        jsonObject?.getString("error")
                    } catch (e: Exception) {
                        "Error en el registro: ${response.message()}"
                    }

                    _registerResult.value = errorMessage
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.value = "Error de red: ${t.message}"
            }
        })
    }
}
