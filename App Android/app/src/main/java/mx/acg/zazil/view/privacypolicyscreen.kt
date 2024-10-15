package mx.acg.zazil.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.acg.zazil.ui.theme.Typography

/**
 * Composable que representa la pantalla de la Política de Privacidad de la aplicación.
 *
 * Esta pantalla muestra el contenido de la política de privacidad, incluyendo secciones sobre la
 * recopilación y uso de datos personales, derechos de los usuarios y contacto.
 *
 * @param navController Controlador de navegación para manejar la navegación entre pantallas.
 * @param modifier Modificador opcional para personalizar el diseño de la pantalla.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun PrivacyPolicyScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    // Contenedor principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(0.dp)
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp))
                .background(Color(0xFFFEE1D6))
                .padding(vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Política de Privacidad",
                    fontSize = 28.sp,
                    color = Color(0xFF191919),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa el ancho completo disponible
                        .wrapContentWidth(Alignment.CenterHorizontally) // Centra el texto horizontalmente
                )
            }
        }

        // Botón "Regresar"
        TextButton(
            onClick = { navController.navigate("configuracion") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "< Regresar", color = Color.Gray, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido de la Política de Privacidad
        PrivacyPolicyCard(
            title = "Política de Privacidad",
            content = """
                Última actualización: 09 de octubre de 2024
                
                Esta Política de Privacidad describe nuestras políticas y procedimientos sobre la recopilación, uso y divulgación de su información cuando utiliza el Servicio, y le informa sobre sus derechos de privacidad y cómo la ley le protege.
                Usamos sus datos personales para proporcionar y mejorar el Servicio. Al utilizar el Servicio, usted acepta la recopilación y el uso de la información de acuerdo con esta Política de Privacidad. Esta Política de Privacidad ha sido creada con la ayuda del Generador de Políticas de Privacidad.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Secciones adicionales de la política de privacidad
        PrivacyPolicyCard(
            title = "Interpretación y Definiciones",
            content = """
                Interpretación
                
                Las palabras cuya letra inicial está en mayúscula tienen significados definidos bajo las siguientes condiciones. Las siguientes definiciones tendrán el mismo significado, independientemente de si aparecen en singular o en plural.
                
                Definiciones
                A los efectos de esta Política de Privacidad:
                
                -Cuenta significa una cuenta única creada para que usted acceda a nuestro Servicio o partes de nuestro Servicio.
                -Afiliado significa una entidad que controla, es controlada por, o está bajo control común con una parte, donde "control" significa la propiedad del 50% o más de las acciones, participación accionaria u otros valores con derecho a voto para la elección de directores u otra autoridad de gestión.
                -Aplicación se refiere a Zazil, el programa de software proporcionado por la Compañía.
                -Compañía (referida como "la Compañía", "nosotros", "nos" o "nuestro" en este Acuerdo) se refiere a Fundación Todas Brillamos, Ignacio Manuel Altamirano 7A, Ex-hacienda de Santa Mónica, 54050 Tlalnepantla, Méx.
                -País se refiere a: México.
                -Dispositivo significa cualquier dispositivo que pueda acceder al Servicio, como una computadora, un teléfono móvil o una tableta digital.
                -Datos Personales son cualquier información que se relacione con un individuo identificado o identificable.
                -Servicio se refiere a la Aplicación.
                -Proveedor de Servicios significa cualquier persona física o jurídica que procese los datos en nombre de la Compañía. Se refiere a terceros o individuos empleados por la Compañía para facilitar el Servicio, proporcionar el Servicio en nombre de la Compañía, realizar servicios relacionados con el Servicio o ayudar a la Compañía a analizar cómo se utiliza el Servicio.
                -Datos de Uso se refiere a los datos recopilados automáticamente, ya sea generados por el uso del Servicio o de la propia infraestructura del Servicio (por ejemplo, la duración de una visita a una página).
                -Usted se refiere a la persona que accede o utiliza el Servicio, o la empresa u otra entidad legal en nombre de la cual dicha persona accede o utiliza el Servicio, según corresponda.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Recopilación y uso de sus datos personales",
            content = """
                Tipos de datos recopilados
                
                Datos Personales
                Mientras usa nuestro Servicio, podemos pedirle que nos proporcione cierta información de identificación personal que puede ser utilizada para contactarlo o identificarlo. La información de identificación personal puede incluir, entre otros:
                -Dirección de correo electrónico
                -Nombre y apellido
                -Número de teléfono
                -Dirección, Estado, Provincia, Código Postal, Ciudad
                -Datos de uso
                
                Datos de uso
                Los datos de uso se recopilan automáticamente cuando se utiliza el Servicio. Los datos de uso pueden incluir información como la dirección IP de su Dispositivo, tipo de navegador, versión del navegador, las páginas de nuestro Servicio que visita, la hora y la fecha de su visita, el tiempo dedicado a esas páginas, identificadores únicos de dispositivos y otros datos de diagnóstico.
                
                Cuando accede al Servicio a través de un dispositivo móvil, podemos recopilar cierta información automáticamente, que incluye, entre otros, el tipo de dispositivo móvil que utiliza, la identificación única de su dispositivo móvil, la dirección IP de su dispositivo móvil, su sistema operativo móvil, el tipo de navegador de Internet móvil que utiliza, identificadores únicos de dispositivos y otros datos de diagnóstico.
                
                También podemos recopilar información que su navegador envía cada vez que visita nuestro Servicio o cuando accede al Servicio a través de un dispositivo móvil.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Uso de sus datos personales",
            content = """
                La Compañía puede usar los Datos Personales para los siguientes propósitos:
                
                -Proporcionar y mantener nuestro Servicio, incluso para monitorear el uso de nuestro Servicio.
                -Gestionar su Cuenta: para gestionar su registro como usuario del Servicio. 
                -Para la ejecución de un contrato: el desarrollo, cumplimiento y gestión del contrato de compra de los productos.
                -Contactarlo: para actualizaciones o comunicaciones informativas.
                -Proporcionarle noticias, ofertas especiales e información general sobre productos y servicios.
                -Gestionar sus solicitudes: para atender y gestionar sus solicitudes hacia nosotros.
                -Para transferencias comerciales: Podemos usar su información para evaluar o llevar a cabo una fusión, desinversión, reestructuración, reorganización, disolución o cualquier otra venta o transferencia de algunos o todos nuestros activos, ya sea como una empresa en marcha o como parte de una quiebra, liquidación o procedimiento similar, en la cual los Datos Personales que poseemos sobre los usuarios de nuestro Servicio están entre los activos transferidos.
                -Para otros fines: Podemos utilizar su información para otros fines, como el análisis de datos, la identificación de tendencias de uso, la determinación de la efectividad de nuestras campañas promocionales y para evaluar y mejorar nuestro Servicio, productos, servicios, marketing y su experiencia.
                
                Podemos compartir su información personal en las siguientes situaciones:
                
                -Con los Proveedores de Servicios: Podemos compartir su información personal con los Proveedores de Servicios para monitorear y analizar el uso de nuestro Servicio, para contactarlo.
                -Para transferencias comerciales: Podemos compartir o transferir su información personal en relación con, o durante las negociaciones de, cualquier fusión, venta de activos de la Compañía, financiamiento o adquisición de todo o parte de nuestro negocio a otra empresa.
                -Con afiliados: Podemos compartir su información con nuestros afiliados, en cuyo caso les exigiremos a esos afiliados que respeten esta Política de Privacidad. Los afiliados incluyen nuestra empresa matriz y cualquier otra subsidiaria, socios de empresas conjuntas u otras empresas que controlamos o que están bajo control común con nosotros.
                -Con socios comerciales: Podemos compartir su información con nuestros socios comerciales para ofrecerle ciertos productos, servicios o promociones.
                -Con otros usuarios: cuando comparte información personal o interactúa en áreas públicas con otros usuarios, dicha información puede ser vista por todos los usuarios y puede ser distribuida públicamente fuera.
                -Con su consentimiento: Podemos divulgar su información personal para cualquier otro propósito con su consentimiento.

            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Retención de sus datos personales",
            content = """
                La Compañía conservará sus Datos Personales solo durante el tiempo que sea necesario para los fines establecidos en esta Política de Privacidad. Conservaremos y utilizaremos sus Datos Personales en la medida necesaria para cumplir con nuestras obligaciones legales (por ejemplo, si estamos obligados a conservar sus datos para cumplir con las leyes aplicables), resolver disputas y hacer cumplir nuestros acuerdos y políticas legales.
                
                La Compañía también conservará los Datos de Uso para fines de análisis interno. Los Datos de Uso generalmente se conservan durante un período de tiempo más corto, excepto cuando estos datos se utilizan para fortalecer la seguridad o para mejorar la funcionalidad de nuestro Servicio, o cuando estamos legalmente obligados a retener estos datos durante períodos más largos.

            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Traslado de sus datos personales",
            content = """
                Su información, incluidos los Datos Personales, se procesa en las oficinas operativas de la Compañía y en cualquier otro lugar donde se encuentren las partes involucradas en el procesamiento. Esto significa que esta información puede transferirse a —y mantenerse en— computadoras ubicadas fuera de su estado, provincia, país u otra jurisdicción gubernamental, donde las leyes de protección de datos pueden diferir de las de su jurisdicción.
                
                Su consentimiento a esta Política de Privacidad, seguido de su envío de dicha información, representa su acuerdo con esa transferencia.
                
                La Compañía tomará todas las medidas razonablemente necesarias para garantizar que sus datos se traten de manera segura y de acuerdo con esta Política de Privacidad, y no se realizará ninguna transferencia de sus Datos Personales a una organización o país, a menos que existan controles adecuados que incluyan la seguridad de sus datos y otra información personal.

            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Eliminación de sus datos personales",
            content = """
                Tiene derecho a eliminar o solicitar que lo ayudemos a eliminar los Datos Personales que hemos recopilado sobre usted.
                
                Nuestro Servicio puede darle la capacidad de eliminar cierta información sobre usted desde dentro del Servicio.
                
                Puede actualizar, enmendar o eliminar su información en cualquier momento iniciando sesión en su Cuenta, si tiene una, y visitando la sección de configuración de la cuenta que le permite gestionar su información personal. También puede contactarnos para solicitar acceso, corregir o eliminar cualquier información personal que nos haya proporcionado.
                
                Sin embargo, tenga en cuenta que podemos necesitar retener cierta información cuando tengamos una obligación legal o una base legal para hacerlo.
                
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Divulgación de sus datos personales",
            content = """
                Transacciones comerciales:
                
                Si la Compañía está involucrada en una fusión, adquisición o venta de activos, sus Datos Personales pueden ser transferidos. Le proporcionaremos un aviso antes de que sus Datos Personales sean transferidos y estén sujetos a una Política de Privacidad diferente.
                
                Cumplimiento de la ley:
                
                En determinadas circunstancias, la Compañía puede verse obligada a divulgar sus Datos Personales si lo requiere la ley o en respuesta a solicitudes válidas de autoridades públicas (por ejemplo, un tribunal o una agencia gubernamental).
                
                Otras obligaciones legales:
                La Compañía puede divulgar sus Datos Personales de buena fe si cree que dicha acción es necesaria para:
                -Cumplir con una obligación legal
                -Proteger y defender los derechos o la propiedad de la Compañía
                -Prevenir o investigar posibles irregularidades en relación con el Servicio
                -Proteger la seguridad personal de los usuarios del Servicio o del público
                -Protegerse contra la responsabilidad legal

                """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Seguridad de sus datos personales",
            content = """
                La seguridad de sus Datos Personales es importante para nosotros, pero recuerde que ningún método de transmisión a través de Internet o de almacenamiento electrónico es 100% seguro. Si bien nos esforzamos por utilizar medios comercialmente aceptables para proteger sus Datos Personales, no podemos garantizar su seguridad absoluta.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Enlaces a otros sitios web",
            content = """
                Nuestro Servicio puede contener enlaces a otros sitios web que no son operados por nosotros. Si hace clic en un enlace de un tercero, será dirigido al sitio de ese tercero. Le recomendamos encarecidamente que revise la Política de Privacidad de cada sitio que visite.
                
                No tenemos control ni asumimos responsabilidad alguna por el contenido, las políticas de privacidad o las prácticas de sitios o servicios de terceros.
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Cambios a esta Política de Privacidad",
            content = """
                Podemos actualizar nuestra Política de Privacidad de vez en cuando. Le notificaremos cualquier cambio publicando la nueva Política de Privacidad en esta página.
                
                Le informaremos por correo electrónico y/o un aviso destacado en nuestro Servicio, antes de que el cambio sea efectivo, y actualizaremos la fecha de "Última actualización" en la parte superior de esta Política de Privacidad.
                
                Se le aconseja que revise esta Política de Privacidad periódicamente para ver si hay cambios. Los cambios a esta Política de Privacidad son efectivos cuando se publican en esta página.

            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrivacyPolicyCard(
            title = "Contáctenos",
            content = """
                Si tiene alguna pregunta sobre esta Política de Privacidad, puede contactarnos por correo electrónico: contacto@fundaciontodasbrillamos.org
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Composable que representa una tarjeta para mostrar una sección de la Política de Privacidad.
 *
 * @param title Título de la sección.
 * @param content Contenido de la sección, generalmente en forma de texto.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Composable
fun PrivacyPolicyCard(title: String, content: String) {
    // Tarjeta para mostrar una sección de la política de privacidad
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Contenido de la tarjeta
        Column {
            Text(
                text = title,
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE17F61)
                ),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = Typography.bodyMedium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF333333),
                textAlign = TextAlign.Justify
            )
        }
    }
}
