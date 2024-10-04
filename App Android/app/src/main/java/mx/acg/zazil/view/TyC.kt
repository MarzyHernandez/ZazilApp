package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.FAQItem
import mx.acg.zazil.model.FAQService
import mx.acg.zazil.viewmodel.FAQViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




@Composable
fun TyC(navController: NavHostController, modifier: Modifier = Modifier, faqViewModel: FAQViewModel = viewModel()) {
    val faqItems by faqViewModel.faqItems

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Sección superior personalizada con bordes redondeados en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFEE1D6),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.CenterStart // Alinea el texto a la izquierda
        ) {
            Text(
                text = "Términos y condiciones",
                fontSize = 28.sp,
                color = Color(0xFF191919),
                modifier = Modifier.padding(start = 16.dp) // Espacio a la izquierda
            )
        }


        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("configuracion") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold)
        }



        // Mostra
    }
}
