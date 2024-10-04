package mx.acg.zazil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckoutScreen()
        }
    }
}

@Composable
fun CheckoutScreen() {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var isAmountValid by remember { mutableStateOf(true) }

    // Stripe configuration
    var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    val paymentSheet = rememberPaymentSheet { paymentSheetResult ->
        onPaymentSheetResult(paymentSheetResult)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // Input for the amount
        TextField(
            value = amount,
            onValueChange = {
                amount = it
                isAmountValid = amount.toDoubleOrNull() != null && amount.isNotEmpty()
            },
            label = { Text("Enter Amount") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            isError = !isAmountValid
        )

        // PayPal Button
        Button(
            onClick = {
                if (isAmountValid) {
                    startWebPayment(context, amount)
                }
            },
            enabled = isAmountValid,
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 16.dp)
        ) {
            Text("Pay with PayPal")
        }

        // Stripe Button
        Button(
            onClick = {
                if (isAmountValid) {
                    // Trigger Stripe payment flow
                    val jsonBody = JSONObject().put("amount", amount.toInt()).toString()

                    "https://paymentsheet-dztx2pd2na-uc.a.run.app/"
                        .httpPost()
                        .header("Content-Type", "application/json")
                        .body(jsonBody)
                        .responseJson { request, response, result ->
                            result.fold(success = { responseJson ->
                                val paymentIntent = responseJson.obj().optString("paymentIntent")
                                val customer = responseJson.obj().optString("customer")
                                val ephemeralKey = responseJson.obj().optString("ephemeralKey")
                                val publishableKey = responseJson.obj().optString("publishableKey")

                                if (paymentIntent.isNotEmpty() && customer.isNotEmpty() && ephemeralKey.isNotEmpty()) {
                                    paymentIntentClientSecret = paymentIntent
                                    customerConfig = PaymentSheet.CustomerConfiguration(
                                        id = customer,
                                        ephemeralKeySecret = ephemeralKey
                                    )
                                    PaymentConfiguration.init(context, publishableKey)

                                    presentPaymentSheet(paymentSheet, customerConfig!!, paymentIntentClientSecret!!)
                                }
                            }, failure = { error ->
                                Log.e("Stripe", "Error: ${error.message}")
                            })
                        }
                }
            },
            enabled = isAmountValid,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Pay with Stripe")
        }
    }
}

// Function to start PayPal Web Payment flow
fun startWebPayment(context: android.content.Context, amount: String) {
    thread {
        try {
            val url = URL("https://paypalpayment-dztx2pd2na-uc.a.run.app")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            // Create the JSON object to send
            val jsonInputString = JSONObject().put("amount", amount)

            val wr = OutputStreamWriter(connection.outputStream)
            wr.write(jsonInputString.toString())
            wr.flush()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val responseJson = JSONObject(response)
                val orderID = responseJson.getString("orderID")

                // Open PayPal checkout
                val webPaymentUrl = "https://www.paypal.com/checkoutnow?token=$orderID"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webPaymentUrl))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                ContextCompat.startActivity(context, browserIntent, null)
            } else {
                Log.e("PayPal", "Failed to create PayPal order. Response Code: $responseCode")
            }
        } catch (e: Exception) {
            Log.e("PayPal", "Error: ${e.message}")
        }
    }
}

// Function to handle Stripe PaymentSheet presentation
private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "MHJ",
            customer = customerConfig,
            defaultBillingDetails = PaymentSheet.BillingDetails(
                address = PaymentSheet.Address(country = "MX")
            )
        )
    )
}

// Handle Stripe payment result
private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when (paymentSheetResult) {
        is PaymentSheetResult.Completed -> Log.i("Stripe", "Payment successful!")
        is PaymentSheetResult.Failed -> Log.e("Stripe", "Error: ${paymentSheetResult.error}")
        is PaymentSheetResult.Canceled -> Log.w("Stripe", "Payment canceled.")
    }
}



