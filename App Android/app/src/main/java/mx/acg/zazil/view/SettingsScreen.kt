package mx.acg.zazil.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R

@Composable
fun SettingsScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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
                    .clickable {navController.navigate("login")},

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

            SettingOption(title = "Cambiar Contraseña", iconResId = R.drawable.ic_password)
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

            SettingOption(title = "Sobre Nosotros", iconResId = R.drawable.ic_about)
            SettingOption(title = "Términos y Condiciones", iconResId = R.drawable.ic_terms)
            SettingOption(title = "Aviso de Privacidad", iconResId = R.drawable.ic_privacy)
            SettingOption(title = "Créditos", iconResId = R.drawable.ic_credits)

            Spacer(modifier = Modifier.height(16.dp))

            // Redes Sociales
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialMediaIcon(R.drawable.ic_facebook)
                SocialMediaIcon(R.drawable.ic_instagram)
                SocialMediaIcon(R.drawable.ic_tiktok)
                SocialMediaIcon(R.drawable.ic_web)
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
fun SettingOption(title: String, iconResId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
fun SocialMediaIcon(iconResId: Int) {
    IconButton(onClick = { /* Acción para red social */ }) {
        Box(
            modifier = Modifier
                .size(50.dp) // Ajusta el tamaño de la caja para los íconos
                .clip(CircleShape)
                .background(Color(0xFFEBB7A7))
        ) {
            Image(
                painter = painterResource(id = iconResId), // Reemplaza con tu recurso
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)  // Tamaño de la imagen
                    .clip(CircleShape)  // Si quieres mantener la forma circular
            )

        }
    }
}
