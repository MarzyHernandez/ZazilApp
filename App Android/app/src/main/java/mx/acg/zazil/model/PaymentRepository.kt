package mx.acg.zazil

import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONObject

/**
 * Clase que gestiona la comunicación con el backend relacionada con el procesamiento de pagos.
 *
 * Esta clase proporciona métodos para interactuar con el servidor para obtener
 * la configuración necesaria para realizar pagos mediante Stripe.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class PaymentRepository {

    /**
     * Función para obtener la configuración de Stripe desde el servidor.
     *
     * Realiza una solicitud HTTP POST al servidor para obtener información
     * sobre el pago, como el Payment Intent, el cliente, la clave efímera
     * y la clave publicable.
     *
     * @param amount Monto del pago en centavos.
     * @param onSuccess Callback que se llama al recibir una respuesta exitosa
     *                  del servidor con los datos necesarios para el pago.
     * @param onFailure Callback que se llama si ocurre un error durante la solicitud.
     */
    fun getStripePaymentConfiguration(
        amount: Int,
        onSuccess: (paymentIntent: String, customer: String, ephemeralKey: String, publishableKey: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val jsonBody = JSONObject().put("amount", amount).toString()

        "https://paymentsheet-dztx2pd2na-uc.a.run.app/"
            .httpPost()
            .header("Content-Type", "application/json")
            .body(jsonBody)
            .responseJson { _, _, result ->
                result.fold(success = { responseJson ->
                    val paymentIntent = responseJson.obj().optString("paymentIntent")
                    val customer = responseJson.obj().optString("customer")
                    val ephemeralKey = responseJson.obj().optString("ephemeralKey")
                    val publishableKey = responseJson.obj().optString("publishableKey")

                    if (paymentIntent.isNotEmpty() && customer.isNotEmpty() && ephemeralKey.isNotEmpty()) {
                        onSuccess(paymentIntent, customer, ephemeralKey, publishableKey)
                    } else {
                        onFailure("Invalid response from server")
                    }
                }, failure = { error ->
                    Log.e("PaymentRepository", "Error: ${error.message}")
                    onFailure(error.message ?: "Unknown error")
                })
            }
    }
}
