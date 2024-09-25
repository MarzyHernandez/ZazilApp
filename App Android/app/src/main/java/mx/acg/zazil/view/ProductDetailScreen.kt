package mx.acg.zazil.view

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import mx.acg.zazil.R

@Composable
fun ProductDetailScreen(productId: Int,
                        productViewModel: ProductViewModel = viewModel(),
                        modifier: Modifier = Modifier
) {
    // Observa el producto seleccionado
    val selectedProduct = productViewModel.selectedProduct.observeAsState()

    // Llama al ViewModel para cargar el producto por su ID
    LaunchedEffect(productId) {
        productViewModel.loadProductById(productId)
    }

    // Si el producto está seleccionado, muestra los detalles
    selectedProduct.value?.let { product ->
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
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)) // Bordes redondeados solo en la parte inferior
                    .background(Color(0xFFFEE1D6))  // Color de fondo rosa
                    .padding(vertical = 16.dp) // Ajuste de padding interno
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp) // Tamaño del círculo
                            .clip(CircleShape) // Forma circular
                            .background(Color.White) // Fondo blanco del círculo
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logotipo",
                            modifier = Modifier
                                .size(40.dp) // Tamaño del logo
                                .align(Alignment.Center) // Centrar el logo dentro del círculo
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp)) // Espacio entre el logo y el texto
                    Text(
                        text = "ZAZIL",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                }
            }
            // Botón "Regresar"
            TextButton(
                onClick = {},
            ) {
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
                    onClick = { /* Acción para agregar al carrito */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Alinea el icono y el texto verticalmente
                        horizontalArrangement = Arrangement.Center // Centra los elementos dentro del botón
                    ) {
                        // Icono del carrito
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart_w), // Reemplaza con el recurso del icono de carrito
                            contentDescription = "Carrito",
                            tint = Color.White, // Cambia el color del icono
                            modifier = Modifier.size(20.dp) // Ajusta el tamaño del icono si es necesario
                        )

                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto

                        // Texto de agregar al carrito
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
        // Muestra un texto de "Cargando producto..." si no se encuentra el producto seleccionado
        Text(text = "Cargando producto...", modifier = Modifier.fillMaxSize())
    }
}

