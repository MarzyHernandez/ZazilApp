package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.functions.FirebaseFunctions
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams

class PaymentViewModel : ViewModel() {

    private val functions = FirebaseFunctions.getInstance()
    private lateinit var stripe: Stripe  // Se inicializa desde la vista

    // LiveData para el estado del pago
    private val _paymentStatus = MutableLiveData<String>()
    val paymentStatus: LiveData<String> get() = _paymentStatus

    // Método para inicializar Stripe desde la Activity o Fragment
    fun initializeStripe(stripeInstance: Stripe) {
        stripe = stripeInstance
    }

    // Función para crear el PaymentIntent llamando a Firebase Functions
    fun createPaymentIntent(amount: Int) {
        val data = hashMapOf("amount" to amount)

        functions.getHttpsCallable("createPaymentIntent")
            .call(data)
            .addOnSuccessListener { result ->
                val clientSecret = result.data as String
                confirmPayment(clientSecret)
            }
            .addOnFailureListener { e ->
                Log.e("Payment", "Error creando PaymentIntent: ${e.message}")
                _paymentStatus.value = "Error al crear PaymentIntent"
            }
    }

    // Confirmar el pago con Stripe usando el clientSecret
    private fun confirmPayment(clientSecret: String) {
        val paymentMethodId = "pm_card_visa"  // Reemplaza con el ID del método de pago real

        val params = ConfirmPaymentIntentParams.createWithPaymentMethodId(
            paymentMethodId,
            clientSecret
        )

        // Aquí lanzarás la confirmación del pago desde la actividad
        // stripe.confirmPayment será lanzado desde MainActivity, usando el ActivityResultLauncher
    }

    // Método para actualizar el estado del pago desde la vista
    fun setPaymentStatus(status: String) {
        _paymentStatus.value = status
    }
}
