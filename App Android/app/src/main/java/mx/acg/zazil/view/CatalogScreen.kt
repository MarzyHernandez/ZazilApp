package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import mx.acg.zazil.viewmodel.CatalogViewModel
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import mx.acg.zazil.R

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    catalogViewModel: CatalogViewModel = viewModel(),
    uid: String
) {
    val products = catalogViewModel.products.observeAsState(initial = emptyList())

    // Estado para la consulta de búsqueda
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filtrar los productos según la consulta de búsqueda
    val filteredProducts = if (searchQuery.text.isEmpty()) {
        products.value
    } else {
        products.value.filter { product ->
            product.nombre.contains(searchQuery.text, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        catalogViewModel.getProducts()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEE1D6)) // Color de fondo rosa para la parte superior
    ) {
        Column {
            // Encabezado con logo y texto "ZAZIL"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                    .background(Color(0xFFFEE1D6))
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logotipo",
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ZAZIL",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                }
            }

            // Aquí aplicamos el fondo blanco para la sección de productos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White) // Fondo blanco desde esta sección
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Productos",
                        fontSize = 28.sp,
                        color = Color(0xFFE17F61),
                        modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
                    )
                    // Barra de búsqueda
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar productos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        }
                    )

                    // Grid de productos con 2 columnas
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductItem(
                                title = product.nombre,
                                price = "$${product.precio_normal}",
                                imageUrl = product.imagen,
                                productId = product.id,
                                onClick = { if (uid.isNotBlank()) {
                                    navController.navigate("productDetail/${product.id}")
                                } else {
                                    Log.e("Navigation", "El UID está vacío, no se puede navegar a ProductDetailScreen")
                                }
                                }
                            )
                        }
                    }
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
    onClick: () -> Unit,  // Función de clic para navegar
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
