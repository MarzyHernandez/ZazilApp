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

/**
 * ViewModel para manejar la lógica de creación de órdenes.
 *
 * Esta clase se encarga de realizar solicitudes a la API para crear un pedido
 * y administrar el estado de la solicitud (cargando, exitoso o error).
 * Utiliza Retrofit para interactuar con el servicio web.
 *
 * @property retrofit Instancia de Retrofit para construir las llamadas a la API.
 * @property apiService Interfaz de servicio para las operaciones relacionadas con el pedido.
 * @property _orderState Estado interno para manejar el resultado del pedido.
 * @property orderState Exposición del estado de la orden, que puede ser Idle, Loading, Success o Error.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class MakeOrderViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://makeorder-dztx2pd2na-uc.a.run.app") // URL base para la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: MakeOrderApiService = retrofit.create(MakeOrderApiService::class.java)

    // Estado para manejar el resultado del pedido
    private val _orderState = MutableStateFlow<OrderResult>(OrderResult.Idle)
    val orderState: StateFlow<OrderResult> = _orderState

    /**
     * Función para realizar un pedido.
     *
     * Llama a la API para crear un nuevo pedido utilizando el objeto [MakeOrder].
     * Maneja los estados de carga y los resultados de la solicitud.
     *
     * @param makeOrder Objeto que contiene la información del pedido a realizar.
     */
    fun makeOrder(makeOrder: MakeOrder) {
        viewModelScope.launch {
            _orderState.value = OrderResult.Loading // Estado de carga
            try {
                apiService.makeOrder(makeOrder) // Llama a la API
                _orderState.value = OrderResult.Success // Pedido exitoso
                println("Pedido exitoso")
            } catch (e: Exception) {
                _orderState.value = OrderResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Clase sellada para representar los diferentes estados de la solicitud
    sealed class OrderResult {
        object Idle : OrderResult() // Estado inicial
        object Loading : OrderResult() // Estado de carga
        object Success : OrderResult() // Estado de éxito
        data class Error(val message: String) : OrderResult() // Estado de error con mensaje
    }
}
