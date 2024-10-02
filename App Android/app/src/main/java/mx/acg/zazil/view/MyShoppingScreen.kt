package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.ShoppingHistory
import mx.acg.zazil.viewmodel.ShoppingHistoryViewModel

@Composable
fun MyShoppingScreen(navController: NavHostController, viewModel: ShoppingHistoryViewModel, modifier: Modifier = Modifier) {
    val shoppingHistory by viewModel.shoppingHistory.observeAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                .background(Color(0xFFFEE1D6))  // Fondo rosa
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis compras",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919),
                )
            }
        }

        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("profile") },
        ) {
            Text(
                text = "< Regresar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }

        // Muestra un mensaje de error si lo hay
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red
            )
        } else {
            // Renderiza cada pedido si no hay errores
            shoppingHistory.forEach { order ->
                OrderItemRow(order = order, navController = navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OrderItemRow(navController: NavHostController, order: ShoppingHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFEBEBEB), shape = RoundedCornerShape(8.dp))
            .clickable {
        navController.navigate("shoppingDetails")},
    verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Image(
            painter = painterResource(id = R.drawable.shopping_bag),
            contentDescription = "Imagen fija del producto",
            modifier = Modifier
                .padding(20.dp)
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Detalles del pedido
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Pedido ${order.orderId}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFE17F61)
            )
            Text(text = "Productos: ${order.productQuantity}")
            Text(text = "Fecha: ${order.date}")
            Text(
                text = "Total: $${order.totalPrice}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

