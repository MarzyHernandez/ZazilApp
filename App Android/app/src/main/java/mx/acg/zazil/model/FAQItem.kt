package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.GET

// Data class para representar los datos obtenidos
data class FAQItem(
    val id: Int,
    val pregunta: String,
    val respuesta: String
)

// Retrofit interface para obtener los datos
interface FAQService {
    @GET("/")
    fun getAllFAQs(): Call<List<FAQItem>>
}
