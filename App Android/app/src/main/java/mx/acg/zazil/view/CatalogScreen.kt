package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import mx.acg.zazil.R

/**
 * Pantalla de catálogo que muestra los productos disponibles.
 * Permite al usuario buscar productos por nombre y navegar a los detalles de cada producto.
 *
 * @param modifier Modificador para aplicar estilos y personalización.
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param catalogViewModel ViewModel que maneja los datos del catálogo de productos.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    catalogViewModel: CatalogViewModel = viewModel()
) {
    // Observa la lista de productos desde el ViewModel
    val products = catalogViewModel.products.observeAsState(initial = emptyList())
    val isLoading = catalogViewModel.isLoading.observeAsState(initial = false)

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

    // Llama al ViewModel para obtener los productos cuando se monta la composición
    LaunchedEffect(Unit) {
        catalogViewModel.getProducts()
    }

    // Estructura principal de la pantalla
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
    ) {
        if (isLoading.value) {
            // Muestra el loader mientras se están cargando los productos
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE17F61))
            }
        } else {
            // Muestra el contenido cuando los productos ya han cargado
            Column {
                // Encabezado con logo y texto "ZAZIL"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp))
                        .background(Color(0xFFFEE1D6))
                        .padding(vertical = 32.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Productos",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = gabaritoFontFamily,
                            color = Color(0xFF191919)
                        )
                    }
                }

                // Sección principal que contiene la barra de búsqueda y el grid de productos
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    Column {
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
                            },
                            shape = RoundedCornerShape(64.dp)
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
                                    price = "$${product.precio_rebajado}",
                                    imageUrl = product.imagen,
                                    productId = product.id,
                                    onClick = { navController.navigate("productDetail/${product.id}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente individual que representa un producto dentro del catálogo.
 * Muestra la imagen, el nombre y el precio del producto, y permite hacer clic para ver más detalles.
 *
 * @param title El nombre del producto.
 * @param price El precio del producto.
 * @param imageUrl URL de la imagen del producto.
 * @param productId ID único del producto.
 * @param onClick Función que se ejecuta cuando el usuario hace clic en el producto.
 * @param modifier Modificador para aplicar estilos y personalización.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
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
            .height(280.dp)  // Altura fija para las tarjetas
            .background(Color(0xFFEDEDED), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable { onClick() }  // Se ejecuta cuando el usuario hace clic en la tarjeta
    ) {
        // Imagen del producto utilizando Coil para cargar imágenes desde URL
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Imagen del Producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)  // Altura fija para las imágenes
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF545454),
            fontFamily = gabaritoFontFamily,
            maxLines = 3 // Limita las líneas del título para que no afecte el layout
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Precio del producto
        Text(
            text = price,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = gabaritoFontFamily,
            color = Color(0xFFE17F61)
        )
    }
}