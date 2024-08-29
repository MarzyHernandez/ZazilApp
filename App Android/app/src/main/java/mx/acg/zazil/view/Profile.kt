import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import mx.acg.zazil.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()  // Ocupa la pantalla completa
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.background_profile),
            contentDescription = "Fondo superior",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // Ajusta la imagen al tamaño de la pantalla
        )

        // Contenido superpuesto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Hace que la columna sea desplazable
                .padding(bottom = 80.dp)  // Deja espacio para la barra de navegación
        ) {
            // Espacio vacío inicial para darle distancia al contenido superior
            Spacer(modifier = Modifier.height(80.dp))

            // Texto "Perfil"
            Text(
                text = "Mi perfil",
                fontSize = 36.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(80.dp))
            // Imagen de perfil centrada
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(width = 5.dp, color = Color(0xFFEBB7A7), shape = CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scarlett),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))  // Espacio entre imagen y formulario

            ProfileForm(
                Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))  // Espacio entre el formulario y los botones

            // Botón Ver Historial de Compra
            Button(
                onClick = { /* Acción del botón Ver Historial de Compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEEEE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Ver Historial de Compra", color = Color(0xFFE17F61), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Eliminar cuenta
            Button(
                onClick = { /* Acción del botón Eliminar cuenta */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Eliminar cuenta", color = Color(0xFF293392), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))  // Asegura un espaciado adecuado al final del contenido
        }

        // Barra de navegación fija
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)  // Fija la barra en la parte inferior
                .background(
                    color = Color(0xFFEBB7A7),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Acción del icono Home */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_w),
                    contentDescription = "Home",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Acción del icono Carrito */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart_w),
                    contentDescription = "Carrito",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Acción del icono Perfil */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_w),
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Acción del icono Configuración */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_w),
                    contentDescription = "Configuración",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ProfileForm(modifier: Modifier = Modifier) {
    val nombre = remember { mutableStateOf("Ana Sofía Vázquez Delgado") }
    val telefono = remember { mutableStateOf("5567892234") }
    val correo = remember { mutableStateOf("ana.vazquez@gmail.com") }
    val contrasena = remember { mutableStateOf("********") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recuadro Nombre
            ProfileInputField(
                label = "Nombre",
                value = nombre.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { nombre.value = it }
            )

            // Recuadro Teléfono
            ProfileInputField(
                label = "Teléfono",
                value = telefono.value,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                onValueChange = { telefono.value = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recuadro Correo
            ProfileInputField(
                label = "Correo",
                value = correo.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onValueChange = { correo.value = it }
            )

            // Recuadro Contraseña
            ProfileInputField(
                label = "Contraseña",
                value = contrasena.value,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                onValueChange = { contrasena.value = it }
            )
        }
    }
}

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFEFEEEE), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE17F61)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
