package mx.acg.zazil.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth

/**
 * Composable que configura la navegación de la aplicación utilizando un controlador de navegación
 * y un ViewModel para manejar la autenticación.
 *
 * @param auth FirebaseAuth utilizado para la autenticación de usuarios.
 * @param signInWithGoogle Función que maneja el inicio de sesión con Google.
 * @param navController Controlador de navegación que permite navegar entre pantallas.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    auth: FirebaseAuth,
    signInWithGoogle: () -> Unit,
    navController: NavHostController
) {
    // Obtiene la entrada actual de la pila de navegación
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    // Condiciona la visibilidad de la NavBar dependiendo de la ruta actual
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val showNavBar = currentRoute != "login" && currentRoute != "register" && currentRoute != "recuperarContrasena"

    // Scaffold con NavBar si es necesario
    Scaffold(
        bottomBar = {
            if (showNavBar) {
                NavBar(navController) // Muestra NavBar solo si no estás en las pantallas de login o register
            }
        }
    ) { innerPadding ->
        // Configuración de las rutas de navegación
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                // Pantalla de inicio de sesión
                LoginScreen(navController = navController, signInWithGoogle = signInWithGoogle)
            }

            composable("recuperarContrasena") {
                // Pantalla de recuperación de contraseña
                RecuperarContrasenaScreen(navController = navController)
            }

            composable("register") {
                // Pantalla de registro de usuario
                RegisterScreen(navController = navController)
            }

            composable("productDetail/{productId}") { backStackEntry ->
                // Pantalla de detalles del producto
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                if (productId != null) {
                    ProductDetailScreen(productId = productId, navController = navController)
                } else {
                    Text("Producto no encontrado")
                }
            }

            composable("chat") {
                // Pantalla de chat
                BlogScreen()
            }

            composable("catalog") {
                // Pantalla de catálogo
                CatalogScreen(navController = navController)
            }


            composable("profile") {
                // Pantalla de perfil
                val currentUser = auth.currentUser
                val uid = currentUser?.uid

                if (uid != null) {
                    ProfileScreen(navController = navController, uid = uid)
                } else {
                    Text("No has iniciado sesión")
                }
            }


            composable("configuracion") {
                // Pantalla de configuración
                SettingsScreen(navController = navController)
            }

            composable("update") {
                // Pantalla de actualización de datos
                UpdateDataScreen(navController = navController)
            }

            composable("cart") {
                // Pantalla de carrito de compras
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


            composable("FAQs") {
                // Pantalla de preguntas frecuentes
                FAQs(navController = navController)
            }

            composable("myShopping/{uid}") { backStackEntry ->
                // Recupera el argumento `uid` desde la navegación
                val uid = backStackEntry.arguments?.getString("uid") ?: ""

                // Muestra la pantalla MyShoppingScreen con el UID recuperado
                MyShoppingScreen(navController = navController, uid = uid)
            }

            composable("shoppingDetails/{orderId}/{uid}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")?.toInt() ?: 0
                val uid = backStackEntry.arguments?.getString("uid") ?: ""

                MyShoppingDetailsScreen(
                    navController = navController,
                    orderId = orderId,
                    uid = uid
                )
            }

            composable("endShopping/{total}/{calle}/{numeroInterior}/{colonia}/{codigoPostal}/{ciudad}/{estado}/{pais}") { backStackEntry ->
                val total = backStackEntry.arguments?.getString("total") ?: "0.00"
                val calle = backStackEntry.arguments?.getString("calle") ?: ""
                val numeroInterior = backStackEntry.arguments?.getString("numeroInterior") ?: ""
                val colonia = backStackEntry.arguments?.getString("colonia") ?: ""
                val codigoPostal = backStackEntry.arguments?.getString("codigoPostal") ?: ""
                val ciudad = backStackEntry.arguments?.getString("ciudad") ?: ""
                val estado = backStackEntry.arguments?.getString("estado") ?: ""
                val pais = backStackEntry.arguments?.getString("pais") ?: ""

                EndShoppingScreen(
                    navController = navController,
                    total = total,
                    calle = calle,
                    numeroInterior = numeroInterior,
                    colonia = colonia,
                    codigoPostal = codigoPostal,
                    ciudad = ciudad,
                    estado = estado,
                    pais = pais
                )
            }

            composable("payment/{total}/{calle}/{numeroInterior}/{colonia}/{codigoPostal}/{ciudad}/{estado}/{pais}") { backStackEntry ->
                val total = backStackEntry.arguments?.getString("total") ?: "0.00"
                val calle = backStackEntry.arguments?.getString("calle") ?: ""
                val numeroInterior = backStackEntry.arguments?.getString("numeroInterior") ?: ""
                val colonia = backStackEntry.arguments?.getString("colonia") ?: ""
                val codigoPostal = backStackEntry.arguments?.getString("codigoPostal") ?: ""
                val ciudad = backStackEntry.arguments?.getString("ciudad") ?: ""
                val estado = backStackEntry.arguments?.getString("estado") ?: ""
                val pais = backStackEntry.arguments?.getString("pais") ?: ""

                PaymentScreen(
                    navController = navController,
                    total = total,
                    calle = calle,
                    numeroInterior = numeroInterior,
                    colonia = colonia,
                    codigoPostal = codigoPostal,
                    ciudad = ciudad,
                    estado = estado,
                    pais = pais
                )
            }


            composable("settings") {
                // Pantalla de configuración
                SettingsScreen(navController)
            }

            composable("updateData") {
                // Pantalla de actualización de datos
                UpdateDataScreen(navController = navController)  // Aquí llamamos a la pantalla de ActualizarDatos
            }

            composable("privacyPolicy") {
                // Pantalla de política de privacidad
                PrivacyPolicyScreen(navController)
            }

            composable("TyC") {
                // Pantalla de términos y condiciones
                TyC(navController) }

            composable("aboutUs") {
                // Pantalla de información de la empresa
                AboutUsScreen(navController)
            }

            composable("credits") {
                // Pantalla de créditos
                CreditsScreen(navController)
            }
        }
    }
}
