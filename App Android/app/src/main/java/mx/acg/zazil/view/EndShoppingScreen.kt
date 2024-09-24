package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@Composable
fun EndShoppingScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado con título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                .background(Color(0xFFFEE1D6))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Finalizar compra",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919),
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Envío y Pago
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto de "ENVÍO" con el mismo peso que "PAGO"
            TextButton(
                onClick = { navController.navigate("endShopping") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // Asegura el mismo padding
            ) {
                Text(
                    text = "ENVÍO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE17F61), // Texto resaltado
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Centro el texto
                )
            }

            // Divider separado
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp),
                color = Color.Gray
            )

            // Texto de "PAGO" con el mismo peso que "ENVÍO"
            TextButton(
                onClick = { navController.navigate("payment") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp) // Asegura el mismo padding
            ) {
                Text(
                    text = "PAGO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF999999), // Texto gris
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Centro el texto
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Datos del cliente
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Datos del cliente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto
            var nombre by remember { mutableStateOf("") }
            var apellidos by remember { mutableStateOf("") }
            var correo by remember { mutableStateOf("") }
            var calle by remember { mutableStateOf("") }
            var colonia by remember { mutableStateOf("") }
            var codigoPostal by remember { mutableStateOf("") }
            var ciudad by remember { mutableStateOf("") }
            var telefono by remember { mutableStateOf("") }


            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre(s)") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = calle,
                onValueChange = { calle = it },
                label = { Text("Calle y número") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = colonia,
                onValueChange = { colonia = it },
                label = { Text("Colonia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = codigoPostal,
                onValueChange = { codigoPostal = it },
                label = { Text("Código Postal") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono móvil") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de continuar
            Button(
                onClick = { /* Acción para continuar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "CONTINUAR",
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

