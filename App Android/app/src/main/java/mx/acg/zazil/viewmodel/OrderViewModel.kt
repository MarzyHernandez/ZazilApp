package mx.acg.zazil.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Order
import mx.acg.zazil.model.OrderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderViewModel : ViewModel() {

    // Instancia de Retrofit para consumir la API
    private val orderApi: OrderApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getorderbyid-dztx2pd2na-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderApi::class.java)
    }

    // LiveData para almacenar la lista de órdenes
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    // LiveData para manejar mensajes de error
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Función para obtener órdenes a partir del UID
    fun fetchOrders(uid: String) {
        viewModelScope.launch {
            try {
                if (uid.isNotEmpty()) {
                    println("UID enviado a la API: $uid")

                    // Llama a la API
                    val ordersList = orderApi.getOrdersByUid(uid)

                    // Verifica el contenido de la lista
                    if (ordersList.isNotEmpty()) {
                        _orders.value = ordersList
                        println("Órdenes obtenidas: $ordersList")
                    } else {
                        _errorMessage.value = "No se encontraron órdenes."
                    }
                } else {
                    _errorMessage.value = "UID está vacío o incorrecto."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener las órdenes: ${e.message}"
                println("Error al hacer la solicitud: ${e.message}")
            }
        }
    }
}