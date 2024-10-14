package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import mx.acg.zazil.model.ShippingAddress
import mx.acg.zazil.model.ShippingApiService
import mx.acg.zazil.model.ShippingResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para manejar la lógica de obtener y guardar direcciones de envío.
 * Interactúa con la API de envío para obtener la última dirección y guardar una nueva dirección.
 *
 * @property apiService Interfaz para acceder a los servicios de envío.
 * @property shippingAddress Dirección de envío actual del usuario.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ShippingViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://getlastordershippinginfo-dztx2pd2na-uc.a.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ShippingApiService = retrofit.create(ShippingApiService::class.java)

    var shippingAddress: ShippingAddress? = null
        private set

    /**
     * Método para obtener la última dirección de envío a partir del UID del usuario.
     * Realiza la llamada a la API y maneja la respuesta.
     *
     * @param uid UID del usuario para el cual se busca la dirección.
     * @param onSuccess Callback que se ejecuta si se obtiene la dirección correctamente.
     * @param onError Callback que se ejecuta si ocurre un error durante la obtención.
     */
    fun getShippingAddress(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getLastOrderShippingInfo(uid)
                if (response.isSuccessful && response.body()?.direccion_envio != null) {
                    shippingAddress = response.body()?.direccion_envio
                    onSuccess()
                } else {
                    onError("No se encontró una dirección de envío.")
                }
            } catch (e: Exception) {
                onError("Error al obtener la dirección de envío: ${e.message}")
            }
        }
    }

    /**
     * Método para guardar una nueva dirección de envío.
     * Realiza la llamada a la API para guardar la dirección y maneja la respuesta.
     *
     * @param uid UID del usuario que está guardando la dirección.
     * @param address Dirección de envío a guardar.
     * @param onSuccess Callback que se ejecuta si la dirección se guarda correctamente.
     * @param onError Callback que se ejecuta si ocurre un error durante el guardado.
     */
    fun saveShippingAddress(uid: String, address: ShippingAddress, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Construir el cuerpo de la solicitud con ShippingResponse
                val shippingResponse = ShippingResponse(direccion_envio = address)

                // Convertir el objeto ShippingResponse a JSON usando Gson y mostrarlo en la consola
                val json = Gson().toJson(shippingResponse)
                println("Enviando JSON: $json")

                // Hacer la solicitud POST
                val response = apiService.saveShippingInfo(uid, shippingResponse)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error al guardar la dirección.")
                }
            } catch (e: Exception) {
                onError("Error al guardar la dirección: ${e.message}")
            }
        }
    }
}
