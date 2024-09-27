package mx.acg.zazil.view

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mx.acg.zazil.R
import mx.acg.zazil.view.NavBar
import mx.acg.zazil.view.ProfileForm

/**
 * Composable que representa la pantalla de perfil del usuario.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 * Incluye la imagen de fondo, la foto de perfil, los datos del usuario y la barra de navegación.
 * @param [modifier] Modificador para personalizar la disposición y el estilo del Composable.
 */
@Composable
fun ProfileScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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

            // Texto "Mi Perfil"
            Text(
                text = "Mi perfil",
                fontSize = 36.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(80.dp))
            // Centra imagen de perfil
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
                onClick = {navController.navigate("myShopping")},
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

            // Botón Q&A
            Button(
                onClick = { navController.navigate("FAQs") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Preguntas Frecuentes", color = Color(0xFF293392), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))  // Asegura un espaciado adecuado al final del contenido
        }
    }
}

