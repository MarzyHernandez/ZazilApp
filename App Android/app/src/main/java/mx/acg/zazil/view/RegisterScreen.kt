package mx.acg.zazil.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.acg.zazil.R
import mx.acg.zazil.model.User
import mx.acg.zazil.viewmodel.RegisterViewModel

/**
 * Pantalla de registro que permite al usuario introducir sus datos personales, aceptar términos y condiciones,
 * y realizar el registro en el servidor remoto a través de Retrofit.
 *
 * La pantalla de registro incluye campos para el nombre, apellido, correo, teléfono y contraseña del usuario.
 * Además, permite la aceptación de los términos y condiciones antes de proceder con el registro.
 *
 * En caso de que el registro sea exitoso, se muestra un diálogo de confirmación y la opción para iniciar sesión.
 * En caso de errores, se notifica al usuario del problema.
 *
 * @param navController Controlador de navegación para cambiar entre pantallas.
 * @property gabaritoFontFamily Fuente personalizada utilizada en la interfaz.
 * @property nombre Estado mutable para almacenar el nombre del usuario.
 * @property apellido Estado mutable para almacenar el apellido del usuario.
 * @property email Estado mutable para almacenar el correo del usuario.
 * @property telefono Estado mutable para almacenar el teléfono del usuario.
 * @property password Estado mutable para almacenar la contraseña del usuario.
 * @property termsAccepted Estado mutable para verificar si los términos han sido aceptados.
 * @property errorMessage Estado mutable para mostrar mensajes de error al usuario.
 * @property showDialog Estado mutable para controlar la visibilidad del diálogo de éxito.
 * @property showTermsDialog Estado mutable para controlar la visibilidad del diálogo de términos y condiciones.
 * @property registerResult Resultado del registro observado desde el ViewModel.
 * @property isPasswordValid Función que verifica si la contraseña cumple con los requisitos mínimos.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
@Composable
fun RegisterScreen(navController: NavHostController) {
    // Fuente personalizada utilizada en toda la pantalla
    val gabaritoFontFamily = FontFamily(Font(R.font.gabarito_regular))

    // Variables para almacenar los valores ingresados por el usuario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var privacyPolicyAccepted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Variables para manejar la visualización de diálogos
    var showDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    // ViewModel para manejar la lógica de registro
    val viewModel: RegisterViewModel = viewModel()
    val registerResult by viewModel.registerResult.observeAsState()


    // Variable para manejar la visualización del loader
    var isLoading by remember { mutableStateOf(false) }
    /**
     * Valida la contraseña ingresada por el usuario.
     * La contraseña debe tener al menos 6 caracteres y contener al menos un número.
     *
     * @param password Contraseña a validar.
     * @return `true` si la contraseña es válida, `false` de lo contrario.
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 && password.any { it.isDigit() }
    }

    // Iniciar el proceso de registro
    LaunchedEffect(registerResult) {
        registerResult?.let {
            // Detener el loader cuando se reciba el resultado del registro
            isLoading = false
            if (it.contains("exitoso")) {
                showDialog = true
            } else {
                errorMessage = it
            }
        }
    }

    // Contenido de la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        // Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.mid_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(height = 200.dp, width = 100.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )

        // Columna que contiene el formulario de registro
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Título principal de la pantalla
            Text(
                text = "Regístrate",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = gabaritoFontFamily,
                color = Color.Black
            )
            // Subtítulo que da la bienvenida al usuario
            Text(
                text = "Te damos la bienvenida a Zazil",
                fontSize = 18.sp,
                fontFamily = gabaritoFontFamily,
                color = Color(0xFFE27F61),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Instrucción al usuario para completar los campos
                Text(
                    text = "Completa tus datos",
                    fontSize = 16.sp,
                    fontFamily = gabaritoFontFamily,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campos de entrada de datos
                BasicTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Nombre(s)", fontSize = 14.sp, color = Color.Gray)
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

                // Campo de texto para el apellido
                BasicTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Apellido", fontSize = 14.sp, color = Color.Gray)
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

                // Campo de texto para el correo
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Correo", fontSize = 14.sp, color = Color.Gray)
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

                // Campo de texto para el teléfono
                BasicTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Teléfono", fontSize = 14.sp, color = Color.Gray)
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

                // Campo de texto para la contraseña
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (!isPasswordValid(it)) {
                            errorMessage =
                                "La contraseña debe tener al menos 6 caracteres e incluir al menos un número."
                        } else {
                            errorMessage = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        Column {
                            Text("Contraseña", fontSize = 14.sp, color = Color.Gray)
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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { termsAccepted = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE27F61))
                        )
                        Text(
                            text = "Acepto los ",
                            fontSize = 14.sp,
                            fontFamily = gabaritoFontFamily,
                            color = Color.Black
                        )
                        TextButton(onClick = { showTermsDialog = true }) {
                            Text(
                                text = "términos y condiciones",
                                color = Color(0xFFE27F61),
                                fontSize = 14.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(start = 0.dp).offset(y = (-8).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = privacyPolicyAccepted,
                            onCheckedChange = { privacyPolicyAccepted = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE27F61))
                        )
                        Text(
                            text = "Acepto la ",
                            fontSize = 14.sp,
                            fontFamily = gabaritoFontFamily,
                            color = Color.Black
                        )
                        TextButton(onClick = { showPrivacyPolicyDialog = true }) {
                            Text(
                                text = "política de privacidad",
                                color = Color(0xFFE27F61),
                                fontSize = 14.sp
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))


                // Mensaje de resultado del registro
                registerResult?.let {
                    if (it.contains("exitoso")) {
                        showDialog = true
                    } else {
                        errorMessage = it
                    }
                }

                errorMessage?.let {
                    Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro
                Button(
                    onClick = {
                        if (!termsAccepted) {
                            errorMessage = "Debes aceptar los términos y condiciones y la política de privacidad"
                        } else if (!privacyPolicyAccepted) {
                            errorMessage = "Debes aceptar la política de privacidad"
                        } else if (!isPasswordValid(password)) {
                            errorMessage =
                                "La contraseña debe tener al menos 6 caracteres y un número"
                        } else {
                            errorMessage = null
                            isLoading = true
                            viewModel.registerUser(
                                User(
                                    email = email,
                                    password = password,
                                    nombres = nombre,
                                    apellidos = apellido,
                                    telefono = telefono
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBB7A7))
                ) {
                    Text(
                        text = "REGISTRAR",
                        fontFamily = gabaritoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Agregar espacio para separar del texto inferior

                // Texto para los usuarios que ya tienen cuenta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes cuenta?",
                        fontSize = 16.sp,
                        fontFamily = gabaritoFontFamily
                    )
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text(
                            text = "Inicia sesión",
                            color = Color(0xFFE27F61),
                            fontSize = 16.sp,
                            fontFamily = gabaritoFontFamily
                        )
                    }
                }
            }
        }

        // Mostrar loader si isLoading es verdadero
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = Color(0xFFE27F61))
            }
        }

        // Diálogo de registro exitoso
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Registro exitoso", color = Color(0xFFE17F61)) },
                text = {
                    Text(
                        text = "Tu cuenta ha sido creada exitosamente.",
                        color = Color(0xFF191919)
                    )
                },
                confirmButton = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("login")
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17F61))
                        ) {
                            Text(text = "Iniciar sesión", color = Color.White)
                        }
                    }
                },
                containerColor = Color.White
            )
        }


        // Diálogo de términos y condiciones
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                title = {
                    Text(
                        text = "Términos y Condiciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "Al utilizar nuestra aplicación de tienda en línea, usted acepta cumplir con los siguientes términos y condiciones. Nuestra plataforma proporciona un espacio para la compra de productos a través de su dispositivo Android. Los precios, la disponibilidad de productos, y las ofertas especiales están sujetos a cambios sin previo aviso. Nos reservamos el derecho de modificar o descontinuar la aplicación en cualquier momento sin responsabilidad alguna hacia usted. Es su responsabilidad asegurarse de que la información de su cuenta y los datos proporcionados para la compra sean precisos y estén actualizados. El uso indebido de nuestra aplicación, como el intento de fraude o cualquier otra actividad ilegal, resultará en la cancelación de su cuenta y podría ser reportado a las autoridades competentes.",
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Justify
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text("Cerrar", color = Color(0xFFE27F61))
                    }
                }
            )
        }
        // Diálogo de política de privacidad
        if (showPrivacyPolicyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyPolicyDialog = false },
                title = {
                    Text(
                        text = "Política de Privacidad",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "Política de Privacidad\n" +
                                    "Última actualización: 09 de octubre de 2024\n" +
                                    "Esta Política de Privacidad describe nuestras políticas y procedimientos sobre la recopilación, uso y divulgación de su información cuando utiliza el Servicio, y le informa sobre sus derechos de privacidad y cómo la ley le protege.\n" +
                                    "Usamos sus datos personales para proporcionar y mejorar el Servicio. Al utilizar el Servicio, usted acepta la recopilación y el uso de la información de acuerdo con esta Política de Privacidad. Esta Política de Privacidad ha sido creada con la ayuda del Generador de Políticas de Privacidad.\n" +
                                    "Interpretación y Definiciones\n" +
                                    "Interpretación\n" +
                                    "Las palabras cuya letra inicial está en mayúscula tienen significados definidos bajo las siguientes condiciones. Las siguientes definiciones tendrán el mismo significado, independientemente de si aparecen en singular o en plural.\n" +
                                    "Definiciones\n" +
                                    "A los efectos de esta Política de Privacidad:\n" +
                                    "Cuenta significa una cuenta única creada para que usted acceda a nuestro Servicio o partes de nuestro Servicio.\n" +
                                    "Afiliado significa una entidad que controla, es controlada por, o está bajo control común con una parte, donde \"control\" significa la propiedad del 50% o más de las acciones, participación accionaria u otros valores con derecho a voto para la elección de directores u otra autoridad de gestión.\n" +
                                    "Aplicación se refiere a Zazil, el programa de software proporcionado por la Compañía.\n" +
                                    "Compañía (referida como \"la Compañía\", \"nosotros\", \"nos\" o \"nuestro\" en este Acuerdo) se refiere a Fundación Todas Brillamos, Ignacio Manuel Altamirano 7A, Ex-hacienda de Santa Mónica, 54050 Tlalnepantla, Méx.\n" +
                                    "País se refiere a: México.\n" +
                                    "Dispositivo significa cualquier dispositivo que pueda acceder al Servicio, como una computadora, un teléfono móvil o una tableta digital.\n" +
                                    "Datos Personales son cualquier información que se relacione con un individuo identificado o identificable.\n" +
                                    "Servicio se refiere a la Aplicación.\n" +
                                    "Proveedor de Servicios significa cualquier persona física o jurídica que procese los datos en nombre de la Compañía. Se refiere a terceros o individuos empleados por la Compañía para facilitar el Servicio, proporcionar el Servicio en nombre de la Compañía, realizar servicios relacionados con el Servicio o ayudar a la Compañía a analizar cómo se utiliza el Servicio.\n" +
                                    "Datos de Uso se refiere a los datos recopilados automáticamente, ya sea generados por el uso del Servicio o de la propia infraestructura del Servicio (por ejemplo, la duración de una visita a una página).\n" +
                                    "Usted se refiere a la persona que accede o utiliza el Servicio, o la empresa u otra entidad legal en nombre de la cual dicha persona accede o utiliza el Servicio, según corresponda.\n" +
                                    "Recopilación y uso de sus datos personales\n" +
                                    "Tipos de datos recopilados\n" +
                                    "Datos Personales\n" +
                                    "Mientras usa nuestro Servicio, podemos pedirle que nos proporcione cierta información de identificación personal que puede ser utilizada para contactarlo o identificarlo. La información de identificación personal puede incluir, entre otros:\n" +
                                    "Dirección de correo electrónico\n" +
                                    "Nombre y apellido\n" +
                                    "Número de teléfono\n" +
                                    "Dirección, Estado, Provincia, Código Postal, Ciudad\n" +
                                    "Datos de uso\n" +
                                    "Datos de uso\n" +
                                    "Los datos de uso se recopilan automáticamente cuando se utiliza el Servicio.\n" +
                                    "Los datos de uso pueden incluir información como la dirección de Protocolo de Internet (por ejemplo, la dirección IP) de su Dispositivo, tipo de navegador, versión del navegador, las páginas de nuestro Servicio que visita, la hora y la fecha de su visita, el tiempo dedicado a esas páginas, identificadores únicos de dispositivos y otros datos de diagnóstico.\n" +
                                    "Cuando accede al Servicio a través de un dispositivo móvil, podemos recopilar cierta información automáticamente, que incluye, entre otros, el tipo de dispositivo móvil que utiliza, la identificación única de su dispositivo móvil, la dirección IP de su dispositivo móvil, su sistema operativo móvil, el tipo de navegador de Internet móvil que utiliza, identificadores únicos de dispositivos y otros datos de diagnóstico.\n" +
                                    "También podemos recopilar información que su navegador envía cada vez que visita nuestro Servicio o cuando accede al Servicio a través de un dispositivo móvil.\n" +
                                    "Uso de sus datos personales\n" +
                                    "La Compañía puede usar los Datos Personales para los siguientes propósitos:\n" +
                                    "Proporcionar y mantener nuestro Servicio, incluso para monitorear el uso de nuestro Servicio.\n" +
                                    "Gestionar su Cuenta: para gestionar su registro como usuario del Servicio. Los Datos Personales que proporcione pueden darle acceso a diferentes funcionalidades del Servicio que están disponibles para usted como usuario registrado.\n" +
                                    "Para la ejecución de un contrato: el desarrollo, cumplimiento y gestión del contrato de compra de los productos, artículos o servicios que ha comprado o de cualquier otro contrato con nosotros a través del Servicio.\n" +
                                    "Contactarlo: Para contactarlo por correo electrónico, llamadas telefónicas, SMS u otras formas equivalentes de comunicación electrónica, como notificaciones push de una aplicación móvil, en relación con actualizaciones o comunicaciones informativas relacionadas con las funcionalidades, productos o servicios contratados, incluidas las actualizaciones de seguridad, cuando sea necesario o razonable para su implementación.\n" +
                                    "Proporcionarle noticias, ofertas especiales e información general sobre otros productos, servicios y eventos que ofrecemos, similares a los que ya ha comprado o preguntado, a menos que haya optado por no recibir dicha información.\n" +
                                    "Gestionar sus solicitudes: Para atender y gestionar sus solicitudes hacia nosotros.\n" +
                                    "Para transferencias comerciales: Podemos usar su información para evaluar o llevar a cabo una fusión, desinversión, reestructuración, reorganización, disolución o cualquier otra venta o transferencia de algunos o todos nuestros activos, ya sea como una empresa en marcha o como parte de una quiebra, liquidación o procedimiento similar, en la cual los Datos Personales que poseemos sobre los usuarios de nuestro Servicio están entre los activos transferidos.\n" +
                                    "Para otros fines: Podemos utilizar su información para otros fines, como el análisis de datos, la identificación de tendencias de uso, la determinación de la efectividad de nuestras campañas promocionales y para evaluar y mejorar nuestro Servicio, productos, servicios, marketing y su experiencia.\n" +
                                    "Podemos compartir su información personal en las siguientes situaciones:\n" +
                                    "Con los Proveedores de Servicios: Podemos compartir su información personal con los Proveedores de Servicios para monitorear y analizar el uso de nuestro Servicio, para contactarlo.\n" +
                                    "Para transferencias comerciales: Podemos compartir o transferir su información personal en relación con, o durante las negociaciones de, cualquier fusión, venta de activos de la Compañía, financiamiento o adquisición de todo o parte de nuestro negocio a otra empresa.\n" +
                                    "Con afiliados: Podemos compartir su información con nuestros afiliados, en cuyo caso les exigiremos a esos afiliados que respeten esta Política de Privacidad. Los afiliados incluyen nuestra empresa matriz y cualquier otra subsidiaria, socios de empresas conjuntas u otras empresas que controlamos o que están bajo control común con nosotros.\n" +
                                    "Con socios comerciales: Podemos compartir su información con nuestros socios comerciales para ofrecerle ciertos productos, servicios o promociones.\n" +
                                    "Con otros usuarios: cuando comparte información personal o interactúa en áreas públicas con otros usuarios, dicha información puede ser vista por todos los usuarios y puede ser distribuida públicamente fuera.\n" +
                                    "Con su consentimiento: Podemos divulgar su información personal para cualquier otro propósito con su consentimiento.\n" +
                                    "Retención de sus datos personales\n" +
                                    "La Compañía conservará sus Datos Personales solo durante el tiempo que sea necesario para los fines establecidos en esta Política de Privacidad. Conservaremos y utilizaremos sus Datos Personales en la medida necesaria para cumplir con nuestras obligaciones legales (por ejemplo, si estamos obligados a conservar sus datos para cumplir con las leyes aplicables), resolver disputas y hacer cumplir nuestros acuerdos y políticas legales.\n" +
                                    "La Compañía también conservará los Datos de Uso para fines de análisis interno. Los Datos de Uso generalmente se conservan durante un período de tiempo más corto, excepto cuando estos datos se utilizan para fortalecer la seguridad o para mejorar la funcionalidad de nuestro Servicio, o cuando estamos legalmente obligados a retener estos datos durante períodos más largos.\n" +
                                    "Traslado de sus datos personales\n" +
                                    "Su información, incluidos los Datos Personales, se procesa en las oficinas operativas de la Compañía y en cualquier otro lugar donde se encuentren las partes involucradas en el procesamiento. Esto significa que esta información puede transferirse a —y mantenerse en— computadoras ubicadas fuera de su estado, provincia, país u otra jurisdicción gubernamental, donde las leyes de protección de datos pueden diferir de las de su jurisdicción.\n" +
                                    "Su consentimiento a esta Política de Privacidad, seguido de su envío de dicha información, representa su acuerdo con esa transferencia.\n" +
                                    "La Compañía tomará todas las medidas razonablemente necesarias para garantizar que sus datos se traten de manera segura y de acuerdo con esta Política de Privacidad, y no se realizará ninguna transferencia de sus Datos Personales a una organización o país, a menos que existan controles adecuados que incluyan la seguridad de sus datos y otra información personal.\n" +
                                    "Eliminación de sus datos personales\n" +
                                    "Tiene derecho a eliminar o solicitar que lo ayudemos a eliminar los Datos Personales que hemos recopilado sobre usted.\n" +
                                    "Nuestro Servicio puede darle la capacidad de eliminar cierta información sobre usted desde dentro del Servicio.\n" +
                                    "Puede actualizar, enmendar o eliminar su información en cualquier momento iniciando sesión en su Cuenta, si tiene una, y visitando la sección de configuración de la cuenta que le permite gestionar su información personal. También puede contactarnos para solicitar acceso, corregir o eliminar cualquier información personal que nos haya proporcionado.\n" +
                                    "Sin embargo, tenga en cuenta que podemos necesitar retener cierta información cuando tengamos una obligación legal o una base legal para hacerlo.\n" +
                                    "Divulgación de sus datos personales\n" +
                                    "Transacciones comerciales\n" +
                                    "Si la Compañía está involucrada en una fusión, adquisición o venta de activos, sus Datos Personales pueden ser transferidos. Le proporcionaremos un aviso antes de que sus Datos Personales sean transferidos y estén sujetos a una Política de Privacidad diferente.\n" +
                                    "Cumplimiento de la ley\n" +
                                    "En determinadas circunstancias, la Compañía puede verse obligada a divulgar sus Datos Personales si lo requiere la ley o en respuesta a solicitudes válidas de autoridades públicas (por ejemplo, un tribunal o una agencia gubernamental).\n" +
                                    "Otras obligaciones legales\n" +
                                    "La Compañía puede divulgar sus Datos Personales de buena fe si cree que dicha acción es necesaria para:\n" +
                                    "Cumplir con una obligación legal\n" +
                                    "Proteger y defender los derechos o la propiedad de la Compañía\n" +
                                    "Prevenir o investigar posibles irregularidades en relación con el Servicio\n" +
                                    "Proteger la seguridad personal de los usuarios del Servicio o del público\n" +
                                    "Protegerse contra la responsabilidad legal\n" +
                                    "Seguridad de sus datos personales\n" +
                                    "La seguridad de sus Datos Personales es importante para nosotros, pero recuerde que ningún método de transmisión a través de Internet o de almacenamiento electrónico es 100% seguro. Si bien nos esforzamos por utilizar medios comercialmente aceptables para proteger sus Datos Personales, no podemos garantizar su seguridad absoluta.\n" +
                                    "Enlaces a otros sitios web\n" +
                                    "Nuestro Servicio puede contener enlaces a otros sitios web que no son operados por nosotros. Si hace clic en un enlace de un tercero, será dirigido al sitio de ese tercero. Le recomendamos encarecidamente que revise la Política de Privacidad de cada sitio que visite.\n" +
                                    "No tenemos control ni asumimos responsabilidad alguna por el contenido, las políticas de privacidad o las prácticas de sitios o servicios de terceros.\n" +
                                    "Cambios a esta Política de Privacidad\n" +
                                    "Podemos actualizar nuestra Política de Privacidad de vez en cuando. Le notificaremos cualquier cambio publicando la nueva Política de Privacidad en esta página.\n" +
                                    "Le informaremos por correo electrónico y/o un aviso destacado en nuestro Servicio, antes de que el cambio sea efectivo, y actualizaremos la fecha de \"Última actualización\" en la parte superior de esta Política de Privacidad.\n" +
                                    "Se le aconseja que revise esta Política de Privacidad periódicamente para ver si hay cambios. Los cambios a esta Política de Privacidad son efectivos cuando se publican en esta página.\n" +
                                    "Contáctenos\n" +
                                    "Si tiene alguna pregunta sobre esta Política de Privacidad, puede contactarnos:\n" +
                                    "Por correo electrónico: contacto@fundaciontodasbrillamos.org\n",
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Justify
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPrivacyPolicyDialog = false }) {
                        Text("Cerrar", color = Color(0xFFE27F61))
                    }
                }
            )
        }
    }
}



