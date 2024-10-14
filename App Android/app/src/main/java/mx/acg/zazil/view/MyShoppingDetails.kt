package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import mx.acg.zazil.R
import mx.acg.zazil.model.Order
import mx.acg.zazil.viewmodel.OrderViewModel

/**
 * Pantalla que muestra los detalles de una orden específica, incluyendo los productos
 * y el resumen del pedido. También permite agregar productos al carrito.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param orderId ID de la orden que se mostrará.
 * @param uid ID del usuario que realizó la orden.
 * @param viewModel ViewModel que maneja la lógica de la orden.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun MyShoppingDetailsScreen(
    navController: NavHostController,
    orderId: Int,
    uid: String,
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Llamar al ViewModel para obtener las órdenes
    LaunchedEffect(Unit) {
        println("UID enviado desde la pantalla: $uid")
        viewModel.fetchOrders(uid)
    }

    // Obtenemos la lista de órdenes desde el ViewModel
    val orders by viewModel.orders.observeAsState(initial = emptyList())

    // Mostrar mensaje de error si existe
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Mostrar el mensaje de error si está presente
    if (errorMessage != null) {
        Text(errorMessage ?: "", color = Color.Red, fontFamily = gabaritoFontFamily)
    } else {
        // Buscar la orden correspondiente al ID recibido
        val order = orders.find { it.id == orderId }

        if (order != null) {
            // Mostrar el resumen de la orden
            OrderSummary(order)

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar los productos de la orden
            order.productos.forEach { product ->
                ProductDetailCard(product)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text("Orden no encontrada.", color = Color.Red, fontFamily = gabaritoFontFamily)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                fontFamily = gabaritoFontFamily,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigateUp() }
        ) {
            Text(
                text = "< Regresar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = gabaritoFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Verificar si hay error
        errorMessage?.let {
            Text(it, color = Color.Red)
        }

        // Buscar la orden con el ID específico
        val order = orders.firstOrNull { it.id == orderId }

        order?.let {
            // Mostrar resumen de la orden
            OrderSummary(order)

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos
            Text(
                text = "${order.productos.size} PRODUCTOS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF191919)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar cada producto en la orden
            order.productos.forEach { product ->
                ProductDetailCard(product)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } ?: run {
            Text("Orden no encontrada.", color = Color.Red, fontFamily = gabaritoFontFamily)
        }
    }
}


/**
 * Composable que muestra un resumen del pedido.
 *
 * @param order Orden cuyo resumen se va a mostrar.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
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
                fontFamily = gabaritoFontFamily,
                color = Color(0xFF191919)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "FECHA: ${order.fecha_pedido.split("T").first()}",
                fontSize = 14.sp,
                fontFamily = gabaritoFontFamily,
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
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
                Text(
                    text = order.estado,
                    fontSize = 14.sp,
                    fontFamily = gabaritoFontFamily,
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
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
                Text(
                    text = "$${String.format("%.2f", order.monto_total)}",
                    fontSize = 14.sp,
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
            }
        }
    }
}

/**
 * Composable que muestra los detalles de un producto en la orden.
 *
 * @param product Producto cuyos detalles se van a mostrar.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun ProductDetailCard(product: mx.acg.zazil.model.ProductDetails) {
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
                fontFamily = gabaritoFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )
            Text(
                text = "Cantidad: ${product.cantidad}",
                fontSize = 14.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFF191919)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Precio del producto
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "PRECIO",
                fontSize = 14.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFF191919),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${String.format("%.2f", product.precio)}",
                fontSize = 16.sp,
                fontFamily = gabaritoFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
        }
    }
}