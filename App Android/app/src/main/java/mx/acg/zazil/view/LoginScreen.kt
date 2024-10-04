package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Composable para mostrar un campo de entrada de texto simple.
 *
 * @param value El valor actual del campo de texto.
 * @param onValueChange Función que se invoca cuando el valor del campo cambia.
 * @param label Texto que se muestra como etiqueta dentro del campo.
 * @param isPassword Indica si el campo es para una contraseña y debe ocultar los caracteres.
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
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param signInWithGoogle Función que maneja la autenticación con Google.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel(), signInWithGoogle: () -> Unit) {

    val user = FirebaseAuth.getInstance().currentUser

    // Simulamos que el usuario se ha logueado correctamente
    if (user != null) {
        val uid = user.uid
        Log.d("LoginScreen", "UID encontrado en LoginScreen: $uid")
    }

    // Definir la fuente personalizada
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Variables que almacenan el email y contraseña ingresados por el usuario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var uid by remember { mutableStateOf<String?>(null) }

    // Observa el ID del usuario y los mensajes de error desde el ViewModel
    val userId by viewModel.userId.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Crop
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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )
            Text(
                text = "Te damos la bienvenida a Zazil",
                fontSize = 16.sp,
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

                Text(text = "Completa tus datos", fontSize = 14.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el mensaje y el formulario

                // Input de email
                SimpleTextInput(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input de contraseña
                SimpleTextInput(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    isPassword = true
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
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        // Llama al método de login en el ViewModel
                        viewModel.loginWithEmail(email, password) {
                            // Navegar a la pantalla de catálogo si la autenticación es exitosa
                            navController.navigate("catalog/$uid") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(text = "INICIAR SESIÓN")
                }

                // Muestra el id del usuario o error
                userId?.let { Text(text = "User ID: $it", color = Color.Green) }
                errorMessage?.let { Text(text = it, color = Color.Red) }
            }

            // Opciones adicionales para crear una cuenta o iniciar sesión con Google
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
                        text = "¿No tienes cuenta?",
                        fontSize = 14.sp,
                        fontFamily = gabaritoFontFamily
                    )

                    // Botón para navegar a la pantalla de registro
                    TextButton(onClick = {navController.navigate("register")} ) {
                        Text(
                            text = "Regístrate",
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
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Centrado
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro con Google
            Button(
                onClick = { signInWithGoogle() },
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
