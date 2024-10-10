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
import coil.compose.rememberAsyncImagePainter
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.PostViewModel

/**
 * Pantalla principal del blog que muestra una lista de publicaciones.
 * Cada publicación contiene un título, autor, fecha, descripción y una imagen.
 * La pantalla se carga desplazable y muestra un indicador de carga
 * mientras se obtienen las publicaciones desde el ViewModel.
 *
 * @param postViewModel ViewModel que maneja la lógica de carga de publicaciones.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun BlogScreen(
    postViewModel: PostViewModel = viewModel()  // Usamos el ViewModel para cargar los posts
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

            if (posts.isEmpty()) {
                // Mostrar el loader debajo del título "Publicaciones" mientras las publicaciones están cargando
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFE17F61), // Color personalizado para el loader
                        modifier = Modifier.size(50.dp)
                    )
                }
            } else {
                // Mostrar los posts obtenidos de la API
                posts.forEach { post ->
                    BlogPost(
                        title = post.titulo,
                        author = post.autor,
                        timeAgo = post.fecha.substring(0, 10),
                        description = post.contenido,
                        imageUrls = listOf(post.imagen)
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio entre publicaciones
                }
            }
        }
    }
}

/**
 * Composable para mostrar una publicación del blog.
 * Incluye título, autor, fecha, descripción y una lista de imágenes.
 *
 * @param title Título de la publicación.
 * @param author Autor de la publicación.
 * @param timeAgo Fecha de la publicación en formato corto.
 * @param description Descripción o contenido breve de la publicación.
 * @param imageUrls Lista de URLs de las imágenes a mostrar en la publicación.
 * @param modifier Modificador opcional para personalizar el componente.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
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
                    color = Color(0xFFE17F61),
                    fontFamily = gabaritoFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .widthIn(max = 230.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Imagen, autor y fecha alineados a la derecha
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.experto),
                        contentDescription = "Imagen del autor",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
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
                imageUrls.forEach { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Imagen de la publicación",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .aspectRatio(1.5f)
                    )
                }
            }
        }
    }
}
