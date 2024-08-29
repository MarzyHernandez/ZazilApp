package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.acg.zazil.R

@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ZAZIL",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF293392)
                )
                Image(
                    painter = painterResource(id = R.drawable.logo), // Reemplaza con tu recurso de logotipo
                    contentDescription = "Logotipo",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                Column {
                    Text(
                        text = "$150.00",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "$200.00",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = { /* Acción de agregar al carrito */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Agregar al carrito", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de descripción y detalles
            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam sem, pretium in pretium vel, ullamcorper quis libero.",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Materiales: Algodón 100%\nAncho: 25 cm\nLargo: 15 cm\nISNF: FRRGB09",
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        // Barra de navegación fija al fondo
        NavBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}
