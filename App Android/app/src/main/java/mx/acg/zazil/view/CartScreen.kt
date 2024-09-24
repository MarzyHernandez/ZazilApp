package mx.acg.zazil.view

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R

data class CartItem(
    val productName: String,  // Nombre del producto
    val productImageRes: Int,  // Recurso de la imagen del producto
    val quantity: Int,         // Cantidad de productos
    val price: Double          // Precio unitario del producto
)

@Composable
fun CartScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)) // Bordes redondeados solo en la parte inferior
                .background(Color(0xFFFEE1D6))  // Color de fondo rosa
                .padding(vertical = 16.dp) // Ajuste de padding interno
        ) {
            // Encabezado con título y carrito dentro del recuadro rosa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp), // Agregamos padding al inicio
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Carrito ",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919),
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart_w),
                    contentDescription = "Carrito",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterVertically)  // Alinea el ícono verticalmente con el texto
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de productos en el carrito
        val cartItems = listOf(
            CartItem("Toalla algodón", R.drawable.prod1, 3, 150.0),
            CartItem("Toalla algodón", R.drawable.prod1, 3, 150.0),
            CartItem("Toalla algodón", R.drawable.prod1, 3, 150.0),
            CartItem("Toalla algodón", R.drawable.prod1, 3, 150.0)
        )

        cartItems.forEach { item ->
            CartItemRow(item = item)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Total y botones de acción
        CartTotal(navController = navController, total = 1800)
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFEBEBEB), shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Image(
            painter = painterResource(id = item.productImageRes),
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(10.dp)
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Detalles del producto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.productName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFFE17F61)
            )
            Text(text = "Cantidad: ${item.quantity}")
            Text(
                text = "Precio: $${item.price}",
                fontWeight = FontWeight.Bold)
        }

        // Botones para añadir y quitar cantidades (uno sobre otro)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 20.dp)
        ) {
            // Botón añadir (ic_add)
            IconButton(
                onClick = { /* Acción para añadir */ },
                modifier = Modifier
                    .size(36.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Añadir",
                    modifier = Modifier
                        .size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón quitar (ic_remove)
            IconButton(
                onClick = { /* Acción para quitar */ },
                modifier = Modifier.size(36.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "Quitar",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun CartTotal(navController: NavHostController, total: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Total: \$$total",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón para seguir comprando
            Button(
                onClick = { /* Acción para seguir comprando */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp) // Ajuste de la altura
            ) {
                Text(
                    text = "SEGUIR COMPRANDO",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth(),  // Ocupa todo el ancho disponible
                    textAlign = TextAlign.Center
                )
            }

            // Botón para finalizar la compra
            Button(
                onClick = {navController.navigate("endShopping")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f) // Ambos botones tienen el mismo peso
                    .height(52.dp) // Ajuste de la altura
            ) {
                Text(
                    text = "CONTINUAR",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
