package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.DeleteAccountApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para gestionar la eliminación de cuentas de usuario.
 *
 * Este ViewModel interactúa con la API de eliminación de cuentas para realizar
 * la operación de borrado a través de una llamada a la red. Utiliza Retrofit
 * para realizar las solicitudes HTTP y se asegura de que la eliminación se
 * realice en un contexto de coroutine para mantener la UI responsiva.
 *
 * @property deleteAccountApi Instancia de la API para eliminar cuentas.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class DeleteAccountViewModel : ViewModel() {

    // Inicialización de la API de eliminación de cuentas con logging
    private val deleteAccountApi: DeleteAccountApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://deleteuserbyid-dztx2pd2na-uc.a.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DeleteAccountApi::class.java)
    }

    /**
     * Elimina la cuenta de usuario correspondiente al UID proporcionado.
     *
     * Realiza una llamada a la API para eliminar la cuenta. Si la eliminación
     * es exitosa, se invoca el callback onSuccess; de lo contrario, se invoca
     * el callback onError con un mensaje de error.
     *
     * @param uid El UID del usuario cuya cuenta se desea eliminar.
     * @param onSuccess Callback que se invoca en caso de éxito.
     * @param onError Callback que se invoca en caso de error.
     */
    fun deleteAccount(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = deleteAccountApi.deleteAccount(uid)
                if (response.isSuccessful) {
                    onSuccess() // Eliminar cuenta con éxito
                } else {
                    onError("Error al eliminar la cuenta")
                }
            } catch (e: Exception) {
                onError("Error de red: ${e.message}")
            }
        }
    }
}
