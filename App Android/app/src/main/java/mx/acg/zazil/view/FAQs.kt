package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.FAQItem
import mx.acg.zazil.viewmodel.FAQViewModel

// Fuente personalizada utilizada en toda la pantalla
val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

/**
 * Pantalla que muestra una lista de Preguntas Frecuentes (FAQs) utilizando datos obtenidos
 * del `FAQViewModel`. La pantalla incluye un indicador de carga mientras se recuperan
 * las preguntas, así como una interfaz interactiva para expandir y contraer las respuestas.
 *
 * @param navController Controlador de navegación para manejar la navegación entre pantallas.
 * @param modifier Modificador opcional para personalizar el diseño de la pantalla.
 * @param faqViewModel ViewModel que proporciona los datos de las preguntas frecuentes.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun FAQs(navController: NavHostController, modifier: Modifier = Modifier, faqViewModel: FAQViewModel = viewModel()) {
    // Observa los elementos de la lista de preguntas frecuentes desde el ViewModel
    val faqItems by faqViewModel.faqItems // Observa los datos obtenidos del ViewModel
    // Observa el estado de carga desde el ViewModel
    val isLoading by faqViewModel.isLoading // Observa el estado de carga desde el ViewModel

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Sección superior personalizada con bordes redondeados en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFEE1D6),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.CenterStart // Alinea el texto a la izquierda
        ) {
            Text(
                text = "FAQs",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF191919),
            )
        }

        // Botón "Regresar"
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold, fontFamily = gabaritoFontFamily,)
        }

        // Título "Preguntas Frecuentes" alineado a la izquierda
        Text(
            text = "PREGUNTAS FRECUENTES",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontFamily = gabaritoFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            color = Color.Black
        )

        // Mostrar loader si las FAQs están cargando
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFE17F61), // Color personalizado para el loader
                    modifier = Modifier.size(50.dp)
                )
            }
        } else {
            // Mostrar las FAQs obtenidas del ViewModel
            faqItems.forEach { faq ->
                FAQCard(faq = faq)
                Spacer(modifier = Modifier.height(8.dp)) // Menor espacio entre tarjetas
            }
        }
    }
}

/**
 * Composable que representa una tarjeta para mostrar una pregunta frecuente.
 * La tarjeta se puede expandir para revelar la respuesta.
 *
 * @param faq Objeto `FAQItem` que contiene la pregunta y la respuesta.
 */
@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    // Tarjeta con contenido expandido
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Color del fondo cambiado a #F5F5F5
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado de la tarjeta con el botón de expansión
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.pregunta.uppercase(), // Pregunta en mayúsculas
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                // Botón de expansión para expandir/contraer la respuesta
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = Color(0xFFE17F61), // Icono con color #E17F61
                        modifier = Modifier.size(32.dp) // Iconos más grandes
                    )
                }
            }
            if (expanded) {
                Text(
                    text = faq.respuesta.uppercase(), // Respuesta en mayúsculas
                    fontSize = 16.sp,
                    fontFamily = gabaritoFontFamily,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
