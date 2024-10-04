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

class SettingsViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://deleteuserbyid-dztx2pd2na-uc.a.run.app")  // URL base de la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: DeleteAccountApiService = retrofit.create(DeleteAccountApiService::class.java)

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
