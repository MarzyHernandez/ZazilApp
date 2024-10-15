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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.acg.zazil.viewmodel.SettingsViewModel

/**
 * Pantalla de ajustes para la aplicación. Permite al usuario realizar acciones como
 * cerrar sesión, actualizar datos, eliminar la cuenta, y acceder a información sobre
 * la aplicación. También incluye opciones para navegar a secciones de la app
 * relacionadas con términos, créditos, y aviso de privacidad.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param modifier Modificador opcional para personalizar el diseño de la pantalla.
 * @param settingsViewModel ViewModel para manejar la lógica relacionada con las configuraciones.
 *
 * @author Alberto Cebreros González
 * @autor Melissa Mireles Rendón
 */
@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid
    var showDialog by remember { mutableStateOf(false) }

    // Variable para controlar la visibilidad del diálogo de cierre de sesión
    var showSignOutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado
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

            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                // Opción "Cerrar sesión"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            showSignOutDialog = true
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
                        fontFamily = gabaritoFontFamily,
                        color = Color(0xFF191919)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección "Mi Cuenta"

                Text(
                    text = "MI CUENTA",
                    fontSize = 16.sp,
                    fontFamily = gabaritoFontFamily,
                    color = Color(0xFF545454),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                SettingOption(title = "Actualizar Datos", iconResId = R.drawable.ic_edit) {
                    navController.navigate("updateData")
                }

                SettingOption(title = "Eliminar Cuenta", iconResId = R.drawable.ic_delete) {
                    showDialog = true
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección "Acerca de"
                Text(
                    text = "ACERCA DE",
                    fontSize = 16.sp,
                    fontFamily = gabaritoFontFamily,
                    color = Color(0xFF545454),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                SettingOption(title = "Sobre Nosotros", iconResId = R.drawable.ic_about) {
                    navController.navigate("aboutUs")
                }

                SettingOption(title = "FAQs", iconResId = R.drawable.ic_faq) {
                    navController.navigate("FAQs")
                }

                SettingOption(title = "Términos y Condiciones", iconResId = R.drawable.ic_terms) {
                    navController.navigate("TyC")
                }

                SettingOption(title = "Política de Privacidad", iconResId = R.drawable.ic_privacy){
                    navController.navigate("privacyPolicy")
                }

                SettingOption(title = "Créditos", iconResId = R.drawable.ic_credits) {
                    navController.navigate("credits")
                }

                Spacer(modifier = Modifier.height(50.dp))

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
                    text = "v1.0.0",
                    fontSize = 12.sp,
                    fontFamily = gabaritoFontFamily,
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
            title = { Text(text = "Confirmación", color = Color(0xFFE17F61)) },
            text = { Text(text = "¿Estás seguro que deseas eliminar tu cuenta?", color = Color(0xFF191919), fontFamily = gabaritoFontFamily)},
            confirmButton = {
                Button(
                    onClick = {
                        uid?.let {
                            settingsViewModel.deleteAccount(
                                uid = it,
                                onSuccess = {
                                    Log.d("SettingsScreen", "Cuenta eliminada exitosamente")
                                    navController.navigate("login")
                                },
                                onError = { errorMessage ->
                                    Log.e("SettingsScreen", "Error al eliminar la cuenta: $errorMessage")
                                }
                            )
                        } ?: Log.e("SettingsScreen", "Error: UID no encontrado")
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                ) {
                    Text(text = "Eliminar", color = Color.White, fontFamily = gabaritoFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancelar", color = Color(0xFFE17F61), fontFamily = gabaritoFontFamily)
                }
            },
            containerColor = Color.White
        )
    }

    // Diálogo de confirmación para cerrar sesión
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text(text = "Cerrar sesión", color = Color(0xFFE17F61), fontFamily = gabaritoFontFamily) },
            text = {
                Text(
                    text = "¿Estás seguro que deseas cerrar sesión?",
                    color = Color(0xFF191919),
                    fontFamily = gabaritoFontFamily
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Cerrar sesión y navegar a la pantalla de inicio de sesión
                        Log.d("SettingsScreen", "Cerrando sesión...")
                        auth.signOut()
                        navController.navigate("login")
                        showSignOutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                ) {
                    Text(text = "Cerrar sesión", color = Color.White, fontFamily = gabaritoFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text(text = "Cancelar", color = Color(0xFFE17F61), fontFamily = gabaritoFontFamily)
                }
            },
            containerColor = Color.White
        )
    }
}

/**
 * Composable para mostrar una opción de configuración con un título y un icono.
 *
 * @param title Título de la opción.
 * @param iconResId Identificador de recurso del icono.
 * @param onClick Callback a ejecutar al hacer clic en la opción.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun SettingOption(title: String, iconResId: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
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
        Text(text = title, fontSize = 16.sp, color = Color(0xFF191919), fontFamily = gabaritoFontFamily)
    }
}

/**
 * Composable para mostrar un icono de red social que abre un enlace en el navegador.
 *
 * @param iconResId Identificador de recurso del icono.
 * @param url Enlace a la red social.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun SocialMediaIcon(iconResId: Int, url: String) {
    val context = LocalContext.current
    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }) {
        Box(
            modifier = Modifier
                .size(50.dp)
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
