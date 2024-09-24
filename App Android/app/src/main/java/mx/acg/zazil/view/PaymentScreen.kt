package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import mx.acg.zazil.R

@Composable
fun PaymentScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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

        // Sección de Envío y Pago
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto de "ENVÍO" con el mismo peso que "PAGO"
            TextButton(
                onClick = { navController.navigate("endShopping") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // Asegura el mismo padding
            ) {
                Text(
                    text = "ENVÍO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF999999), // Texto gris
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Centro el texto
                )
            }

            // Divider separado
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp),
                color = Color.Gray
            )

            // Texto de "PAGO" con el mismo peso que "ENVÍO"
            TextButton(
                onClick = { navController.navigate("payment") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp) // Asegura el mismo padding
            ) {
                Text(
                    text = "PAGO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE17F61), // Texto resaltado
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Centro el texto
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dirección de envío
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DIRECCIÓN DE ENVÍO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MARIANA HERNÁNDEZ 55 8895 1362",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
                Text(
                    text = "Av. Paseo de la Reforma 222, Piso 5,\nJuárez, Cuauhtémoc, 06600, Ciudad de México",
                    fontSize = 14.sp,
                    color = Color(0xFF191919)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { navController.navigate("endShopping") }, // Aquí navega a la pantalla de envío
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Alinear a la derecha
                            .padding(end = 2.dp) // Reducir padding para pegar el texto
                    ) {
                        Text(
                            text = "EDITAR",
                            fontSize = 12.sp,
                            color = Color(0xFFE17F61),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Métodos de pago
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "ELIGE TU MÉTODO DE PAGO",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentMethodOption(
                methodName = "PAYPAL",
                description = "Compra en línea con tu cuenta.",
                iconRes = R.drawable.ic_paypal // Reemplaza con tu recurso de PayPal
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaymentMethodOption(
                methodName = "TARJETAS DE CRÉDITO O DÉBITO",
                description = "Visa, Mastercard",
                iconRes = R.drawable.ic_credit_card // Reemplaza con tu recurso de tarjeta de crédito
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total del pedido
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "TOTAL DEL PEDIDO",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )

            Text(
                text = "$486.00",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de finalizar compra
        Button(
            onClick = { /* Acción para finalizar compra */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "FINALIZAR COMPRA",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PaymentMethodOption(methodName: String, description: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono del método de pago
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = methodName,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre y descripción del método de pago
        Column {
            Text(
                text = methodName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

