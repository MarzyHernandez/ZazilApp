package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Composable que representa un campo de entrada de texto simple.
 * Permite gestionar tanto texto como contraseñas y visualiza una línea divisoria.
 *
 * @param value El valor actual del campo de texto.
 * @param onValueChange Función que se invoca cuando el valor del campo cambia.
 * @param label Etiqueta del campo de texto.
 * @param isPassword Indica si el campo es para contraseñas (true) o texto normal (false).
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun SimpleTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
        decorationBox = { innerTextField ->
            Column {
                // Campo de texto donde el usuario puede escribir
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(label, color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField() // Aquí se muestra el texto que el usuario escribe
                }
                // Línea debajo del campo de texto
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    )
}

/**
 * Composable que representa la pantalla de inicio de sesión.
 * Contiene los campos para email, contraseña, y botones para iniciar sesión o registrarse.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param signInWithGoogle Función que maneja la autenticación con Google.
 * @param viewModel ViewModel utilizado para manejar el estado del login.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel(), signInWithGoogle: () -> Unit) {
    // Definir la fuente personalizada
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    var uid by remember { mutableStateOf<String?>(null) }

    // Variables que almacenan el email y contraseña ingresados por el usuario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observa el ID del usuario y los mensajes de error desde el ViewModel
    val userId by viewModel.userId.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Imagen de logo
        Image(
            painter = painterResource(id = R.drawable.mid_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(height = 300.dp, width = 150.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Mensaje de bienvenida
            Text(
                text = "¡Hola!",
                fontSize = 45.sp,
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

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el mensaje y el formulario

            // Formulario de inicio de sesión
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(text = "Completa tus datos", fontSize = 16.sp, fontFamily = gabaritoFontFamily, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el mensaje y el formulario

                // Input de email
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text(
                                text = "Email",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                // Campo para escribir email
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input de contraseña
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    // Cambiamos entre ocultar y mostrar la contraseña según el valor de passwordVisible
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        Column {
                            Text(
                                text = "Contraseña",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Campo de texto para la contraseña
                                    Box(Modifier.weight(1f)) {
                                        innerTextField()
                                    }
                                    // Icono de "ojito" para mostrar/ocultar la contraseña
                                    IconButton(
                                        onClick = { passwordVisible = !passwordVisible }, // Cambia el estado
                                        modifier = Modifier
                                            .size(24.dp) // Ajusta el tamaño del botón para hacerlo más pequeño
                                            .align(Alignment.CenterVertically) // Alinea el icono verticalmente con el texto
                                    ) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            }
                            // Línea debajo del campo de contraseña
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Enlace para restablecer contraseña
                Text(
                    text = "Olvidé mi contraseña",
                    color = Color(0xFFE27F61),
                    fontSize = 14.sp,
                    fontFamily = gabaritoFontFamily,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .wrapContentWidth(Alignment.End)
                        .clickable {
                            // Navega a la pantalla de recuperar contraseña
                            navController.navigate("recuperarContrasena")
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        // Llama al método de login en el ViewModel
                        viewModel.loginWithEmail(
                            email = email,
                            password = password,
                            onSuccess = {
                                // Navegar a la pantalla de catálogo si la autenticación es exitosa
                                navController.navigate("catalog") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onFailure = { error ->
                                // Actualiza el mensaje de error en el ViewModel
                                viewModel.setErrorMessage("Error en la autenticación: $error")
                                Log.e("Login", "Error en la autenticación: $error")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(text = "INICIAR SESIÓN", fontFamily = gabaritoFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Muestra el id del usuario o error
                userId?.let {
                    Text(text = "Inicio de sesión Exitoso", color = Color.Green, modifier = Modifier.padding(top = 8.dp))
                }
                errorMessage?.let {
                    Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // Opciones adicionales para crear una cuenta o iniciar sesión con Google
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Mensaje de inicio de sesión con Google
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        fontSize = 16.sp,
                        fontFamily = gabaritoFontFamily,
                        color = Color.Black
                    )

                    // Botón para navegar a la pantalla de registro
                    TextButton(onClick = { navController.navigate("register") }) {
                        Text(
                            text = "Regístrate",
                            color = Color(0xFFE27F61),
                            fontSize = 16.sp,
                            fontFamily = gabaritoFontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mensaje de inicio de sesión con Google
                Text(
                    text = "o regístrate vía",
                    fontFamily = gabaritoFontFamily,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Centrado
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro con Google
            Button(
                onClick = { signInWithGoogle() },  // Aquí se llama a la función pasada por parámetro
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Google", color = Color.Black, fontFamily = gabaritoFontFamily, fontSize = 16.sp)
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

