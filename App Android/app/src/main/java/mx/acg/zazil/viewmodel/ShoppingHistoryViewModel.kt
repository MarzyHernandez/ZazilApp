/**package mx.acg.zazil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.ShoppingHistory
import mx.acg.zazil.model.ShoppingHistoryRepository


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
class ShoppingHistoryViewModel(private val repository: ShoppingHistoryRepository) : ViewModel() {
    // LiveData para almacenar el historial de compras
    private val _shoppingHistory = MutableLiveData<List<ShoppingHistory>>()
    val shoppingHistory: LiveData<List<ShoppingHistory>> = _shoppingHistory

    // LiveData para manejar mensajes de error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * Obtiene el historial de compras para el usuario con el UID especificado.
     *
     * @param uid El UID del usuario cuyas compras se quieren obtener.
     */
    fun getShoppingHistory(uid: String) {
        viewModelScope.launch {
            try {
                // Llamar al repositorio para obtener el historial de compras
                val history = repository.getShoppingHistory(uid)

                // Verificar si se obtuvieron datos y actualizar LiveData en consecuencia
                if (!history.isNullOrEmpty()) {
                    //_shoppingHistory.value = history
                } else {
                    _errorMessage.value = "No se encontraron compras."
                }
            } catch (e: Exception) {
                // En caso de error, actualizar el mensaje de error
                _errorMessage.value = "Error al obtener el historial de compras: ${e.message}"
            }
        }
    }
}
 **/