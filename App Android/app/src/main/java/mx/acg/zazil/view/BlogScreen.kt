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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.PostViewModel

@Composable
fun BlogScreen(
    postViewModel: PostViewModel = viewModel()  // Usamos el ViewModel
) {
    // Obtenemos el estado actual de los posts desde el ViewModel
    val posts by postViewModel.posts.observeAsState(initial = emptyList())

    // Cargamos los posts al inicio
    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    // Pantalla desplazable con encabezado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())  // Hacer la pantalla desplazable
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
        ) {
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
                        text = "Publicaciones",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = gabaritoFontFamily,
                        color = Color(0xFF191919)

                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            // Mostrar los posts obtenidos de la API
            posts.forEach { post ->
                BlogPost(
                    title = post.titulo,
                    author = post.autor,
                    timeAgo = post.fecha.substring(0, 10),  // Solo mostramos la fecha, puedes formatear mejor si lo prefieres
                    description = post.contenido,
                    imageUrls = listOf(post.imagen)  // Solo una imagen, en este caso
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre publicaciones
            }
        }
    }
}

@Composable
fun BlogPost(
    title: String,
    author: String,
    timeAgo: String,
    description: String,
    imageUrls: List<String>,  // Lista de URLs de imágenes
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        // Tarjeta de la publicación
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFEDEDED), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Row que contiene el título y luego la imagen, autor y fecha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Título del post primero
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color(0xFFE17F61),  // Aplica el color aquí
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)  // Ocupa todo el espacio restante a la izquierda
                        .wrapContentHeight()  // Permite que el título crezca en altura si es necesario
                        .widthIn(max = 230.dp)  // Ajusta el ancho máximo para que el texto haga salto de línea
                )

                Spacer(modifier = Modifier.width(12.dp))  // Espacio entre el título y la información del autor

                // Imagen, autor y fecha alineados a la derecha
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen del autor
                    Image(
                        painter = painterResource(id = R.drawable.experto),  // Icono del perfil del autor
                        contentDescription = "Imagen del autor",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)  // Hacemos la imagen redonda
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = author, fontSize = 14.sp, color = Color(0xFF545454))
                        Text(text = timeAgo, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción de la publicación
            Text(
                text = description,
                fontFamily = gabaritoFontFamily,
                fontSize = 14.sp,
                color = Color(0xFF545454)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Imágenes del contenido de la publicación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mostramos las imágenes desde las URLs
                imageUrls.forEach { imageUrl ->
                    Image(
                        painter = rememberImagePainter(imageUrl),  // Usamos Coil para cargar las imágenes desde URLs
                        contentDescription = "Imagen de la publicación",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))  // Bordes redondeados
                            .aspectRatio(1.5f)  // Relación de aspecto uniforme
                    )
                }
            }
        }
    }
}
