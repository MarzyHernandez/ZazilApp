package mx.acg.zazil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.acg.zazil.ui.theme.ZazilTheme
import mx.acg.zazil.view.BlogScreen
import mx.acg.zazil.view.CatalogScreen
import mx.acg.zazil.view.LoginScreen
import mx.acg.zazil.view.NavBar
import mx.acg.zazil.view.ProductDetailScreen
import mx.acg.zazil.view.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZazilTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { NavBar(navController) } // Aquí está tu NavBar
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "catalog",
                        modifier = Modifier.padding(innerPadding) // Usa el padding que recibe Scaffold
                    ) {
                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: -1
                            ProductDetailScreen(productId = productId)
                        }
                        composable("chat") { BlogScreen() }
                        composable("catalog") { CatalogScreen(navController = navController) }  // Pasa el NavController
                        composable("perfil") { ProfileScreen() }
                        composable("configuracion"){}
                    }
                }
            }
        }
    }
}
