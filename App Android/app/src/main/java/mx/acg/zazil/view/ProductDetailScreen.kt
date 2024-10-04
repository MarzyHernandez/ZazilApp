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
import mx.acg.zazil.viewmodel.ProductViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.Product
import mx.acg.zazil.viewmodel.CartViewModel
import mx.acg.zazil.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    productDetailViewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(), // CartViewModel inyectado
    uid: String,
    modifier: Modifier = Modifier
) {
    // Observar el producto seleccionado
    val selectedProduct by productDetailViewModel.selectedProduct.observeAsState()

    // Verificación y log del `uid`
    Log.d("ProductDetailScreen", "UID recibido: $uid")


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

            // Botón "Regresar"
            TextButton(onClick = {}) {
                Text(
                    text = "< Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            // Nombre del producto
            Text(
                text = product.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del producto
            Image(
                painter = rememberImagePainter(data = product.imagen),
                contentDescription = "Imagen del Producto",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Precio y botón de agregar al carrito
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Botón de agregar al carrito
                Button(
                    onClick = {
                        if (uid.isNotBlank()) {
                            Log.d("ProductDetailScreen", "UID encontrado: $uid")  // Registro para verificar el UID
                            cartViewModel.addProductToCart(
                                uid = uid,
                                productId = product.id,
                                quantity = 1
                            )
                        } else {
                            Log.e("ProductDetailScreen", "El UID está vacío, no se puede agregar al carrito")
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
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción del producto
            Text(
                text = "Descripción",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = product.descripcion,
                fontSize = 16.sp,
                color = Color(0xFF191919),
                modifier = Modifier.padding(16.dp)
            )
        }
    } ?: run {
        // Muestra un indicador de "Cargando producto..." mientras se obtiene el producto
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}



