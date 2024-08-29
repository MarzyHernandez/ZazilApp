package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import mx.acg.zazil.R

/**
 * Composable que representa una barra de navegación fija en la parte inferior de la pantalla.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 * @param [modifier] Modificador que permite personalizar la disposición y el estilo del Composable.
 */
@Composable
fun NavBar(navController: NavHostController, modifier: Modifier = Modifier) {
    // Barra de navegación fija
    Row(
        modifier = modifier  // Usa el modifier que se pasa como parámetro
            .fillMaxWidth()
            .background(
                color = Color(0xFFEBB7A7),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home_w),
                contentDescription = "Home",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("carrito") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cart_w),
                contentDescription = "Carrito",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("perfil") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile_w),
                contentDescription = "Perfil",
                tint = Color.White
            )
        }
        IconButton(onClick = { navController.navigate("configuracion") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings_w),
                contentDescription = "Configuración",
                tint = Color.White
            )
        }
    }
}