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


//REVISAR SI SE USA






@Composable
fun ActualizarDatosScreen(viewModel: UpdateDataViewModel = viewModel(), navController: NavHostController) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Variables para almacenar los datos de entrada del usuario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Estado de la respuesta de la actualización
    val updateResult by viewModel.updateResult.observeAsState()

    // Obtener el uid del usuario autenticado
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen decorativa en la parte superior derecha
        Image(
            painter = painterResource(id = R.drawable.logo),  // Cambia al recurso adecuado
            contentDescription = "Decoración",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
        )

        // Columna que contiene el formulario de actualización de datos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
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

            // Botón "Regresar"
            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "< Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

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

            // Mostrar el mensaje de éxito o error
            updateResult?.let {
                Text(
                    text = it,
                    color = if (it.contains("exitosamente")) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
}
