package mx.acg.zazil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.acg.zazil.ui.theme.ZazilTheme
import mx.acg.zazil.view.AppNavHost
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import mx.acg.zazil.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    // Variable global del NavController
    private lateinit var navController: NavHostController

    // Inicializamos el LoginViewModel usando ViewModelProvider
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el tema de la aplicación en modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Inicializar el LoginViewModel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("3024441242-in4h94s1di0nh0tqppmg4fj9nah9ri6j.apps.googleusercontent.com")  // Este es el correcto
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializa el launcher para el flujo de Google Sign-In
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("GoogleSignIn", "Resultado de Sign-In recibido, cuenta: $account")
                    account?.idToken?.let { idToken ->
                        account.email?.let { email ->
                            firebaseAuthWithGoogle(idToken, email)
                        }
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Error en el proceso de Sign-In con Google: ${e.message}", e)
                }
            } else {
                Log.e("GoogleSignIn", "Error: Sign-In no completado.")
            }
        }

        setContent {
            ZazilTheme {
                // Inicializa el NavController y asigna a la variable global
                navController = rememberNavController()
                AppNavHost(
                    auth = auth,
                    signInWithGoogle = { signInWithGoogle() },
                    navController = navController
                )
            }
        }
    }

    // Iniciar el flujo de Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
        Log.d("GoogleSignIn", "Flujo de Sign-In con Google iniciado")
    }

    // Autenticar con Firebase usando el token de Google y redirigir al catalog
    private fun firebaseAuthWithGoogle(idToken: String, email: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    Log.d("GoogleSignIn", "Inicio de sesión exitoso con Google")
                    uid?.let {
                        // Utiliza el LoginViewModel para enviar los datos del usuario a la API
                        loginViewModel.sendUserDataToApi(email, uid)
                    }
                    navigateToCatalog()
                } else {
                    Log.e("GoogleSignIn", "Error en la autenticación con Google: ${task.exception?.message}")
                }
            }
    }

    // Función para navegar al catalog después de un breve retraso
    private fun navigateToCatalog() {
        lifecycleScope.launch {
            delay(300)
            withContext(Dispatchers.Main) {
                if (::navController.isInitialized) {
                    try {
                        navController.navigate("catalog") {
                            popUpTo("login") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("Navigation", "Error al navegar al catalog: ${e.message}")
                    }
                } else {
                    Log.e("Navigation", "NavController no está inicializado")
                }
            }
        }
    }
}
