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
import mx.acg.zazil.R
import mx.acg.zazil.model.User
import mx.acg.zazil.viewmodel.RegisterViewModel


/**
 * Pantalla de registro que permite al usuario introducir sus datos personales, aceptar términos y condiciones,
 * y realizar el registro en el servidor remoto a través de Retrofit.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @author Melissa Mireles Rendón
 */
@Composable
fun RegisterScreen(navController: NavHostController) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Variables para almacenar los datos de entrada del usuario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }  // Estado del checkbox de términos y condiciones
    var errorMessage by remember { mutableStateOf<String?>(null) }  // Estado del checkbox de términos y condiciones

    // Alcance de las coroutines
    val coroutineScope = rememberCoroutineScope()

    val viewModel: RegisterViewModel = viewModel()

    val registerResult by viewModel.registerResult.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen del logo en la parte superior derecha
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Crop
        )

        // Columna que contiene el formulario de registro
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la pantalla
            Text(
                text = "Regístrate",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )
            // Mensaje de bienvenida
            Text(
                text = "Te damos la bienvenida a Zazil",
                fontSize = 16.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el título y el formulario

            // Formulario de registro
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Completa tus datos",
                    fontSize = 14.sp,
                    color = Color.Black)

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

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox para aceptar términos y condiciones
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE27F61))
                    )
                    Text(
                        text = "Acepto",
                        fontSize = 14.sp,
                        color = Color.Black)

                    TextButton(onClick = { }) {
                        Text(
                            text = "términos y condiciones",
                            color = Color(0xFFE27F61),
                            fontSize = 14.sp,
                            fontFamily = gabaritoFontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro
                Button(
                    onClick = {
                        if (termsAccepted) {
                            val user = User(
                                email = email,
                                password = password,
                                nombres = nombre,
                                apellidos = apellido,
                                telefono = telefono
                            )
                            viewModel.registerUser(user)
                        } else {
                            errorMessage = "Debes aceptar los términos y condiciones"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(
                        text = "REGISTRAR",
                        color = Color.Black
                    )
                }

                registerResult?.let {
                    Text(
                        text = it,
                        color = if (it.contains("exitoso")) Color.Green else Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Mostrar el mensaje de error si aplica
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Ya tienes cuenta?",
                            fontSize = 14.sp,
                            fontFamily = gabaritoFontFamily
                        )

                        TextButton(onClick = {navController.navigate("login")}) {
                            Text(
                                text = "Iniciar sesión",
                                color = Color(0xFFE27F61),
                                fontSize = 14.sp,
                                fontFamily = gabaritoFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "o regístrate vía",
                        fontFamily = gabaritoFontFamily,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro con Google
                Button(
                    onClick = { /* Lógica para registro con Google */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Google", color = Color.Black)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}
