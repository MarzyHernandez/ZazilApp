package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import mx.acg.zazil.model.DeleteAccountApiService

/**
 * ViewModel para manejar la configuración de la cuenta del usuario.
 * Proporciona funciones para eliminar la cuenta del usuario.
 *
 * @property retrofit Instancia de Retrofit configurada para realizar llamadas a la API.
 * @property apiService Interfaz para acceder a los servicios de eliminación de cuenta.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class SettingsViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://deleteuserbyid-dztx2pd2na-uc.a.run.app")  // URL base de la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: DeleteAccountApiService = retrofit.create(DeleteAccountApiService::class.java)

    /**
     * Elimina la cuenta del usuario dado su UID.
     * Realiza la llamada a la API y maneja el resultado.
     *
     * @param uid UID de la cuenta a eliminar.
     * @param onSuccess Callback que se ejecuta si la eliminación es exitosa.
     * @param onError Callback que se ejecuta si ocurre un error durante la eliminación.
     */
    fun deleteAccount(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                apiService.deleteAccount(uid)
                Log.d("SettingsViewModel", "Cuenta eliminada exitosamente con UID: $uid")

                // Ejecutar el callback de éxito en el hilo principal
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error al eliminar la cuenta: ${e.message}")

                // Ejecutar el callback de error en el hilo principal
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "Error desconocido")
                }
            }
        }
    }
}
