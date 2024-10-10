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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.ShoppingHistory
import mx.acg.zazil.viewmodel.ShoppingHistoryViewModel

/**
 * Pantalla para mostrar el historial de compras del usuario.
 *
 * Esta pantalla muestra una lista de todas las compras que ha realizado el usuario.
 * Cada elemento de la lista incluye el número de pedido, la cantidad de productos,
 * la fecha y el monto total del pedido. También permite navegar a los detalles del pedido.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param viewModel ViewModel que gestiona la lógica del historial de compras.
 * @param uid ID del usuario autenticado para recuperar su historial de compras.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun MyShoppingScreen(
    navController: NavHostController,
    viewModel: ShoppingHistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    uid: String
) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))


    // Obteniendo el historial de compras desde el ViewModel
    val shoppingHistory by viewModel.shoppingHistory.observeAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Ejecuta el request para obtener las compras
    LaunchedEffect(Unit) {
        viewModel.getShoppingHistory(uid)
    }

    // Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
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

        // Mostrar mensaje de error si hay alguno
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Mostrar un indicador de cargando si el historial está vacío
        if (shoppingHistory.isEmpty() && errorMessage == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Lista de compras
            shoppingHistory.forEach { order ->
                OrderItemRow(navController = navController, order = order, uid = uid)
            }
        }
    }
}

/**
 * Elemento de la lista que muestra un pedido en el historial de compras.
 *
 * Cada fila incluye información como el número de pedido, la cantidad de productos,
 * la fecha del pedido y el monto total. Al hacer clic en la fila, se navega a los detalles del pedido.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param order Pedido del historial de compras.
 * @param uid ID del usuario autenticado.
 */
@Composable
fun OrderItemRow(
    navController: NavHostController,
    order: ShoppingHistory,
    uid: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFEBEBEB), shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("shoppingDetails/${order.id}/$uid")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Image(
            painter = painterResource(id = R.drawable.shopping_bag),
            contentDescription = "Imagen del producto",
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
            // Número de pedido
            Text(
                text = "Pedido #${order.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFE17F61)
            )
            // Cantidad de productos
            Text(text = "Productos: ${order.productos.size}")

            // Fecha del pedido, recortando la hora
            val formattedDate = order.fecha_pedido.substringBefore("T")
            Text(text = "Fecha: $formattedDate")

            // Monto total del pedido
            Text(
                text = "Total: $${order.monto_total}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}