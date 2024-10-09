package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R

/**
 * Pantalla de créditos que muestra el logo de Zazil,
 * las imágenes e información de los integrantes del equipo en formato deslizable,
 * y la mención a la universidad.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun CreditsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
            .verticalScroll(rememberScrollState()) // Habilita el desplazamiento vertical
    ) {
        // Sección superior con el logo de Zazil y fondo degradado
        HeaderWithGradientLogo()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección central con tarjetas deslizable tipo "story" para los integrantes del equipo
        StoryTeamSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Sección inferior con agradecimientos y mención a Campus Edo Mex
        FooterSection()
    }
}

/**
 * Sección superior que contiene el logo de Zazil y un fondo degradado suave.
 * Utiliza un degradado que va de un color rosa claro hacia un gris suave.
 */
@Composable
fun HeaderWithGradientLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Altura del encabezado
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD9CE), Color(0xFFF5F5F5)) // Degradado rosa a gris claro
                )
            ),
        contentAlignment = Alignment.Center // Centra el contenido del Box
    ) {

        // Logo de Zazil en el centro
        Image(
            painter = painterResource(id = R.drawable.logo), // Reemplaza con el logo de Zazil
            contentDescription = "Logo Zazil",
            modifier = Modifier.size(150.dp) // Tamaño del logo
        )
    }
}

/**
 * Sección que muestra los integrantes del equipo en tarjetas tipo "story"
 * que pueden ser deslizadas horizontalmente. Cada tarjeta incluye la imagen
 * del integrante, su nombre y su correo electrónico.
 */
@Composable
fun StoryTeamSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(horizontal = 16.dp) // Añade espacio horizontal
            .horizontalScroll(rememberScrollState()), // Habilita el desplazamiento horizontal
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre las tarjetas
    ) {
        // Lista de miembros del equipo
        val teamMembers = listOf(
            TeamMember("ALMA CARPIO", "A01798523@tec.mx", R.drawable.alma),
            TeamMember("ALBERTO CEBREROS", "A01798671@tec.mx", R.drawable.alberto),
            TeamMember("MARIANA HERNÁNDEZ", "A01799263@tec.mx", R.drawable.mariana),
            TeamMember("CARLOS HERRERA", "A01798203@tec.mx", R.drawable.carlos),
            TeamMember("MELISSA MIRELES", "A01379736@tec.mx", R.drawable.melissa)
        )

        // Crear las tarjetas estilo "story" para cada integrante
        teamMembers.forEach { member ->
            TeamMemberStoryCard(member)
        }
    }
}

/**
 * Tarjeta individual tipo "story" para cada integrante del equipo.
 * Muestra la imagen, nombre y correo del integrante.
 * @param member Datos del integrante (nombre, correo, imagen).
 */
@Composable
fun TeamMemberStoryCard(member: TeamMember) {
    Card(
        modifier = Modifier
            .width(220.dp) // Ancho de la tarjeta estilo "story"
            .height(300.dp) // Alto de la tarjeta
            .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
            .shadow(8.dp, RoundedCornerShape(16.dp)), // Sombra para destacar la tarjeta
        shape = RoundedCornerShape(16.dp), // Forma de la tarjeta
        colors = CardDefaults.cardColors(containerColor = Color.White) // Color de fondo blanco
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // El contenido ocupa todo el tamaño de la tarjeta
        ) {
            // Imagen del miembro
            Image(
                painter = painterResource(id = member.imageRes),
                contentDescription = member.name, // Descripción de accesibilidad
                modifier = Modifier
                    .fillMaxSize() // La imagen ocupa toda la tarjeta
                    .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
            )

            // Nombre y correo encima de la imagen, en la parte inferior
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart) // Se alinea en la parte inferior de la tarjeta
                    .background(Color(0xAA635A57)) // Fondo semitransparente para resaltar el texto
                    .fillMaxWidth() // El fondo cubre todo el ancho de la tarjeta
                    .padding(8.dp), // Espacio interno
                horizontalAlignment = Alignment.CenterHorizontally // El texto está centrado horizontalmente
            ) {
                Text(
                    text = member.name, // Nombre del integrante
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black // Texto en color negro
                )
                Text(
                    text = member.email, // Correo del integrante
                    fontSize = 14.sp,
                    color = Color.Black, // Texto en color negro
                    textAlign = TextAlign.Center // Alineación centrada del texto
                )
            }
        }
    }
}

/**
 * Modelo de datos que representa a un miembro del equipo.
 * Incluye el nombre del integrante, su correo electrónico y el recurso de imagen.
 */
data class TeamMember(
    val name: String, // Nombre del integrante
    val email: String, // Correo del integrante
    val imageRes: Int // Recurso de la imagen
)

/**
 * Sección inferior que contiene el texto de agradecimiento y mención a Campus Edo Mex.
 */
@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Alineación centrada
    ) {
        Spacer(modifier = Modifier.height(30.dp)) // Espacio adicional antes del texto

        // Texto de agradecimiento
        Text(
            text = "Desarrollado por estudiantes del programa de Ingeniería en Tecnologías Computacionales del Instituto Tecnológico y de Estudios Superiores de Monterrey®, Campus Estado de México.",
            fontSize = 12.sp, // Tamaño de fuente
            color = Color.Gray, // Color gris del texto
            textAlign = TextAlign.Center // Alineación centrada
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el siguiente contenido
    }
}
