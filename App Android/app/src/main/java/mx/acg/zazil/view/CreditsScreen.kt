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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.R

@Composable
fun CreditsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEEEE))
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        // Encabezado del carrito
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
                    text = "Créditos",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = gabaritoFontFamily,
                    color = Color(0xFF191919)

                )

            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        // Botón de regresar
        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "< Regresar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Tarjeta de créditos
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CreditCard(
                    imageRes = R.drawable.carlos,
                    name = "CARLOS HERRERA"
                )
                CreditCard(
                    imageRes = R.drawable.mariana,
                    name = "MARIANA HERNÁNDEZ"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CreditCard(
                    imageRes = R.drawable.alma,
                    name = "ALMA CARPIO"
                )
                CreditCard(
                    imageRes = R.drawable.alberto,
                    name = "ALBERTO CEBREROS"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                CreditCard(
                    imageRes = R.drawable.melissa,
                    name = "MELISSA MIRELES"
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Elaborado por alumnos del programa Ingeniería en Tecnologías Computacionales del Tecnológico de Monterrey",
                fontSize = 16.sp,
                fontFamily = gabaritoFontFamily,
                textAlign = TextAlign.Center,
                color = Color(0xFF191919)

            )
        }

    }
}


@Composable
fun CreditCard(imageRes: Int, name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF191919)
        )
    }
}