package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import mx.acg.zazil.model.Order
import mx.acg.zazil.model.Product
import mx.acg.zazil.model.ProductDetails
import mx.acg.zazil.viewmodel.OrderViewModel

@Composable
fun MyShoppingDetailsScreen(
    navController: NavHostController,
    orderId: Int,  // Recibe el ID de la orden como parámetro
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Obtenemos la lista de órdenes desde el ViewModel
    val orders = viewModel.orders.observeAsState(initial = emptyList())

    // Buscamos la orden correspondiente al ID recibido
    val order = orders.value.find { it.id == orderId }

    Column(
        modifier = Modifier
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

        if (order != null) {
            // Mostrar resumen de la orden
            OrderSummary(order)

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos
            Text(
                text = "${order.productos.size} PRODUCTOS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar cada producto en la orden
            order.productos.forEach { product ->
                ProductDetailCard(product)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(
                text = "Orden no encontrada.",
                fontSize = 18.sp,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun OrderSummary(order: Order) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "RESUMEN DEL PEDIDO   #${order.id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "FECHA: ${order.fecha_pedido.split("T").first()}",
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
                    text = order.estado,
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50), // Verde para entregado
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
                    text = "$${order.monto_total}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
            }
        }
    }
}

@Composable
fun ProductDetailCard(product: ProductDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Image(
            painter = rememberImagePainter(data = product.imagen),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Detalles del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )
            Text(
                text = "Cantidad: ${product.cantidad}",
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
                text = "$${product.precio}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
        }
    }
}
