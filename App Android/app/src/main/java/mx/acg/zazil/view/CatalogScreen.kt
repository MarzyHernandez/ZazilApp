package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.acg.zazil.R

@Composable
fun CatalogScreen(modifier: Modifier = Modifier) {
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

            // Lista de productos scrolleable
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(10) { // Simula 10 productos. Cambia esto a tu lista real de productos
                    ProductItem(
                        title = "Toalla algodón",
                        price = "$500.00",
                        imageResId = R.drawable.prod1
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
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Imagen del producto
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Imagen del Producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Título del producto
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF545454)
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
