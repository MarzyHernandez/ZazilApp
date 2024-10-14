package mx.acg.zazil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.ShoppingHistory
import mx.acg.zazil.model.ShoppingHistoryApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para manejar el historial de compras del usuario.
 * Interactúa con la API para obtener el historial de compras basado en el UID del usuario.
 *
 * @property shoppingHistory LiveData que contiene la lista de compras del usuario.
 * @property errorMessage LiveData que maneja los mensajes de error.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ShoppingHistoryViewModel : ViewModel() {

    private val _shoppingHistory = MutableLiveData<List<ShoppingHistory>>()
    val shoppingHistory: LiveData<List<ShoppingHistory>> = _shoppingHistory

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Instancia de Retrofit y API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://getorderbyid-dztx2pd2na-uc.a.run.app/") // URL base de la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ShoppingHistoryApi::class.java)

    /**
     * Función para obtener el historial de compras a partir del UID del usuario.
     * Realiza una llamada a la API y actualiza el LiveData correspondiente con la respuesta.
     *
     * @param uid UID del usuario cuyo historial de compras se desea obtener.
     */
    fun getShoppingHistory(uid: String) {
        viewModelScope.launch {
            try {
                val response = api.getShoppingHistoryByUid(uid)
                if (response.isSuccessful && response.body() != null) {
                    _shoppingHistory.value = response.body() // Actualiza el historial de compras
                } else {
                    _errorMessage.value = "No se encontraron compras." // Maneja la falta de compras
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener el historial de compras: ${e.message}" // Maneja excepciones
            }
        }
    }
}
