package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Order
import mx.acg.zazil.model.OrderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderViewModel : ViewModel() {

    // LiveData para las órdenes
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    // LiveData para el manejo de errores
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Instancia de la API
    private val orderApi: OrderApi

    init {
        // Inicializar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://getorderbyid-dztx2pd2na-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        orderApi = retrofit.create(OrderApi::class.java)
    }

    // Método para obtener las órdenes por UID
    fun fetchOrders(uid: String) {
        viewModelScope.launch {
            try {
                val response = orderApi.getOrdersByUid(uid)
                if (response.isNotEmpty()) {
                    _orders.value = response
                } else {
                    _errorMessage.value = "No se encontraron órdenes."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener las órdenes: ${e.message}"
            }
        }
    }
}
