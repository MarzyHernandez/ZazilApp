package mx.acg.zazil.model

/**
 * Data class que representa a un usuario para el proceso de registro.
 *
 * Esta clase se utiliza para encapsular los datos del usuario que se enviarán al servidor durante el registro.
 *
 * @param email El correo electrónico del usuario.
 * @param password La contraseña del usuario.
 * @param nombres Los nombres del usuario.
 * @param apellidos Los apellidos del usuario.
 * @param telefono El número de teléfono del usuario.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class User(
    val email: String,
    val password: String,
    val nombres: String,
    val apellidos: String,
    val telefono: String
)

/**
 * Data class que representa la respuesta del servidor después de un intento de registro.
 *
 * Esta clase encapsula la información que el servidor devuelve tras una solicitud de registro.
 *
 * @param success Un valor booleano que indica si el registro fue exitoso o no.
 * @param message Un mensaje de respuesta del servidor (puede ser éxito o error).
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class RegisterResponse(
    val success: Boolean,
    val message: String
)
