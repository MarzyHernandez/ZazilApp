package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import mx.acg.zazil.viewmodel.ProductViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter

@Composable
fun ProductDetailScreen(
    productId: Int,
    productViewModel: ProductViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Llama al ViewModel para seleccionar el producto actual
    val selectedProduct = productViewModel.selectedProduct

    LaunchedEffect(Unit) {
        productViewModel.loadProducts() // Carga los productos al iniciar
        productViewModel.selectProductById(productId) // Selecciona el producto por su ID
    }

    if (selectedProduct != null) {
        // Aquí puedes utilizar selectedProduct para mostrar los datos en la pantalla
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
                    text = selectedProduct.nombre,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE17F61)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del producto
                Image(
                    painter = rememberImagePainter(data = selectedProduct.imagen), // Cargar la imagen desde la URL
                    contentDescription = "Imagen del Producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1.5f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción del producto
                Text(
                    text = selectedProduct.descripcion,
                    fontSize = 16.sp,
                    color = Color(0xFF191919)
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Precio del producto
                Text(
                    text = "$${selectedProduct.precio_normal}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )
                selectedProduct.precio_rebajado?.let {
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
    } else {
        Text(text = "Cargando producto...", modifier = Modifier.fillMaxSize())
    }
}

