package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import mx.acg.zazil.R

/**
 * Función que representa la pantalla de inicio de sesión.
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */

@Composable
fun LoginScreen() {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hola!",
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

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Text(
                text = "Olvidé mi contraseña",
                color = Color(0xFFE27F61),
                fontSize = 14.sp,
                fontFamily = gabaritoFontFamily,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(alignment = Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Lógica de inicio de sesión */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
            ) {
                Text(text = "Iniciar sesión", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿No tienes cuenta?", fontSize = 14.sp)

                TextButton(onClick = { /* Lógica de registro */ }) {
                    Text(text = "Regístrate", color = Color(0xFFE27F61), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "o regístrate vía",
                fontFamily = gabaritoFontFamily,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Lógica de registro con Google */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC7C5))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), // Asegura que ocupe el ancho completo
                    horizontalArrangement = Arrangement.SpaceBetween, // Distribuye el espacio entre los elementos
                    verticalAlignment = Alignment.CenterVertically // Centra verticalmente el texto y el ícono
                ) {
                    // Texto alineado a la izquierda
                    Text(text = "Google", color = Color.Black)

                    // Ícono alineado a la derecha
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}