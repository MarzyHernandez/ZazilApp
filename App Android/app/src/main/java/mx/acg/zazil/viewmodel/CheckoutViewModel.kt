package mx.acg.zazil

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel para gestionar el proceso de pago utilizando Stripe.
 *
 * Este ViewModel interactúa con el repositorio de pagos para obtener la configuración
 * necesaria para realizar transacciones y manejar el flujo de pago a través de Stripe.
 *
 * @property paymentRepository Repositorio que maneja las operaciones relacionadas con los pagos.
 * @property _amount Almacena el monto a pagar como un flujo de estado mutable.
 * @property amount Exposición del flujo de estado del monto.
 * @property _isAmountValid Almacena el estado de validez del monto ingresado.
 * @property isAmountValid Exposición del flujo de estado que indica si el monto es válido.
 * @property customerConfig Configuración del cliente para la hoja de pagos de Stripe.
 * @property paymentIntentClientSecret Secreto del cliente del intento de pago.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class CheckoutViewModel : ViewModel() {

    private val paymentRepository = PaymentRepository()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _isAmountValid = MutableStateFlow(true)
    val isAmountValid: StateFlow<Boolean> = _isAmountValid

    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String? = null

    /**
     * Actualiza el monto ingresado y valida su formato.
     *
     * @param newAmount El nuevo monto ingresado por el usuario.
     */
    fun onAmountChange(newAmount: String) {
        _amount.value = newAmount
        _isAmountValid.value = newAmount.toDoubleOrNull() != null && newAmount.isNotEmpty()
    }

    /**
     * Inicia el proceso de pago utilizando Stripe.
     *
     * Obtiene la configuración de pago de Stripe y presenta la hoja de pagos.
     *
     * @param context El contexto de la actividad actual.
     * @param paymentSheet Instancia de PaymentSheet para presentar el flujo de pago.
     */
    fun onStripePayment(context: Context, paymentSheet: PaymentSheet) {
        val amountDouble = _amount.value.toDoubleOrNull()
        if (amountDouble != null) {
            val amountInt = (amountDouble).toInt()
            paymentRepository.getStripePaymentConfiguration(amountInt, onSuccess = { paymentIntent, customer, ephemeralKey, publishableKey ->
                customerConfig = PaymentSheet.CustomerConfiguration(
                    id = customer,
                    ephemeralKeySecret = ephemeralKey
                )
                paymentIntentClientSecret = paymentIntent

                PaymentConfiguration.init(context, publishableKey)
                presentPaymentSheet(paymentSheet)
            }, onFailure = { error ->
                Log.e("CheckoutViewModel", "Failed to get payment config: $error")
            })
        } else {
            Log.e("CheckoutViewModel", "Invalid amount")
        }
    }

    /**
     * Presenta la hoja de pagos de Stripe.
     *
     * @param paymentSheet Instancia de PaymentSheet para presentar el flujo de pago.
     */
    private fun presentPaymentSheet(paymentSheet: PaymentSheet) {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret ?: return,
            PaymentSheet.Configuration(
                merchantDisplayName = "Zazil",
                customer = customerConfig,
                defaultBillingDetails = PaymentSheet.BillingDetails(
                    address = PaymentSheet.Address(country = "MX")
                )
            )
        )
    }
}
