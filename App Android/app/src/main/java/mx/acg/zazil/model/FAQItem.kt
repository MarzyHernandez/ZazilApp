package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.GET

/**
 * Data class que representa un elemento de FAQ (Preguntas Frecuentes).
 *
 * @param id El identificador único de la FAQ.
 * @param pregunta El texto de la pregunta.
 * @param respuesta El texto de la respuesta asociada a la pregunta.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class FAQItem(
    val id: Int,
    val pregunta: String,
    val respuesta: String
)

/**
 * Interfaz de servicio para obtener los elementos de Preguntas Frecuentes (FAQs) desde una API.
 * Usa Retrofit para realizar solicitudes HTTP GET.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface FAQService {

    /**
     * Realiza una solicitud HTTP GET para obtener todas las FAQ del servicio.
     *
     * @return Un `Call` que contiene una lista de objetos `FAQItem`.
     */
    @GET("/")
    fun getAllFAQs(): Call<List<FAQItem>>
}
