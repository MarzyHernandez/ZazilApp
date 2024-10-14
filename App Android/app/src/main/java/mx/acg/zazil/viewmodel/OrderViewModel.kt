package mx.acg.zazil.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Order
import mx.acg.zazil.model.OrderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para manejar la lógica de obtención de órdenes.
 *
 * Esta clase se encarga de realizar solicitudes a la API para obtener las órdenes
 * de un usuario utilizando su UID. Administra el estado de la lista de órdenes
 * y los mensajes de error.
 *
 * @property orderApi Instancia de la interfaz OrderApi para realizar llamadas a la API.
 * @property _orders LiveData privado que almacena la lista de órdenes.
 * @property orders LiveData expuesto que permite acceder a la lista de órdenes.
 * @property _errorMessage LiveData privado que maneja los mensajes de error.
 * @property errorMessage LiveData expuesto que permite acceder a los mensajes de error.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
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

    /**
     * Función para obtener las órdenes a partir del UID del usuario.
     *
     * Realiza una solicitud a la API utilizando el UID proporcionado y
     * actualiza el LiveData correspondiente con la lista de órdenes.
     * Si hay un error durante la solicitud, se actualiza el LiveData
     * de mensajes de error.
     *
     * @param uid El UID del usuario para el cual se desean obtener las órdenes.
     */
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
