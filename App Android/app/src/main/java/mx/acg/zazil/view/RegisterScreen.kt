package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import mx.acg.zazil.R

@Composable
fun RegisterScreen(navController: NavHostController) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Declaramos las variables para cada input del registro
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier
        .fillMaxSize()) {
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Regístrate",
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

                // Input de Contraseña
                SimpleTextInput(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox para aceptar términos y condiciones
                var termsAccepted by remember { mutableStateOf(false) }
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

                    TextButton(onClick = { /* Acción para ir al login */ }) {
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
                            // Lógica para registrar al usuario
                        } else {
                            errorMessage = "Debes aceptar los términos y condiciones"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(
                        text = "REGISTRAR",
                        color = Color.Black,)
                }

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
