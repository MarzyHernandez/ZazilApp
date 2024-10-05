package mx.acg.zazil

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CheckoutViewModel : ViewModel() {

    private val paymentRepository = PaymentRepository()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _isAmountValid = MutableStateFlow(true)
    val isAmountValid: StateFlow<Boolean> = _isAmountValid

    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String? = null

    fun onAmountChange(newAmount: String) {
        _amount.value = newAmount
        _isAmountValid.value = newAmount.toDoubleOrNull() != null && newAmount.isNotEmpty()
    }

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