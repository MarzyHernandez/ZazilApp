package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.ui.theme.Typography



@Composable
fun TyC(navController: NavHostController, modifier: Modifier = Modifier) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(0.dp)
        ) {
            // Encabezado del carrito
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp))
                    .background(Color(0xFFFEE1D6))
                    .padding(vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Términos y condiciones",
                        fontSize = 28.sp,
                        color = Color(0xFF191919),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp) // Espacio a la izquierda
                    )
                }
            }

            // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("configuracion") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido de los Términos y Condiciones
        TyCCard(
            title = "Uso de la aplicación",
            content = "Al utilizar nuestra aplicación de tienda en línea, usted acepta cumplir con los siguientes términos y condiciones. Nuestra plataforma proporciona un espacio para la compra de productos a través de su dispositivo Android."
        )

        Spacer(modifier = Modifier.height(8.dp))

        TyCCard(
            title = "Cambios en los productos",
            content = "Los precios, la disponibilidad de productos, y las ofertas especiales están sujetos a cambios sin previo aviso. Nos reservamos el derecho de modificar o descontinuar la aplicación en cualquier momento sin responsabilidad alguna hacia usted."
        )

        Spacer(modifier = Modifier.height(8.dp))

        TyCCard(
            title = "Responsabilidad del usuario",
            content = "Es su responsabilidad asegurarse de que la información de su cuenta y los datos proporcionados para la compra sean precisos y estén actualizados."
        )

        Spacer(modifier = Modifier.height(8.dp))

        TyCCard(
            title = "Actividades ilegales",
            content = "El uso indebido de nuestra aplicación, como el intento de fraude o cualquier otra actividad ilegal, resultará en la cancelación de su cuenta y podría ser reportado a las autoridades competentes."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TyCCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE17F61)
                ),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = Typography.bodyMedium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF333333),
                textAlign = TextAlign.Justify  // Justificación del texto
            )
        }
    }
}