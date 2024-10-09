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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.FAQItem
import mx.acg.zazil.model.FAQService
import mx.acg.zazil.viewmodel.FAQViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))




@Composable
fun FAQs(navController: NavHostController, modifier: Modifier = Modifier, faqViewModel: FAQViewModel = viewModel()) {
    val faqItems by faqViewModel.faqItems // Observa los datos obtenidos del ViewModel

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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
                color = Color(0xFF191919),
                modifier = Modifier.padding(start = 16.dp) // Espacio a la izquierda
            )
        }







        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("profile") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold)
        }

        // Título "Preguntas Frecuentes" alineado a la izquierda
        Text(
            text = "PREGUNTAS FRECUENTES",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontFamily = gabaritoFontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = Color.Black
        )

        // Mostrar las FAQs obtenidas del ViewModel
        faqItems.forEach { faq ->
            FAQCard(faq = faq)
            Spacer(modifier = Modifier.height(8.dp)) // Menor espacio entre tarjetas
        }
    }
}

@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

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

