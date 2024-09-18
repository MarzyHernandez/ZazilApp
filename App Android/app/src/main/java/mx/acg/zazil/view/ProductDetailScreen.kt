package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import mx.acg.zazil.viewmodel.ProductViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun ProductDetailScreen(
    productId: Int,
    productViewModel: ProductViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Observa el producto seleccionado
    val selectedProduct = productViewModel.selectedProduct.observeAsState()

    // Llama al ViewModel para cargar el producto por su ID
    LaunchedEffect(productId) {
        productViewModel.loadProductById(productId) // Usa el nuevo endpoint para obtener el producto
    }

    // Si el producto está seleccionado, mostrar los detalles
    selectedProduct.value?.let { product ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFEFEEEE))
                .verticalScroll(rememberScrollState())  // Hacer la pantalla desplazable
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Título del producto
                Text(
                    text = product.nombre,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE17F61)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del producto
                Image(
                    painter = rememberImagePainter(data = product.imagen), // Cargar la imagen desde la URL
                    contentDescription = "Imagen del Producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1.5f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción del producto
                Text(
                    text = product.descripcion,
                    fontSize = 16.sp,
                    color = Color(0xFF191919)
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Precio del producto
                Text(
                    text = "$${product.precio_normal}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )

                product.precio_rebajado?.let {
                    Text(
                        text = "$$it",
                        fontSize = 16.sp,
                        color = Color(0xFFFF5757),
                        modifier = Modifier.padding(top = 4.dp),
                        textDecoration = TextDecoration.LineThrough  // Añade la línea para simular el descuento
                    )
                }
            }
        }
    } ?: run {
        // Muestra un texto de "Cargando producto..." si no se encuentra el producto seleccionado
        Text(text = "Cargando producto...", modifier = Modifier.fillMaxSize())
    }
}
