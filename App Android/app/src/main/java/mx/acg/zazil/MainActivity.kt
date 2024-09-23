package mx.acg.zazil


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import mx.acg.zazil.viewmodel.PaymentViewModel

class MainActivity : ComponentActivity() {

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var stripe: Stripe

    private lateinit var stripeActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear Layout programáticamente
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val amountEditText = EditText(this).apply {
            hint = "Ingrese la cantidad a pagar"
        }

        val paymentStatusTextView = TextView(this).apply {
            text = "Estado del pago: Aún no iniciado"
        }

        val payButton = Button(this).apply {
            text = "Pagar"
        }

        layout.addView(amountEditText)
        layout.addView(payButton)
        layout.addView(paymentStatusTextView)

        setContentView(layout)

        // Inicializar Stripe
        stripe = Stripe(
            applicationContext,
            "tu_stripe_publishable_key"  // Coloca tu clave pública de Stripe aquí
        )

        // Inicializar el ViewModel
        paymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        paymentViewModel.initializeStripe(stripe)

        // Observar cambios en el estado del pago
        paymentViewModel.paymentStatus.observe(this, { status ->
            paymentStatusTextView.text = "Estado del pago: $status"
        })

        // Configurar el ActivityResultLauncher para manejar el resultado del pago
        stripeActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            if (data != null) {
                stripe.onPaymentResult(
                    result.resultCode,
                    data,
                    object : ApiResultCallback<PaymentIntentResult> {
                        override fun onSuccess(result: PaymentIntentResult) {
                            val paymentIntent = result.intent
                            if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                                paymentViewModel.setPaymentStatus("Pago completado")
                            } else {
                                paymentViewModel.setPaymentStatus("El pago no se completó")
                            }
                        }

                        override fun onError(e: Exception) {
                            paymentViewModel.setPaymentStatus("Error en el pago: ${e.message}")
                        }
                    }
                )
            }
        }

        // Lógica para el botón de pago
        payButton.setOnClickListener {
            val amount = amountEditText.text.toString().toIntOrNull()
            if (amount != null && amount > 0) {
                paymentViewModel.createPaymentIntent(amount)
            } else {
                paymentStatusTextView.text = "Por favor ingrese una cantidad válida"
            }
        }
    }
}

/*
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import mx.acg.zazil.ui.theme.ZazilTheme
import mx.acg.zazil.view.BlogScreen
import mx.acg.zazil.view.CatalogScreen
import mx.acg.zazil.view.LoginScreen
import mx.acg.zazil.view.NavBar
import mx.acg.zazil.view.ProfileScreen
import mx.acg.zazil.view.SettingsScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import mx.acg.zazil.view.CarritoScreen
import mx.acg.zazil.view.CartScreen
import mx.acg.zazil.view.ProductDetailScreen


class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inicializar firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // configurar google sing in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("3024441242-in4h94s1di0nh0tqppmg4fj9nah9ri6j.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            //LoginScreen(::signInWithGoogle)
            ZazilTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { NavBar(navController) } // Aquí está tu NavBar
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding) // Usa el padding que recibe Scaffold
                    ) {
                        composable("login") {
                            LoginScreen(navController = navController, signInWithGoogle = ::signInWithGoogle)  // Pasa el navController y la función de Google Sign-In
                        }

                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                            if (productId != null) {
                                ProductDetailScreen(productId = productId)
                            } else {
                                // Manejar el error en caso de que el productId sea nulo o inválido
                                Text("Producto no encontrado")
                            }
                        }

                        composable("carrito") { CartScreen() }
                        composable("chat") { BlogScreen() }
                        composable("catalog") { CatalogScreen(navController = navController) }  // Pasa el NavController
                        composable("perfil") { ProfileScreen() }
                        composable("configuracion"){ SettingsScreen() }
                    }
                }
            }
        }
    }

    // Iniciar el flujo de Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // resultado de Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Manejo del error si el inicio de sesión con Google falla
                e.printStackTrace()
            }
        }
    }

    // Autenticar con Firebase usando el token de Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // El inicio de sesión fue exitoso
                    println("Inicio de sesión exitoso con Google")
                } else {
                    // Error en la autenticación
                    println("Error en la autenticación con Google: ${task.exception}")
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001  // Puedes usar cualquier valor entero único.
    }
}
*/