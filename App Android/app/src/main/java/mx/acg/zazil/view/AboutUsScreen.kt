package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import mx.acg.zazil.ui.theme.Typography

@Composable
fun AboutUsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEEEE))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado con el título de la pantalla
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                .background(Color(0xFFFEE1D6))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Sobre Nosotros",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF191919)
            )
        }

        // Botón "Regresar"
        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "< Regresar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de "¿Quiénes Somos?"
        Text(
            text = "¿QUIÉNES SOMOS?",
            style = Typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Zazil es una marca comprometida con el bienestar de las mujeres y el cuidado del medio ambiente. Su misión es proporcionar soluciones innovadoras y sostenibles para el período menstrual. ¿Cómo lo hacen? A través de la creación de toallas femeninas reutilizables.",
            style = Typography.bodyMedium,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "“Cambiando el mundo, un ciclo a la vez.”",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de "Empoderamiento Económico"
        Text(
            text = "EMPODERAMIENTO ECONÓMICO",
            style = Typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Bloque de Ahorro a Largo Plazo
        AboutUsCard(
            title = "Ahorro a Largo Plazo",
            content = "Al invertir en Zazil, estás invirtiendo en un producto que dura. Olvídate de compras mensuales; nuestras toallas son una inversión que ahorra dinero con el tiempo."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Bloque de Oportunidades de Emprendimiento
        AboutUsCard(
            title = "Oportunidades de Emprendimiento",
            content = "Zazil apoya programas que proporcionan oportunidades de emprendimiento para mujeres locales, contribuyendo así al empoderamiento económico en comunidades de todo el mundo."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AboutUsCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEE1D6), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE17F61)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = Typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
}
