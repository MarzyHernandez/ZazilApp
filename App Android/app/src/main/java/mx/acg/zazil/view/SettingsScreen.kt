package mx.acg.zazil.view

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R

@Composable
fun SettingsScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    // Obtén la instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEE1D6)) // Fondo color rosa
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) // Hacemos la columna scrolleable
        ) {
            // Título de Ajustes
            Text(
                text = "Ajustes",
                fontSize = 28.sp,
                color = Color(0xFF191919),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Opción "Cerrar sesión"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        Log.d("SettingsScreen", "Cerrando sesión...") // Log para rastrear cuando se clickea "Cerrar sesión"
                        auth.signOut()  // Cierra la sesión de Firebase
                        Log.d("SettingsScreen", "Sesión cerrada con éxito. Redirigiendo a la pantalla de login.")
                        navController.navigate("login")  // Redirige al login
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Cerrar sesión",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    fontSize = 16.sp,
                    color = Color(0xFF191919)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección "Mi Cuenta"
            Text(
                text = "MI CUENTA",
                fontSize = 16.sp,
                color = Color(0xFF545454),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingOption(title = "Cambiar Contraseña", iconResId = R.drawable.ic_password )
            SettingOption(title = "Actualizar Datos", iconResId = R.drawable.ic_edit)
            SettingOption(title = "Eliminar Cuenta", iconResId = R.drawable.ic_delete)

            Spacer(modifier = Modifier.height(16.dp))

            // Sección "Acerca de"
            Text(
                text = "ACERCA DE",
                fontSize = 16.sp,
                color = Color(0xFF545454),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Navegar a la pantalla de "Sobre Nosotros"
            SettingOption(title = "Sobre Nosotros", iconResId = R.drawable.ic_about) {
                navController.navigate("aboutUs")
            }

            // Navegar a la pantalla de "TyC"
            SettingOption(title = "Términos y Condiciones", iconResId = R.drawable.ic_terms) {
                navController.navigate("TyC")
            }

            SettingOption(title = "Aviso de Privacidad", iconResId = R.drawable.ic_privacy)

            // Navegar a la pantalla de "Créditos"
            SettingOption(title = "Créditos", iconResId = R.drawable.ic_credits) {
                navController.navigate("credits")
            }

            // Redes Sociales
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialMediaIcon(R.drawable.ic_facebook, "https://www.facebook.com/share/mjfnrxUW55mwTEXk/?mibextid=LQQJ4d")
                SocialMediaIcon(R.drawable.ic_instagram, "https://www.instagram.com/toallas.zazil?igsh=MTdtaXRmeXk3MWxqMw==")
                SocialMediaIcon(R.drawable.ic_tiktok, "https://www.tiktok.com/@todas.brillamos?_t=8qDNNVLY1kz&_r=1")
                SocialMediaIcon(R.drawable.ic_web, "https://zazilrrr.org/catalogo/zazil/index.php#")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Versión de la aplicación
            Text(
                text = "v1.0.1",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingOption(title: String, iconResId: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }  // Añadimos un callback para manejar clics
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = title,
            tint = Color(0xFFE17F61),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = Color(0xFF191919))
    }
}

@Composable
fun SocialMediaIcon(iconResId: Int, url: String) {
    val context = LocalContext.current
    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }) {
        Box(
            modifier = Modifier
                .size(50.dp) // Ajusta el tamaño de la caja para los íconos
                .clip(CircleShape)
                .background(Color(0xFFEBB7A7))
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }
    }
}
