package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.UpdateDataViewModel

/**
 * Pantalla de actualización de datos que permite al usuario modificar sus datos personales.
 * Incluye campos para nombre, apellido, correo, teléfono y contraseña.
 * Si la actualización es exitosa, muestra un diálogo de confirmación.
 *
 * @param viewModel ViewModel para manejar la lógica de actualización.
 * @param navController Controlador de navegación para cambiar entre pantallas.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun UpdateDataScreen(viewModel: UpdateDataViewModel = viewModel(), navController: NavHostController) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Variables para almacenar los datos de entrada del usuario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) } // Para controlar la visibilidad del diálogo de éxito

    // Estado de la respuesta de la actualización
    val updateResult by viewModel.updateResult.observeAsState()

    // Obtener el uid del usuario autenticado
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen decorativa en la parte superior derecha
        Image(
            painter = painterResource(id = R.drawable.mid_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(height = 200.dp, width = 100.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )

        // Columna que contiene el formulario de actualización de datos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Botón "Regresar"
            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "< Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(0.dp))
            // Título de la pantalla
            Text(
                text = "Actualizar",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )

            // Sección de "Datos"
            Text(
                text = "DATOS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Nombre
            SimpleTextInput(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre(s)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Apellido
            SimpleTextInput(
                value = apellido,
                onValueChange = { apellido = it },
                label = "Apellido"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Email
            SimpleTextInput(
                value = email,
                onValueChange = { email = it },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Teléfono
            SimpleTextInput(
                value = telefono,
                onValueChange = { telefono = it },
                label = "Teléfono"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Contraseña
            SimpleTextInput(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Actualizar
            Button(
                onClick = {
                    // Validar campos
                    if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank() && telefono.isNotBlank()) {
                        viewModel.updateUserData(uid, nombre, apellido, email, telefono, password)
                        errorMessage = null
                    } else {
                        errorMessage = "Todos los campos son obligatorios"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
            ) {
                Text(text = "ACTUALIZAR", color = Color.White)
            }

            // Mostrar el mensaje de error si existe
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    // Mostrar el diálogo de éxito si la actualización fue exitosa
    updateResult?.let {
        if (it.contains("exitosamente")) {
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Datos Actualizados Correctamente", color = Color(0xFFE17F61)) },
            text = { Text(text = "Tus datos han sido actualizados exitosamente.", color = Color(0xFF191919)) },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate("profile") // Navegar a la pantalla de perfil
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                    ) {
                        Text(text = "Ir a mi perfil", color = Color.White)
                    }
                }
            },
            containerColor = Color.White
        )
    }
}
