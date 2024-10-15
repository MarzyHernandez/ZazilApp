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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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

/**
 * Pantalla de carrito de compras que muestra los productos añadidos por el usuario,
 * permite incrementar o disminuir la cantidad, y proceder al pago.
 * Incluye también la opción de mostrar un mensaje de "Carrito Vacío" si no hay productos.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param uid UID del usuario actual para cargar su carrito.
 * @param cartViewModel ViewModel para manejar la lógica del carrito.
 * @param productViewModel ViewModel para manejar la lógica de los productos.
 *
 * @author Alberto Cebreros González
 * @autor Melissa Mireles Rendón
 */
@Composable
fun CartScreen(
    navController: NavHostController,
    uid: String,
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

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
                isLoading = true

                // Hacemos las solicitudes de productos
                productos.forEach { cartProduct ->
                    if (!loadedProducts.containsKey(cartProduct.id_producto)) {
                        productViewModel.loadProductById(cartProduct.id_producto) { product ->
                            loadedProducts[cartProduct.id_producto] = product

                            // Desactivar la carga cuando todos los productos hayan sido cargados
                            if (loadedProducts.size == productos.size) {
                                isLoading = false
                            }
                        }
                    }
                }
            } else {
                // Si no hay productos, no está cargando más
                isLoading = false
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

        // Botón "Regresar"
        TextButton(
            onClick = { navController.popBackStack()},
        ) {
            Text(
                text = "< Regresar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
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
                CircularProgressIndicator(color = Color(0xFFE17F61), modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (cart?.productos.isNullOrEmpty()) {
                    // Mostrar mensaje de carrito vacío si no hay productos
                    Text(
                        text = "Carrito Vacío",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontFamily = gabaritoFontFamily,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    // Usamos LazyColumn para que la lista de productos sea desplazable
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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
            }

            // Total del carrito y botones de acción
            cart?.let {
                if (!cart!!.productos.isNullOrEmpty()) {
                    CartTotal(navController = navController, total = it.monto_total)
                }
            }
        }
    }
}

/**
 * Composable que representa una fila en la lista del carrito, mostrando el producto y
 * opciones para ajustar la cantidad.
 *
 * @param productId Identificador del producto.
 * @param productName Nombre del producto.
 * @param productImageUrl URL de la imagen del producto.
 * @param quantity Cantidad del producto en el carrito.
 * @param price Precio del producto.
 * @param onAddClicked Acción cuando se aumenta la cantidad.
 * @param onRemoveClicked Acción cuando se disminuye la cantidad.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun CartItemRow(
    productId: Int,
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
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 16.dp)
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
                text = "Precio: \$${String.format("%.2f", price)}",
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 20.dp)
        ) {
            // Botón añadir
            IconButton(
                onClick = {
                    if (uid != null) {
                        cartViewModel.addToCart(productId, uid)
                    } else {
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

            // Botón quitar
            IconButton(
                onClick = {
                    if (uid != null) {
                        cartViewModel.removeFromCart(productId, uid)
                    } else {
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

            Spacer(modifier = Modifier.height(8.dp))

            // Botón borrar producto
            IconButton(
                onClick = {
                    if (uid != null) {
                        cartViewModel.deleteFromCart(productId, uid)
                    } else {
                        Log.e("CartItemRow", "Error: No se pudo obtener el UID del usuario.")
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete_product),
                    contentDescription = "Borrar",
                    modifier = Modifier.size(28.dp)
                )
            }

        }
    }
}

/**
 * Composable que muestra el total del carrito y botones para seguir comprando o proceder al pago.
 *
 * @param navController Controlador de navegación para redirigir a otras pantallas.
 * @param total Total del monto en el carrito.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun CartTotal(navController: NavHostController, total: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Total: \$${String.format("%.2f", total)}",
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
            ) {
                Text(
                    text = "SEGUIR COMPRANDO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }

            Button(
                onClick = {
                    val calle = ""
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
                Text(
                    text = "COMPRAR",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    color = Color.White
                )
            }
        }
    }
}
