package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import mx.acg.zazil.viewmodel.CatalogViewModel
import androidx.navigation.NavHostController

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,  // Recibe el NavController
    catalogViewModel: CatalogViewModel = viewModel() // Inyectamos el ViewModel
) {
    val products = catalogViewModel.products.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        catalogViewModel.getProducts()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEE1D6))
            .padding(16.dp)
    ) {
        Column {
            // Header
            Text(
                text = "Productos",
                fontSize = 28.sp,
                color = Color(0xFFE17F61),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Grid de productos con 2 columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(products.value) { product ->
                    ProductItem(
                        title = product.nombre,
                        price = "$${product.precio_normal}",
                        imageUrl = product.imagen,
                        productId = product.id,  // Pasamos el ID del producto
                        onClick = { navController.navigate("productDetail/${product.id}") }  // Navegar a la pantalla de detalles
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    title: String,
    price: String,
    imageUrl: String,
    productId: Int,
    onClick: () -> Unit,  // Nueva función de clic para navegar
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)  // Altura fija para las tarjetas
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable { onClick() }  // Se ejecuta cuando el usuario hace clic en la tarjeta
    ) {
        // Imagen del producto utilizando Coil para cargar imágenes desde URL
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Imagen del Producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)  // Altura fija para las imágenes
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Título del producto
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF545454),
            maxLines = 3 // Limita las líneas del título para que no afecte el layout
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Precio del producto
        Text(
            text = price,
            fontSize = 16.sp,
            color = Color(0xFFE17F61)
        )
    }
}
