package mx.acg.zazil

import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONObject

// Esta clase gestiona todo lo relacionado con la comunicación con el backend
class PaymentRepository {

    // Función para obtener la configuración de Stripe desde el servidor
    fun getStripePaymentConfiguration(amount: Int, onSuccess: (paymentIntent: String, customer: String, ephemeralKey: String, publishableKey: String) -> Unit, onFailure: (String) -> Unit) {
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