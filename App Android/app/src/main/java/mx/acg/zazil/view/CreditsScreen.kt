package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import mx.acg.zazil.R
import mx.acg.zazil.model.TeamMember

/**
 * Pantalla de créditos que muestra el logo de Zazil,
 * las imágenes e información de los integrantes del equipo en formato deslizable,
 * y la mención a la universidad.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
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

        // Título de la sección Créditos
        Text(
            text = "Créditos",
            fontSize = 28.sp,
            fontFamily = gabaritoFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEBB7A6),
            modifier = Modifier
                .fillMaxWidth() // Ocupa el ancho completo disponible
                .wrapContentWidth(Alignment.CenterHorizontally) // Centra el texto horizontalmente
        )

        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("configuracion") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold, fontFamily = gabaritoFontFamily,)
        }
        // Sección central con tarjetas deslizable tipo "story" para los integrantes del equipo
        StoryTeamSection()

        // Sección inferior con agradecimientos y mención a Campus Edo Mex
        FooterSection()
    }
}

/**
 * Sección superior que contiene el logo de Zazil y un fondo degradado suave.
 * Utiliza un degradado que va de un color rosa claro hacia un gris suave.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
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
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun StoryTeamSection() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Añade espacio horizontal
                .horizontalScroll(scrollState), // Habilita el desplazamiento horizontal
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre las tarjetas
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lista de integrantes del equipo
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

        Spacer(modifier = Modifier.height(8.dp))

        // Flechas indicadoras de scroll y barra debajo de las tarjetas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flecha izquierda indicadora de scroll
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val newValue = (scrollState.value - 300).coerceAtLeast(0)
                        scrollState.animateScrollTo(newValue)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Scroll Left",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp).padding(start = 8.dp)
                )
            }

            // Barra indicadora del desplazamiento horizontal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0)) // Color de fondo de la barra
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f))
                        .height(8.dp)
                        .background(Color(0xFFEBB7A6)) // Color del indicador
                )
            }

            // Flecha derecha indicadora de scroll
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val newValue = (scrollState.value + 300).coerceAtMost(scrollState.maxValue)
                        scrollState.animateScrollTo(newValue)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Scroll Right",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                )
            }
        }
    }
}

/**
 * Tarjeta individual tipo "story" para cada integrante del equipo.
 * Muestra la imagen, nombre y correo del integrante.
 *
 * @param member Datos del integrante (nombre, correo, imagen).
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Color de fondo blanco
    ) {
        Box(
            modifier = Modifier.fillMaxSize() //
        ) {
            // Imagen del integrante
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
                    .align(Alignment.BottomCenter) // Se alinea en la parte inferior de la tarjeta
                    .offset(y = (-4).dp) // Mueve el contenedor
                    .background(Color(0xFFEBB7A6)) // Fondo semitransparente para resaltar el texto
                    .fillMaxWidth() //
                    .padding(8.dp), // Espacio interno
                horizontalAlignment = Alignment.CenterHorizontally // El texto está centrado horizontalmente
            ) {
                Text(
                    text = member.name, // Nombre del integrante
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    fontSize = 18.sp,
                    color = Color.Black // Texto en color negro
                )
                Text(
                    text = member.email, // Correo del integrante
                    fontSize = 14.sp,
                    fontFamily = gabaritoFontFamily,
                    color = Color.Black, // Texto en color negro
                    textAlign = TextAlign.Center // Alineación centrada del texto
                )
            }
        }
    }
}

/**
 * Sección inferior que contiene el texto de agradecimiento y mención a Campus Edo Mex.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Alineación centrada
    ) {

        // Texto de créditos
        Text(
            text = "Desarrollado por estudiantes del programa de Ingeniería en Tecnologías Computacionales del Instituto Tecnológico y de Estudios Superiores de Monterrey®, Campus Estado de México.",
            fontSize = 12.sp, // Tamaño de fuente
            fontFamily = gabaritoFontFamily,
            color = Color.Gray, // Color gris del texto
            textAlign = TextAlign.Center // Alineación centrada
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el siguiente contenido
    }
}