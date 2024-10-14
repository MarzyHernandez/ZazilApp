package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mx.acg.zazil.model.ShippingAddress
import mx.acg.zazil.viewmodel.ShippingViewModel

/**
 * Pantalla para finalizar la compra, donde el usuario puede revisar y editar
 * la información de envío antes de proceder al pago.
 * Los campos incluyen dirección, ciudad, estado, país, etc., con valores pre-llenados.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @param total Total del carrito de compra.
 * @param calle Dirección del cliente.
 * @param numeroInterior Número interior del domicilio.
 * @param colonia Colonia del domicilio.
 * @param codigoPostal Código postal del domicilio.
 * @param ciudad Ciudad del domicilio.
 * @param estado Estado del domicilio.
 * @param pais País del domicilio.
 * @param modifier Modificador para personalizar el componente.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun EndShoppingScreen(
    navController: NavHostController,
    total: String,
    modifier: Modifier = Modifier,
    shippingViewModel: ShippingViewModel = viewModel()
) {

    // Obtener el UID del usuario
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Estado de la dirección de envío
    var updatedCalle by remember { mutableStateOf("") }
    var updatedNumeroInterior by remember { mutableStateOf("") }
    var updatedColonia by remember { mutableStateOf("") }
    var updatedCodigoPostal by remember { mutableStateOf("") }
    var updatedCiudad by remember { mutableStateOf("") }
    var updatedEstado by remember { mutableStateOf("") }
    var updatedPais by remember { mutableStateOf("México") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar la dirección de envío cuando se abre la pantalla
    LaunchedEffect(uid) {
        shippingViewModel.getShippingAddress(uid, {
            shippingViewModel.shippingAddress?.let { address ->
                updatedCalle = address.calle
                updatedNumeroInterior = address.numero_interior
                updatedColonia = address.colonia
                updatedCodigoPostal = address.codigo_postal
                updatedCiudad = address.ciudad
                updatedEstado = address.estado
                updatedPais = address.pais
            }
            isLoading = false
        }, {
            Log.e("EndShoppingScreen", it)
            isLoading = false
        })
    }

    if (isLoading) {
        // Mostrar un loader mientras se carga la dirección
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFE17F61))
        }
    } else {
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

            // Botón "Regresar" para volver al carrito
            TextButton(onClick = { navController.navigate("cart") }) {
                Text(
                    text = "< Regresar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            // Datos del cliente
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Datos del cliente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919),
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto personalizado para cada dato
                BasicTextField(
                    value = updatedCalle,
                    onValueChange = { updatedCalle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Calle", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedNumeroInterior,
                    onValueChange = { updatedNumeroInterior = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Número Interior (Opcional)", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedColonia,
                    onValueChange = { updatedColonia = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Colonia", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedCodigoPostal,
                    onValueChange = { updatedCodigoPostal = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Código Postal", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedCiudad,
                    onValueChange = { updatedCiudad = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Ciudad", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedEstado,
                    onValueChange = { updatedEstado = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Estado", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = updatedPais,
                    onValueChange = { updatedPais = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("País", fontSize = 14.sp, color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp)
                            ) {
                                innerTextField()
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje de error si hay campos vacíos
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (updatedCalle.isBlank() || updatedColonia.isBlank() ||
                            updatedCodigoPostal.isBlank() || updatedCiudad.isBlank() ||
                            updatedEstado.isBlank() || updatedPais.isBlank()
                        ) {
                            errorMessage = "Por favor, llena todos los campos obligatorios."
                        } else {
                            val address = ShippingAddress(
                                calle = updatedCalle,
                                numero_interior = updatedNumeroInterior,
                                colonia = updatedColonia,
                                codigo_postal = updatedCodigoPostal,
                                ciudad = updatedCiudad,
                                estado = updatedEstado,
                                pais = updatedPais
                            )
                            shippingViewModel.saveShippingAddress(uid, address, {
                                navController.navigate(
                                    "payment/$total/$updatedCalle/$updatedNumeroInterior/$updatedColonia/$updatedCodigoPostal/$updatedCiudad/$updatedEstado/$updatedPais"
                                )
                            }, {
                                Log.e("EndShoppingScreen", it)
                            })
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "CONTINUAR",
                        fontSize = 18.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
