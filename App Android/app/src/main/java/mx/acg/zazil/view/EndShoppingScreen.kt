package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EndShoppingScreen(
    navController: NavHostController,
    total: String,
    calle: String,
    numeroInterior: String,
    colonia: String,
    codigoPostal: String,
    ciudad: String,
    estado: String,
    pais: String,
    modifier: Modifier = Modifier
) {
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

        // Datos del cliente
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Datos del cliente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto con datos pre-llenados
            var updatedCalle by remember { mutableStateOf(calle) }
            var updatedNumeroInterior by remember { mutableStateOf(numeroInterior) }
            var updatedColonia by remember { mutableStateOf(colonia) }
            var updatedCodigoPostal by remember { mutableStateOf(codigoPostal) }
            var updatedCiudad by remember { mutableStateOf(ciudad) }
            var updatedEstado by remember { mutableStateOf(estado) }
            var updatedPais by remember { mutableStateOf(pais) }

            TextField(
                value = updatedCalle,
                onValueChange = { updatedCalle = it },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedNumeroInterior,
                onValueChange = { updatedNumeroInterior = it },
                label = { Text("Número Interior") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedColonia,
                onValueChange = { updatedColonia = it },
                label = { Text("Colonia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedCodigoPostal,
                onValueChange = { updatedCodigoPostal = it },
                label = { Text("Código Postal") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedCiudad,
                onValueChange = { updatedCiudad = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedEstado,
                onValueChange = { updatedEstado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = updatedPais,
                onValueChange = { updatedPais = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de continuar
            Button(
                onClick = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val uid = currentUser?.uid
                    if (uid != null) {
                        // Navegar a la pantalla de pago con los valores actualizados
                        navController.navigate(
                            "payment/$total/$updatedCalle/$updatedNumeroInterior/$updatedColonia/$updatedCodigoPostal/$updatedCiudad/$updatedEstado/$updatedPais"
                        )
                    } else {
                        Log.e("EndShoppingScreen", "Error: Usuario no autenticado, uid es null.")
                    }
                },
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
