package mx.acg.zazil.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.model.MakeOrder
import mx.acg.zazil.viewmodel.MakeOrderViewModel

import android.util.Log
import androidx.compose.ui.draw.clip
import com.google.firebase.auth.FirebaseAuth
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.acg.zazil.CheckoutViewModel

/**
 * Pantalla para el proceso de pago. Permite al usuario finalizar su compra,
 * ver los detalles del pedido y editar la dirección de envío si es necesario.
 * Una vez completada la compra, se muestra un diálogo confirmando la realización exitosa del pedido.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param total Total del pedido.
 * @param calle Dirección del cliente.
 * @param numeroInterior Número interior del domicilio.
 * @param colonia Colonia del domicilio.
 * @param codigoPostal Código postal del domicilio.
 * @param ciudad Ciudad del domicilio.
 * @param estado Estado del domicilio.
 * @param pais País del domicilio.
 * @param modifier Modificador para personalizar el componente.
 * @param makeOrderViewModel ViewModel para manejar la lógica del pedido.
 * @param checkoutViewModel ViewModel para manejar la lógica del pago con Stripe.
 *
 * @author Alberto Cebreros González
 * @autor Melissa Mireles Rendón
 */
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
    makeOrderViewModel: MakeOrderViewModel = viewModel(),
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val context = LocalContext.current

    // Estado para controlar la visibilidad del loader
    var isLoading by remember { mutableStateOf(false) }
    // Estado para controlar la visibilidad del popup
    var showDialog by remember { mutableStateOf(false) }

    // Crear instancia de PaymentSheet
    val paymentSheet = rememberPaymentSheet { paymentSheetResult ->
        isLoading = false // Ocultar el loader cuando se completa el proceso de pago
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Log.d("PaymentScreen", "Pago completado con éxito")
                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid

                if (uid != null) {
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
                    makeOrderViewModel.makeOrder(makeOrder)
                    showDialog = true // Mostrar el popup después de realizar la compra
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
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                isLoading = true // Mostrar el loader cuando se haga clic en el botón
                checkoutViewModel.onAmountChange(total)
                checkoutViewModel.onStripePayment(context, paymentSheet)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
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

        // Mostrar el loader debajo del botón si isLoading es verdadero
        if (isLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(
                color = Color(0xFFE17F61),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    // Diálogo emergente cuando se completa la compra
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Compra realizada con éxito", fontWeight = FontWeight.Bold) },
            text = { Text(text = "Tu compra ha sido completada correctamente.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val uid = currentUser?.uid
                        if (uid != null) {
                            navController.navigate("myShopping/$uid")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                ) {
                    Text(text = "Ir a mis compras", color = Color.White)
                }
            },
            containerColor = Color.White
        )
    }
}
