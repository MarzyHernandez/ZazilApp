package mx.acg.zazil.view

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

@Composable
fun AppNavHost(
    auth: FirebaseAuth,
    signInWithGoogle: () -> Unit,
    navController: NavHostController
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    // Condiciona la visibilidad de la NavBar dependiendo de la ruta actual
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val showNavBar = currentRoute != "login" && currentRoute != "register" && currentRoute != "recuperarContrasena"

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
                LoginScreen(navController = navController, signInWithGoogle = signInWithGoogle)
            }

            composable("recuperarContrasena") {
                RecuperarContrasenaScreen(navController = navController)
            }

            composable("register") {
                RegisterScreen(navController = navController)
            }

            composable("productDetail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                if (productId != null) {
                    ProductDetailScreen(productId = productId, navController = navController)
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
                val currentUser = auth.currentUser
                val uid = currentUser?.uid

                if (uid != null) {
                    ProfileScreen(navController = navController, uid = uid)
                } else {
                    Text("No has iniciado sesión")
                }
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


            composable("FAQs") {
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


            composable("settings") { SettingsScreen(navController) }

            composable("updateData") {
                UpdateDataScreen(navController = navController)  // Aquí llamamos a la pantalla de ActualizarDatos
            }

            composable("TyC") { TyC(navController) }

            composable("aboutUs") { AboutUsScreen(navController) }

            composable("credits") { CreditsScreen(navController) }
        }
    }
}
