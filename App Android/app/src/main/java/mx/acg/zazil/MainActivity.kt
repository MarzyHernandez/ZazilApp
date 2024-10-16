package mx.acg.zazil

import android.content.Intent
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import mx.acg.zazil.viewmodel.LoginViewModel

/**
 * MainActivity es la actividad principal de la aplicación, encargada de gestionar la autenticación del usuario
 * mediante Google Sign-In y de iniciar el flujo de navegación a través de la aplicación.
 *
 * @property googleSignInClient Cliente para manejar las solicitudes de Google Sign-In.
 * @property auth FirebaseAuth utilizado para autenticar usuarios con Google.
 * @property signInLauncher Launcher para manejar los resultados de la actividad de Sign-In.
 * @property navController Controlador de navegación para gestionar el flujo de la aplicación.
 * @property loginViewModel ViewModel para manejar la lógica de inicio de sesión.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    // Variable global del NavController
    private lateinit var navController: NavHostController

    // Inicializamos el LoginViewModel usando ViewModelProvider
    private lateinit var loginViewModel: LoginViewModel

    @RequiresApi(Build.VERSION_CODES.O)
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
            .requestIdToken("3024441242-in4h94s1di0nh0tqppmg4fj9nah9ri6j.apps.googleusercontent.com") // Reemplaza con tu ID de cliente
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

    // Autenticar con Firebase usando el token de Google y redirigir al catálogo
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

    // Función para navegar al catálogo después de un breve retraso
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
