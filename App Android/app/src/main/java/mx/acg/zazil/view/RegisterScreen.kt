package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
 * La pantalla de registro incluye campos para el nombre, apellido, correo, teléfono y contraseña del usuario.
 * Además, permite la aceptación de los términos y condiciones antes de proceder con el registro.
 *
 * En caso de que el registro sea exitoso, se muestra un diálogo de confirmación y la opción para iniciar sesión.
 * En caso de errores, se notifica al usuario del problema.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @property gabaritoFontFamily Fuente personalizada utilizada en la interfaz.
 * @property nombre Estado mutable para almacenar el nombre del usuario.
 * @property apellido Estado mutable para almacenar el apellido del usuario.
 * @property email Estado mutable para almacenar el correo del usuario.
 * @property telefono Estado mutable para almacenar el teléfono del usuario.
 * @property password Estado mutable para almacenar la contraseña del usuario.
 * @property termsAccepted Estado mutable para verificar si los términos han sido aceptados.
 * @property errorMessage Estado mutable para mostrar mensajes de error al usuario.
 * @property showDialog Estado mutable para controlar la visibilidad del diálogo de éxito.
 * @property showTermsDialog Estado mutable para controlar la visibilidad del diálogo de términos y condiciones.
 * @property registerResult Resultado del registro observado desde el ViewModel.
 * @property isPasswordValid Función que verifica si la contraseña cumple con los requisitos mínimos.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun RegisterScreen(navController: NavHostController) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    val viewModel: RegisterViewModel = viewModel()
    val registerResult by viewModel.registerResult.observeAsState()

    /**
     * Función para validar la contraseña.
     *
     * La contraseña debe tener al menos 6 caracteres e incluir al menos un número.
     *
     * @param password Contraseña a validar.
     * @return Verdadero si la contraseña es válida, falso en caso contrario.
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 && password.any { it.isDigit() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.mid_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(height = 200.dp, width = 100.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Regístrate",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )
            Text(
                text = "Te damos la bienvenida a Zazil",
                fontSize = 18.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Completa tus datos",
                    fontSize = 16.sp,
                    fontFamily = gabaritoFontFamily,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campos de entrada de datos
                BasicTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Nombre(s)", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Apellido", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Correo", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Teléfono", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (!isPasswordValid(it)) {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres e incluir al menos un número."
                        } else {
                            errorMessage = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Contraseña", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE27F61))
                    )
                    Text(text = "Acepto", fontSize = 14.sp, fontFamily = gabaritoFontFamily, color = Color.Black)
                    TextButton(onClick = { showTermsDialog = true }) {
                        Text(text = "términos y condiciones", color = Color(0xFFE27F61), fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de resultado del registro
                registerResult?.let {
                    if (it.contains("exitoso")) {
                        showDialog = true
                    } else {
                        errorMessage = it
                    }
                }

                errorMessage?.let {
                    Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!termsAccepted) {
                            errorMessage = "Debes aceptar los términos y condiciones"
                        } else if (!isPasswordValid(password)) {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres y un número"
                        } else {
                            errorMessage = null
                            viewModel.registerUser(
                                User(email = email, password = password, nombres = nombre, apellidos = apellido, telefono = telefono)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(text = "REGISTRAR", fontFamily = gabaritoFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Diálogo de registro exitoso
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Registro Exitoso", color = Color(0xFFE17F61)) },
            text = { Text(text = "Tu cuenta ha sido creada exitosamente.", color = Color(0xFF191919)) },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate("login")
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                    ) {
                        Text(text = "Iniciar sesión", color = Color.White)
                    }
                }
            },
            containerColor = Color.White
        )
    }

    // Diálogo de términos y condiciones
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text(text = "Términos y Condiciones", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Al utilizar nuestra aplicación...",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Cerrar", color = Color(0xFFE27F61))
                }
            }
        )
    }
}
