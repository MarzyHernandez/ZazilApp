package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import mx.acg.zazil.model.Product
import mx.acg.zazil.viewmodel.CartViewModel
import mx.acg.zazil.viewmodel.ProductViewModel

@Composable
fun CartScreen(
    navController: NavHostController,
    uid: String,  // UID del usuario
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    // Observar el estado del carrito
    val cart by cartViewModel.cart.observeAsState()

    // Estado para almacenar los productos cargados
    val loadedProducts = remember { mutableStateMapOf<Int, Product>() }

    // Estado para manejar si aún se están cargando productos
    var isLoading by remember { mutableStateOf(true) }

    // Estado para saber si las solicitudes ya se hicieron
    var isProductLoadingStarted by remember { mutableStateOf(false) }

    // Cargar el carrito al inicio
    LaunchedEffect(uid) {
        cartViewModel.loadCartByUid(uid)
    }

    // Cargar detalles de los productos del carrito solo una vez
    LaunchedEffect(cart) {
        cart?.let {
            val productos = it.productos

            if (productos.isNotEmpty() && !isProductLoadingStarted) {
                // Marcamos que la carga de productos ha comenzado
                isProductLoadingStarted = true
                isLoading = true  // Empezamos cargando

                // Hacemos las solicitudes de productos
                productos.forEach { cartProduct ->
                    if (!loadedProducts.containsKey(cartProduct.id_producto)) {
                        // Cargamos el producto y lo añadimos al mapa
                        productViewModel.loadProductById(cartProduct.id_producto) { product ->
                            loadedProducts[cartProduct.id_producto] = product

                            // Desactivar la carga cuando todos los productos hayan sido cargados
                            if (loadedProducts.size == productos.size) {
                                isLoading = false
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(0.dp)
    ) {
        // Encabezado del carrito
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
                    text = "Carrito",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    color = Color(0xFF191919)

                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart_w),
                    contentDescription = "Carrito",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Mostrar indicador de carga si los productos aún se están cargando
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Usamos LazyColumn para que la lista de productos sea desplazable
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)  // Esto asegura que la lista de productos ocupe solo el espacio restante
                ) {
                    items(cart?.productos ?: emptyList()) { cartProduct ->
                        loadedProducts[cartProduct.id_producto]?.let { product ->
                            CartItemRow(
                                productId = cartProduct.id_producto,
                                productName = product.nombre,
                                productImageUrl = product.imagen,
                                quantity = cartProduct.cantidad,
                                price = product.precio_rebajado,
                                onAddClicked = { /* Acción para aumentar la cantidad */ },
                                onRemoveClicked = { /* Acción para disminuir la cantidad */ }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            // Total del carrito y botones de acción
            cart?.let {
                CartTotal(navController = navController, total = it.monto_total)
            }
        }
    }
}




@Composable
fun CartItemRow(
    productId: Int,  // Añadimos el productId como parámetro
    productName: String,
    productImageUrl: String,
    quantity: Int,
    price: Double,
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    val cartViewModel = viewModel<CartViewModel>()
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFEBEBEB), shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del producto
        Image(
            painter = rememberImagePainter(productImageUrl),
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
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = productName,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                fontSize = 20.sp,
                color = Color(0xFFE17F61)
            )
            Text(text = "Cantidad: $quantity")
            Text(
                text = "Precio: $$price",
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 20.dp)
        ) {
            // Botón añadir (ic_add)
            IconButton(
                onClick = {
                    if (uid != null) {
                        cartViewModel.addToCart(productId, uid)
                    } else {
                        // Manejo del caso en que el usuario no esté autenticado
                        Log.e("CartItemRow", "Error: No se pudo obtener el UID del usuario.")
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Añadir",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón quitar (ic_remove)
            IconButton(
                onClick = {
                    if (uid != null) {
                        cartViewModel.removeFromCart(productId, uid)  // Llama a removeFromCart cuando se hace clic en quitar
                    } else {
                        // Manejo del caso en que el usuario no esté autenticado
                        Log.e("CartItemRow", "Error: No se pudo obtener el UID del usuario.")
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "Quitar",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}



@Composable
fun CartTotal(navController: NavHostController, total: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Total: \$$total",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = gabaritoFontFamily,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("catalog")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .padding(8.dp)
            ) {
                Text(text = "SEGUIR COMPRANDO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black)
            }

            Button(
                onClick = {
                    val calle = ""  // Puedes reemplazar estos valores por los reales cuando se obtengan
                    val numeroInterior = ""
                    val colonia = ""
                    val codigoPostal = ""
                    val ciudad = ""
                    val estado = ""
                    val pais = ""
                    navController.navigate("endShopping/$total/$calle/$numeroInterior/$colonia/$codigoPostal/$ciudad/$estado/$pais")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(text = "CONTINUAR",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    color = Color.White)
            }
        }
    }
}