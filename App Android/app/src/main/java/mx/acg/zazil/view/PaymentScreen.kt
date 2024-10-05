package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.model.MakeOrder
import mx.acg.zazil.viewmodel.MakeOrderViewModel

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import mx.acg.zazil.CheckoutViewModel // Importa tu ViewModel

@Composable
fun PaymentScreen(
    navController: NavHostController,
    total: String,
    calle: String,
    numeroInterior: String,
    colonia: String,
    codigoPostal: String,
    ciudad: String,
    estado: String,
    pais: String,
    modifier: Modifier = Modifier,
    makeOrderViewModel: MakeOrderViewModel = viewModel(), // Inyectamos el ViewModel
    checkoutViewModel: CheckoutViewModel = viewModel() // Inyectamos el CheckoutViewModel
) {
    val context = LocalContext.current

    // Crear instancia de PaymentSheet
    val paymentSheet = rememberPaymentSheet { paymentSheetResult ->
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Log.d("PaymentScreen", "Pago completado con éxito")

                // Aquí colocamos el resto del código del onClick original
                // Obtener el uid directamente desde Firebase
                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid

                if (uid != null) {
                    // Crear objeto MakeOrder con los datos
                    val makeOrder = MakeOrder(
                        uid = uid,
                        codigo_postal = codigoPostal.toInt(),
                        estado = estado,
                        ciudad = ciudad,
                        calle = calle,
                        numero_interior = numeroInterior,
                        pais = pais,
                        colonia = colonia
                    )

                    Log.d("PaymentScreen", "MakeOrder creado: $makeOrder")

                    // Llamar a la API para hacer el pedido
                    makeOrderViewModel.makeOrder(makeOrder)

                    Log.d("PaymentScreen", "Solicitud de orden enviada")

                    // Redirigir al historial de compras
                    navController.navigate("myShopping/$uid")
                } else {
                    Log.e("PaymentScreen", "Error: Usuario no autenticado, uid es null.")
                }
            }
            is PaymentSheetResult.Failed -> {
                Log.e("PaymentScreen", "Pago fallido: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Canceled -> {
                Log.d("PaymentScreen", "Pago cancelado por el usuario")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado con título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                .background(Color(0xFFFEE1D6))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Finalizar compra",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919),
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los datos de envío
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DIRECCIÓN DE ENVÍO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar los datos del cliente
                Text(
                    text = "$calle, $numeroInterior, $colonia, $codigoPostal, $ciudad, $estado, $pais",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para editar los datos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            Log.d("PaymentScreen", "Navegando a la pantalla de edición")
                            navController.navigate(
                                "endShopping/$total/$calle/$numeroInterior/$colonia/$codigoPostal/$ciudad/$estado/$pais"
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 2.dp)
                    ) {
                        Text(
                            text = "EDITAR",
                            fontSize = 12.sp,
                            color = Color(0xFFE17F61),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el total del pedido
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "TOTAL DEL PEDIDO",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )

            Text(
                text = "$$total",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de finalizar compra
        Button(
            onClick = {
                Log.d("PaymentScreen", "Iniciando proceso de pago...")

                // Establecer el monto en el ViewModel
                checkoutViewModel.onAmountChange(total)

                // Iniciar el proceso de pago con Stripe
                checkoutViewModel.onStripePayment(context, paymentSheet)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "FINALIZAR COMPRA",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PaymentMethodOption(methodName: String, description: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono del método de pago
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = methodName,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre y descripción del método de pago
        Column {
            Text(
                text = methodName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191919)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}
