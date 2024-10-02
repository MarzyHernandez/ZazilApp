package mx.acg.zazil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import mx.acg.zazil.view.CartTotal
import mx.acg.zazil.view.EndShoppingScreen
import mx.acg.zazil.view.FAQs
import mx.acg.zazil.view.MyShoppingDetailsScreen
import mx.acg.zazil.view.MyShoppingScreen
import mx.acg.zazil.view.PaymentScreen
import mx.acg.zazil.view.ProductDetailScreen
import mx.acg.zazil.view.RegisterScreen
import mx.acg.zazil.view.UpdateDataScreen
import mx.acg.zazil.viewmodel.LoginViewModel
import mx.acg.zazil.viewmodel.ShoppingHistoryViewModel


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
            ZazilTheme {
                val navController = rememberNavController()
                val currentBackStackEntry = navController.currentBackStackEntryAsState()

                // Condiciona la visibilidad de la NavBar dependiendo de la ruta actual
                val currentRoute = currentBackStackEntry.value?.destination?.route
                val showNavBar = currentRoute != "login" && currentRoute != "register"

                Scaffold(
                    bottomBar = {
                        if (showNavBar) {
                            NavBar(navController) // Muestra NavBar solo si no estás en las pantallas de login o register
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(navController = navController, viewModel = LoginViewModel(), signInWithGoogle = ::signInWithGoogle)
                        }

                        composable("register") {
                            RegisterScreen(navController = navController)
                        }

                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                            if (productId != null) {
                                ProductDetailScreen(productId = productId)
                            } else {
                                Text("Producto no encontrado")
                            }
                        }

                        composable("chat") {
                            BlogScreen()
                        }

                        composable("catalog") {
                            CatalogScreen(navController = navController)
                        }

                        composable("profile") {
                            ProfileScreen(navController = navController)
                        }

                        composable("configuracion") {
                            SettingsScreen(navController = navController)
                        }

                        composable("update") {
                            UpdateDataScreen(navController = navController)
                        }

                        composable("carrito") {
                            // Obtener el uid del usuario autenticado en Firebase
                            val currentUser = auth.currentUser
                            val uid = currentUser?.uid

                            // Pasar el uid al CartScreen
                            if (uid != null) {
                                CartScreen(navController = navController, uid = uid)
                            } else {
                                Text("No has iniciado sesión")
                            }
                        }


                        composable("myShopping") {
                            val shoppingHistoryViewModel: ShoppingHistoryViewModel = viewModel()
                            MyShoppingScreen(navController = navController, viewModel = shoppingHistoryViewModel)
                        }

                        composable("FAQs"){
                            FAQs(navController = navController)
                        }
                        composable("shoppingDetails") {
                            MyShoppingDetailsScreen(navController = navController)
                        }

                        composable("endShopping") {
                            EndShoppingScreen(navController = navController)
                        }

                        composable("payment") {
                            PaymentScreen(navController = navController)
                        }
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