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

class ShoppingHistoryViewModel : ViewModel() {

    private val _shoppingHistory = MutableLiveData<List<ShoppingHistory>>()
    val shoppingHistory: LiveData<List<ShoppingHistory>> = _shoppingHistory

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Instancia de Retrofit y API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://getorderbyid-dztx2pd2na-uc.a.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ShoppingHistoryApi::class.java)

    // Función para obtener el historial de compras
    fun getShoppingHistory(uid: String) {
        viewModelScope.launch {
            try {
                val response = api.getShoppingHistoryByUid(uid)
                if (response.isSuccessful && response.body() != null) {
                    _shoppingHistory.value = response.body()
                } else {
                    _errorMessage.value = "No se encontraron compras."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener el historial de compras: ${e.message}"
            }
        }
    }
}
