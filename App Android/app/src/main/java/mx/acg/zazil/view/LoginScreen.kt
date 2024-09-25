package mx.acg.zazil.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.R
import mx.acg.zazil.model.UserResponse
import mx.acg.zazil.model.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Composable
fun LoginScreen(navController: NavHostController, signInWithGoogle: () -> Unit) {
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<String?>(null) }  // Variable para mostrar el userId

    // Instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()

    // Función para crear el cliente de Retrofit con interceptor de logging
    fun createRetrofitClient(): Retrofit {
        // Crear un interceptor que loguee
        val logging = HttpLoggingInterceptor { message -> Log.d("HTTP", message) }
        logging.level = HttpLoggingInterceptor.Level.BODY  // Cambiar a Level.BASIC para menos detalles

        // Crear un cliente OkHttpClient y añadir el interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Crear la instancia de Retrofit usando el cliente OkHttpClient con logging
        return Retrofit.Builder()
            .baseUrl("https://getuidbyemail-dztx2pd2na-uc.a.run.app/")  // URL base
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

// Crear la instancia del servicio usando el cliente de Retrofit configurado
    val retrofit = createRetrofitClient()

    val userService = retrofit.create(UserService::class.java)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Hola!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )

            Text(
                text = "Te damos la bienvenida a Zazil",
                fontSize = 16.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para iniciar sesión
            Button(
                onClick = {
                    // Primero se autentica con Firebase
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Autenticación exitosa, proceder con la solicitud GET
                                Log.d("LoginScreen", "Autenticación con Firebase exitosa")

                                // Realizar la solicitud GET con Retrofit para obtener el userId
                                val call = userService.getUserIdByEmail(email)
                                call.enqueue(object : Callback<UserResponse> {
                                    override fun onResponse(
                                        call: Call<UserResponse>,
                                        response: Response<UserResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val user = response.body()

                                            // Imprimir la respuesta completa en el log
                                            Log.d("LoginScreen", "Respuesta del servidor: ${response.body()}")

                                            userId = user?.uid
                                            Log.d("LoginScreen", "UserId: $userId")

                                            // Navegar a la pantalla de catálogo
                                            navController.navigate("catalog") {
                                                popUpTo("login") { inclusive = true } // Limpia el historial de navegación
                                            }

                                        } else {
                                            errorMessage = "Error al obtener el user Id"
                                            Log.d("LoginScreen", "Error en la respuesta: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                        errorMessage = "Error de red: ${t.message}"
                                    }
                                })
                            } else {
                                // Error en la autenticación
                                errorMessage = "Error en la autenticación: ${task.exception?.message}"
                                Log.e("LoginScreen", "Error en la autenticación: ${task.exception}")
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
            ) {
                Text(text = "Iniciar sesión")
            }

            userId?.let {
                Text(
                    text = "User ID: $it",
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro con Google
            Button(
                onClick = { signInWithGoogle() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC7C5))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Google", color = Color.Black)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}
