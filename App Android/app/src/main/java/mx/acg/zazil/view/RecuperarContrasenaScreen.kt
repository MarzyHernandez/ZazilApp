package mx.acg.zazil.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

/**
 * Pantalla de recuperación de contraseña que permite al usuario solicitar un correo
 * para restablecer su contraseña en la aplicación de Zazil.
 *
 * El usuario puede ingresar su correo electrónico, y al presionar el botón "CAMBIAR CONTRASEÑA",
 * se envía un correo de recuperación a través del servicio de Firebase Authentication.
 *
 * En caso de que el correo sea válido, se notifica al usuario mediante un Toast.
 * Si ocurre un error o el correo está vacío, también se muestra un mensaje.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun RecuperarContrasenaScreen(navController: NavHostController) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estados mutables para almacenar los valores ingresados y los mensajes de error
    var email by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener el contexto actual de la aplicación, necesario para mostrar Toasts
    val context = LocalContext.current



    /**
     * Función que envía un correo de recuperación de contraseña utilizando Firebase.
     *
     * Si el correo es válido, se muestra un Toast indicando que el correo ha sido enviado.
     * Si ocurre un error, se muestra el mensaje de error en un Toast.
     *
     * @param email El correo electrónico al que se enviará el enlace de recuperación.
     */
    fun enviarCorreoDeRecuperacion(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Correo de recuperación enviado a $email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al enviar el correo: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Contenido de la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen logo
        Image(
            painter = painterResource(id = R.drawable.mid_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(height = 300.dp, width = 150.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )

        // Contenido del formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Botón "Regresar"
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "<",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Espacio entre la flecha y el texto
                    Text(
                        text = "Regresar",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            // Título de la pantalla
            Text(
                text = "Recuperar",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black,
                modifier = Modifier.padding(start = 6.dp)
            )

            // Subtítulo
            Text(
                text = "Contraseña",
                fontSize = 16.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario de recuperación
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Escribe tu correo",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Correo
                SimpleTextInput(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de Recuperar Contraseña
                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            enviarCorreoDeRecuperacion(email)
                            errorMessage = null
                        } else {
                            errorMessage = "El correo está vacío."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(text = "CAMBIAR CONTRASEÑA", color = Color.White)
                }

                // Mostrar mensaje de error si es necesario
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
}