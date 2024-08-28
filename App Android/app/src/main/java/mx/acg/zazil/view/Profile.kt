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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())  // Hace que la columna sea desplazable
    ) {
        // Fondo superior semicircular
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(bottomEnd = 350.dp, bottomStart = 350.dp))
                .background(Color(0xFFFEE1D6)),
            contentAlignment = Alignment.Center // Alinea el contenido dentro del círculo
        ) {
            // Título "Mi perfil" centrado en el semicírculo
            Text(
                text = "Mi perfil",
                fontSize = 36.sp,
                color = Color(0xFF191919),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp) // Ajusta este valor para que el texto sea visible
            )
        }

        // Ajustamos la imagen para que esté centrada y en la posición correcta
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
                .offset(y = -100.dp)  // Para que la imagen esté en la posición adecuada
                .clip(CircleShape)  // Clip para hacer la figura circular
                .border(width = 5.dp, color = Color(0xFFEBB7A7), shape = CircleShape)  // Borde circular
                .background(Color.White),  // Hacemos que el fondo sea transparente
            contentAlignment = Alignment.Center
        ) {
            // Aquí puedes colocar la imagen de perfil real
            Image(
                painter = painterResource(id = R.drawable.scarlett), // reemplaza con la imagen real
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(2.dp)) // Añade un espacio entre la imagen y el formulario

        ProfileForm(Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el formulario y los botones

        // Botón de Ver Historial de Compra
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

        Spacer(modifier = Modifier.height(12.dp)) // Espacio entre los botones

        // Botón de Eliminar cuenta
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

        Spacer(modifier = Modifier.height(24.dp)) // Espacio para el pie de navegación

        // Íconos de navegación inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Acción del icono Home */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_home_w), contentDescription = "Home")
            }
            IconButton(onClick = { /* Acción del icono Carrito */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_cart_w), contentDescription = "Carrito")
            }
            IconButton(onClick = { /* Acción del icono Perfil */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_profile_w), contentDescription = "Perfil")
            }
            IconButton(onClick = { /* Acción del icono Configuración */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_settings_w), contentDescription = "Configuración")
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
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // Recuadro Nombre
            ProfileInputField(
                label = "Nombre",
                value = nombre.value,
                onValueChange = { nombre.value = it }
            )

            // Recuadro Teléfono
            ProfileInputField(
                label = "Teléfono",
                value = telefono.value,
                onValueChange = { telefono.value = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(45.dp)
        ) {
            // Recuadro Correo
            ProfileInputField(
                label = "Correo",
                value = correo.value,
                onValueChange = { correo.value = it }
            )

            // Recuadro Contraseña
            ProfileInputField(
                label = "Contraseña",
                value = contrasena.value,
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
