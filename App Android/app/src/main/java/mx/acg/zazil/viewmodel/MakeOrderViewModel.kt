package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.acg.zazil.model.MakeOrder
import mx.acg.zazil.model.MakeOrderApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MakeOrderViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://makeorder-dztx2pd2na-uc.a.run.app") // URL base para la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: MakeOrderApiService = retrofit.create(MakeOrderApiService::class.java)

    // Estado para manejar el resultado del pedido
    private val _orderState = MutableStateFlow<OrderResult>(OrderResult.Idle)
    val orderState: StateFlow<OrderResult> = _orderState

    fun makeOrder(makeOrder: MakeOrder) {
        viewModelScope.launch {
            _orderState.value = OrderResult.Loading // Estado de carga
            try {
                apiService.makeOrder(makeOrder) // Llama a la API
                _orderState.value = OrderResult.Success // Pedido exitoso
            } catch (e: Exception) {
                _orderState.value = OrderResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Clase sellada para representar los diferentes estados de la solicitud
    sealed class OrderResult {
        object Idle : OrderResult()
        object Loading : OrderResult()
        object Success : OrderResult()
        data class Error(val message: String) : OrderResult()
    }
}
