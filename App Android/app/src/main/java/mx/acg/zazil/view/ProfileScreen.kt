package mx.acg.zazil.view

import UserProfile
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import mx.acg.zazil.viewmodel.ProfileViewModel

/**
 * Composable que representa la pantalla de perfil del usuario.
 * Incluye la imagen de fondo, la foto de perfil, los datos del usuario y la barra de navegación.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param uid ID único del usuario.
 * @param modifier Modificador para personalizar la disposición y el estilo del Composable.
 * @param profileViewModel Instancia del ViewModel para gestionar la lógica del perfil.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun ProfileScreen(
    navController: NavHostController,
    uid: String,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileData = profileViewModel.profileData.collectAsState().value

    // Carga el perfil del usuario cuando se proporciona un UID
    LaunchedEffect(uid) {
        profileViewModel.fetchUserProfile(uid)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Imagen de fondo para la pantalla de perfil
        Image(
            painter = painterResource(id = R.drawable.background_profile),
            contentDescription = "Fondo superior",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Título de la pantalla
            Text(
                text = "Mi perfil",
                fontSize = 36.sp,
                fontFamily = gabaritoFontFamily,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(80.dp))

            // Muestra los datos del perfil del usuario
            profileData?.let { profile ->
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .border(width = 5.dp, color = Color(0xFFEBB7A7), shape = CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(profile.foto_perfil),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Formulario para mostrar y editar los datos del perfil
                ProfileForm(
                    profile = profile,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } ?: run {
                // Muestra un indicador de carga mientras se obtienen los datos del perfil
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFFE17F61))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegar al historial de compras
            Button(
                onClick = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val uid = currentUser?.uid ?: ""
                    navController.navigate("myShopping/$uid")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEEEE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "HISTORIAL DE COMPRA", fontSize = 16.sp, color = Color(0xFFE17F61), fontWeight = FontWeight.Bold, fontFamily = gabaritoFontFamily)
            }
        }
    }
}
