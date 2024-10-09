package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.R
import mx.acg.zazil.view.NavBar
import mx.acg.zazil.view.ProfileForm
import mx.acg.zazil.viewmodel.ProfileViewModel

/**
 * Composable que representa la pantalla de perfil del usuario.
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 * Incluye la imagen de fondo, la foto de perfil, los datos del usuario y la barra de navegación.
 * @param [modifier] Modificador para personalizar la disposición y el estilo del Composable.
 */
@Composable
fun ProfileScreen(
    navController: NavHostController,
    uid: String,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileData = profileViewModel.profileData.collectAsState().value

    LaunchedEffect(uid) {
        profileViewModel.fetchUserProfile(uid)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
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

            Text(
                text = "Mi perfil",
                fontSize = 36.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(80.dp))

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

                ProfileForm(
                    profile = profile,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } ?: run {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Obtén el usuario actual desde FirebaseAuth
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val uid = currentUser?.uid ?: ""
                    // Navega a MyShoppingScreen con el UID del usuario
                    navController.navigate("myShopping/$uid")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEEEE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "HISTORIAL DE COMPRA", fontSize = 16.sp, fontFamily = gabaritoFontFamily, color = Color(0xFFE17F61), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("FAQs") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE1D6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "PREGUNTAS FRECUENTES", color = Color(0xFF293392), fontSize = 16.sp,fontFamily = gabaritoFontFamily, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

