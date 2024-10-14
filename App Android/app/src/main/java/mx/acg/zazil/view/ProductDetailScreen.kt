package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.CartViewModel
import mx.acg.zazil.viewmodel.ProductDetailViewModel

/**
 * Composable para mostrar los detalles de un producto.
 * Incluye la imagen, precio, descripción y un botón para agregar el producto al carrito.
 *
 * @param productId El ID del producto que se va a mostrar.
 * @param productDetailViewModel ViewModel para manejar la lógica de los detalles del producto.
 * @param navController Controlador de navegación para mover entre pantallas.
 * @param modifier Modificador para ajustar el diseño de la pantalla.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun ProductDetailScreen(
    productId: Int,
    productDetailViewModel: ProductDetailViewModel = viewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Observar el producto seleccionado
    val selectedProduct by productDetailViewModel.selectedProduct.observeAsState()
    val cartViewModel = viewModel<CartViewModel>()
    var showConfirmationMessage by remember { mutableStateOf(false) }

    // Llama al ViewModel para cargar el producto por su ID
    LaunchedEffect(productId) {
        productDetailViewModel.loadProductById(productId)
    }

    // Si el producto está seleccionado, muestra los detalles
    selectedProduct?.let { product ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFEFEEEE))
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado con logotipo y nombre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                    .background(Color(0xFFFEE1D6))
                    .padding(vertical = 16.dp)
            ) {
                // Logotipo de la aplicación y nombre
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
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ZAZIL",
                        fontSize = 24.sp,
                        fontFamily = gabaritoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                }
            }

            // Botón "Regresar"
            TextButton(onClick = { navController.navigate("catalog") }) {
                Text(
                    text = "< Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre del producto
            Text(
                text = product.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE17F61),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de la imagen del producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Imagen
                Image(
                    painter = rememberImagePainter(data = product.imagen),
                    contentDescription = "Imagen del Producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .aspectRatio(1.5f)
                        .clip(RoundedCornerShape(24.dp))
                )

                // Texto "Ir al carrito"
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd // Alinea el texto a la derecha
                ) {
                    TextButton(onClick = { navController.navigate("cart") }) {
                        Text(
                            text = "Ir al carrito",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            fontFamily = gabaritoFontFamily,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }

            // Sección de precio y botón de agregar al carrito
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // Precio del producto
                        Text(
                            text = String.format("$%.2f", product.precio_rebajado),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = gabaritoFontFamily,
                            color = Color(0xFF191919)
                        )
                        product.precio_normal?.let {
                            Text(
                                text = String.format("$%.2f", it),
                                fontSize = 16.sp,
                                color = Color(0xFFFF5757),
                                fontFamily = gabaritoFontFamily,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Botón de agregar al carrito
                    Button(
                        onClick = {
                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid

                            if (uid != null) {
                                // Llama al método para agregar el producto al carrito
                                cartViewModel.addToCart(productId, uid)

                                // Cambia el estado para mostrar el mensaje de confirmación
                                showConfirmationMessage = true

                            } else {
                                Log.e(
                                    "ProductDetailScreen",
                                    "Error: No se pudo obtener el UID del usuario."
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart_w),
                                contentDescription = "Carrito",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Agregar al carrito",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontFamily = gabaritoFontFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp)) // Espacio entre el botón y el aviso de agregado

                // Muestra el mensaje de confirmación "Agregado"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp), // Reserva espacio para el mensaje "Agregado"
                    contentAlignment = Alignment.CenterEnd // Coloca el mensaje alineado a la derecha
                ) {
                    // Muestra un mensaje emergente si showConfirmationMessage es true
                    if (showConfirmationMessage) {
                        LaunchedEffect(Unit) {
                            // Espera 3 segundos antes de ocultar el mensaje
                            kotlinx.coroutines.delay(2000)
                            showConfirmationMessage = false
                        }

                        // Aviso de agregado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End, // Alinea al centro
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.check_icon),
                                contentDescription = "Agregado",
                                modifier = Modifier.size(15.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp)) // Ajusta el valor para reducir el espacio

                            Text(
                                text = "Agregado",
                                color = Color(0xFF39B54A),
                                fontSize = 18.sp,
                                fontFamily = gabaritoFontFamily,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }

            // Descripción del producto
            Text(
                text = "Descripción",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = product.descripcion,
                fontSize = 18.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFF191919),
                modifier = Modifier.padding(16.dp)
            )
        }
    } ?: run {
        // Muestra un indicador de "Cargando producto..." mientras se obtiene el producto
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFE17F61))
        }
    }
}