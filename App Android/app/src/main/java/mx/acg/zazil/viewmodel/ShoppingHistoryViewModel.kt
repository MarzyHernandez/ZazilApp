package mx.acg.zazil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.ShoppingHistory
import mx.acg.zazil.model.ShoppingHistoryApi
import mx.acg.zazil.model.ShoppingHistoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para manejar el historial de compras del usuario.
 *
 * Este ViewModel se encarga de obtener el historial de compras del usuario llamando al repositorio [ShoppingHistoryRepository].
 * Actualiza el estado de la UI utilizando LiveData.
 *
 * @property repository Instancia del repositorio que maneja las llamadas a la API.
 *
 * @author Melissa Mireles Rendón
 */
class ShoppingHistoryViewModel : ViewModel() {
    // Crear la instancia de Retrofit y ShoppingHistoryApi
    private val api: ShoppingHistoryApi = Retrofit.Builder()
        .baseUrl("https://getorderbyid-dztx2pd2na-uc.a.run.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ShoppingHistoryApi::class.java)

    // Instancia del repositorio directamente en el ViewModel
    private val repository = ShoppingHistoryRepository(api)

    // LiveData para almacenar el historial de compras
    private val _shoppingHistory = MutableLiveData<List<ShoppingHistory>>()
    val shoppingHistory: LiveData<List<ShoppingHistory>> = _shoppingHistory

    // LiveData para almacenar los mensajes de error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    /**
     * Obtiene el historial de compras para el usuario con el UID especificado.
     *
     * @param uid El UID del usuario cuyas compras se quieren obtener.
     */
    fun getShoppingHistory(uid: String) {
        // Ejecutar la llamada de red en una corrutina para no bloquear el hilo principal
        viewModelScope.launch {
            try {
                // Llamada al repositorio para obtener el historial de compras
                val history = repository.getShoppingHistory(uid)

                // Si hay datos en la respuesta, actualizar LiveData
                if (!history.isNullOrEmpty()) {
                    _shoppingHistory.value = history
                } else {
                    _shoppingHistory.value = emptyList()
                    _errorMessage.value = "No se encontraron compras."
                }
            } catch (e: Exception) {
                // Si ocurre un error, actualizar el mensaje de error
                _errorMessage.value = "Error al obtener el historial de compras: ${e.message}"
                _shoppingHistory.value = emptyList()
            }
        }
    }
}