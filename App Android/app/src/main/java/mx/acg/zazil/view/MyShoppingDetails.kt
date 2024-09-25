package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MyShoppingDetailsScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Encabezado con título y botón "Regresar"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                .background(Color(0xFFFEE1D6))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Detalles",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919),
            )
        }
        Column {
            // Botón "Regresar"
            TextButton(
                onClick = { navController.navigateUp() }, // Navegar hacia atrás
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp
                )
            ) {
                Text(
                    text = "← Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resumen del Pedido
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "RESUMEN DEL PEDIDO   #MX3325",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "FECHA: 02/09/2024",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ESTATUS:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                    Text(
                        text = "ENTREGADO",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50), // Verde
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                    Text(
                        text = "$448.00",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Productos
        Text(
            text = "2 PRODUCTOS",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF191919)
        )

        Spacer(modifier = Modifier.height(8.dp))

        repeat(2) {
            ProductDetailCard(
                productName = "Toalla algodón",
                quantity = 3,
                price = 369.00
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductDetailCard(productName: String, quantity: Int, price: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        ) {
            // Aquí puedes poner la imagen del producto
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Detalles del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = productName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )
            Text(
                text = "Cantidad: $quantity",
                fontSize = 14.sp,
                color = Color(0xFF191919)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Precio del producto
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "PRECIO",
                fontSize = 14.sp,
                color = Color(0xFF191919),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$price",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
        }
    }
}


