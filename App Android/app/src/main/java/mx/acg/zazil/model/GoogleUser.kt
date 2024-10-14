package mx.acg.zazil.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Clase para enviar los datos de un usuario autenticado con Google a la API.
 * Esta clase utiliza OkHttpClient para realizar la solicitud POST a la API que registra
 * un nuevo usuario en el sistema remoto.
 *
 * El cuerpo de la solicitud contiene el email y el UID del usuario autenticado.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class GoogleUser {
    private val client = OkHttpClient()

    /**
     * Envía los datos del usuario autenticado a la API de nuevo usuario.
     *
     * @param email El correo electrónico del usuario.
     * @param uid El UID del usuario proporcionado por Firebase Auth.
     * @return Respuesta de la API si la solicitud es exitosa, de lo contrario, lanza una excepción.
     * @throws IOException Si la solicitud falla.
     */
    @Throws(IOException::class)
    fun sendUserData(email: String, uid: String): String? {
        // URL de la API de nuevo usuario
        val url = "https://newuser-dztx2pd2na-uc.a.run.app"

        // Crear el objeto JSON con los datos del usuario
        val json = JSONObject().apply {
            put("email", email)
            put("uid", uid)
        }

        // Convertir el JSON a cuerpo de solicitud
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // Crear la solicitud POST con la URL y el cuerpo
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        // Ejecutar la solicitud y manejar la respuesta
        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                response.body?.string()
            } else {
                throw IOException("Unexpected code $response")
            }
        }
    }
}
