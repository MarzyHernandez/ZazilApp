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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.acg.zazil.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel()  // Inyectamos el ViewModel
) {
    // Obtén la instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    // Estado para controlar el diálogo de confirmación
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)  // Fondo color blanco
            .padding(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())  // Hacemos la columna scrolleable
        ) {

            // Encabezado del carrito
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp))
                    .background(Color(0xFFFEE1D6))
                    .padding(vertical = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ajustes",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = gabaritoFontFamily,
                        color = Color(0xFF191919)

                    )
                }
            }

            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                // Opción "Cerrar sesión"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            Log.d("SettingsScreen", "Cerrando sesión...")
                            auth.signOut()
                            navController.navigate("login")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(24.dp))
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


                SettingOption(
                    title = "Actualizar Datos",
                    iconResId = R.drawable.ic_edit
                ) {
                    navController.navigate("updateData")  // Navega a la pantalla de actualización de datos
                }


                SettingOption(
                    title = "Eliminar Cuenta",
                    iconResId = R.drawable.ic_delete
                ) {
                    // Mostrar diálogo de confirmación antes de eliminar la cuenta
                    showDialog = true
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección "Acerca de"
                Text(
                    text = "ACERCA DE",
                    fontSize = 16.sp,
                    color = Color(0xFF545454),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                SettingOption(title = "Sobre Nosotros", iconResId = R.drawable.ic_about) {
                    navController.navigate("aboutUs")
                }

                SettingOption(title = "Términos y Condiciones", iconResId = R.drawable.ic_terms) {
                    navController.navigate("TyC")
                }

                SettingOption(title = "Aviso de Privacidad", iconResId = R.drawable.ic_privacy)

                SettingOption(title = "Créditos", iconResId = R.drawable.ic_credits) {
                    navController.navigate("credits")
                }

                Spacer(modifier = Modifier.height(50.dp))
                // Redes Sociales
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SocialMediaIcon(
                        R.drawable.ic_facebook,
                        "https://www.facebook.com/share/mjfnrxUW55mwTEXk/?mibextid=LQQJ4d"
                    )
                    SocialMediaIcon(
                        R.drawable.ic_instagram,
                        "https://www.instagram.com/toallas.zazil?igsh=MTdtaXRmeXk3MWxqMw=="
                    )
                    SocialMediaIcon(
                        R.drawable.ic_tiktok,
                        "https://www.tiktok.com/@todas.brillamos?_t=8qDNNVLY1kz&_r=1"
                    )
                    SocialMediaIcon(
                        R.drawable.ic_web,
                        "https://zazilrrr.org/catalogo/zazil/index.php#"
                    )
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
    // Diálogo de confirmación para eliminar la cuenta
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmación", color = Color(0xFFE17F61)) },  // Cambia el color del título
            text = { Text(text = "¿Estás seguro que deseas eliminar tu cuenta?", color = Color(0xFF191919)) },  // Texto del cuerpo
            confirmButton = {
                Button(
                    onClick = {
                        uid?.let {
                            settingsViewModel.deleteAccount(
                                uid = it,
                                onSuccess = {
                                    Log.d("SettingsScreen", "Cuenta eliminada exitosamente")
                                    navController.navigate("login")  // Navega a la pantalla de login tras la eliminación
                                },
                                onError = { errorMessage ->
                                    Log.e("SettingsScreen", "Error al eliminar la cuenta: $errorMessage")
                                }
                            )
                        } ?: Log.e("SettingsScreen", "Error: UID no encontrado")
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))  // Botón de confirmación en rosita
                ) {
                    Text(text = "Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancelar", color = Color(0xFFE17F61))  // Texto de cancelación en rosita
                }
            },
            containerColor = Color.White,  // Fondo del diálogo en rosita claro
        )
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
    })

    {
        Spacer(modifier = Modifier.height(50.dp))
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
