package mx.acg.zazil.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mx.acg.zazil.model.FAQItem
import mx.acg.zazil.model.FAQService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para manejar la lógica de obtención de preguntas frecuentes (FAQs).
 *
 * Esta clase se encarga de realizar la solicitud a la API para obtener
 * la lista de preguntas frecuentes y almacena el estado de carga.
 * Utiliza Retrofit para realizar las solicitudes HTTP y actualizar
 * el estado de la UI a través de Compose.
 *
 * @property _faqItems Lista de preguntas frecuentes obtenidas de la API.
 * @property faqItems Exposición del estado de la lista de preguntas frecuentes.
 * @property _isLoading Estado que indica si la carga de FAQs está en progreso.
 * @property isLoading Exposición del estado de carga.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class FAQViewModel : ViewModel() {
    private val _faqItems = mutableStateOf<List<FAQItem>>(emptyList())
    val faqItems: State<List<FAQItem>> = _faqItems

    private val _isLoading = mutableStateOf(true) // Estado de carga inicializado en verdadero
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchFAQs() // Llamada a la función para obtener FAQs al iniciar el ViewModel
    }

    /**
     * Función privada que realiza la solicitud a la API para obtener la lista de FAQs.
     *
     * Utiliza Retrofit para construir la llamada a la API y maneja la respuesta
     * actualizando el estado de las FAQs y ocultando el indicador de carga
     * al finalizar la operación.
     */
    private fun fetchFAQs() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://getallfaq-dztx2pd2na-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FAQService::class.java)
        service.getAllFAQs().enqueue(object : Callback<List<FAQItem>> {
            override fun onResponse(call: Call<List<FAQItem>>, response: Response<List<FAQItem>>) {
                if (response.isSuccessful) {
                    _faqItems.value = response.body() ?: emptyList()
                }
                _isLoading.value = false // Ocultar el loader cuando se completa la carga
            }

            override fun onFailure(call: Call<List<FAQItem>>, t: Throwable) {
                // Manejo de errores
                t.printStackTrace()
                _isLoading.value = false // Ocultar el loader incluso si hay un error
            }
        })
    }
}
