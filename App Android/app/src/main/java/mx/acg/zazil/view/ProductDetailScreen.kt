package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.acg.zazil.R

@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier) {
    // Variable de estado para saber si el ícono de "like" ha sido seleccionado
    var isLiked by remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el encabezado y el botón de regresar

            // Botón de regresar
            Text(
                text = "< Regresar",
                color = Color(0xFF545454),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable { /* Acción para regresar */ }
            )

            // Título del producto
            Text(
                text = "Toalla algodón",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE17F61)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del producto
            Image(
                painter = painterResource(id = R.drawable.prod1), // Reemplaza con la imagen del producto
                contentDescription = "Imagen del Producto",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de precio y botón agregar al carrito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono de like y los precios
                Column {
                    Text(
                        text = "$150.00",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191919)
                    )
                    Text(
                        text = "$200.00",
                        fontSize = 16.sp,
                        color = Color(0xFFFF5757),
                        modifier = Modifier.padding(top = 4.dp),
                        textDecoration = TextDecoration.LineThrough  // Añade la línea para simular el descuento
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Espacio flexible entre los precios y el botón

                Button(
                    onClick = { /* Acción de agregar al carrito */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                    modifier = Modifier
                        .height(40.dp)
                        .width(200.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),  // Asegura que el botón ocupe todo el ancho disponible
                        horizontalArrangement = Arrangement.Center,  // Centra el contenido horizontalmente
                        verticalAlignment = Alignment.CenterVertically  // Alinea verticalmente el ícono y el texto
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart_w),
                            contentDescription = "Carrito",
                            modifier = Modifier
                                .size(22.dp), // Tamaño del ícono
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Espacio mínimo entre el ícono y el texto

                        Text(
                            text = "Agregar al carrito",
                            fontSize = 15.9.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Sección de descripción y detalles
            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF545454)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam sem, pretium in pretium vel, ullamcorper quis libero.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam sem, pretium in pretium vel, ullamcorper quis libero.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam sem, pretium in pretium vel, ullamcorper quis libero.",
                fontSize = 16.sp,
                color = Color(0xFF191919)
            )

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen()
}
