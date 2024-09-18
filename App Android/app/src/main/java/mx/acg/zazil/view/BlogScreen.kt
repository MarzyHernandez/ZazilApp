package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BlogScreen(modifier: Modifier = Modifier) {
    // Pantalla desplazable con encabezado
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

            Spacer(modifier = Modifier.height(16.dp))

            // Encabezado de la pantalla
            Text(
                text = "Publicaciones",
                fontSize = 28.sp,
                color = Color(0xFFE17F61),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Ejemplo de dos publicaciones
            BlogPost(
                title = "Título",
                author = "Dr. Antonio Solís Ortega",
                timeAgo = "hace una hora",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce luctus, elit eget consectetur pretium.",
                imageResId = R.drawable.prod1
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre publicaciones

            BlogPost(
                title = "Título",
                author = "Dr. Amanda Castillo Ruiz",
                timeAgo = "hace un día",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce luctus, elit eget consectetur pretium.",
                imageResId = R.drawable.prod1
            )
        }
    }
}

@Composable
fun BlogPost(
    title: String,
    author: String,
    timeAgo: String,
    description: String,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    // Tarjeta de la publicación
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Título y nombre del autor
        Text(text = title, fontSize = 16.sp, color = Color(0xFF545454), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del autor
            Image(
                painter = painterResource(id = R.drawable.scarlett),  // Icono del perfil del autor
                contentDescription = "Imagen del autor",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = author, fontSize = 14.sp, color = Color(0xFF545454))
                Text(text = timeAgo, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Descripción de la publicación
        Text(text = description, fontSize = 14.sp, color = Color(0xFF545454))

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen del contenido de la publicación
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Imagen de la publicación",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)) // Bordes redondeados para la imagen
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BlogScreenPreview() {
    BlogScreen()
}
