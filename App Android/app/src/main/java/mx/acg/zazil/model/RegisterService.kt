package mx.acg.zazil.model

/**
 * Data class que representa a un usuario para el proceso de registro.
 *
 * @param email El correo electrónico del usuario.
 * @param password La contraseña del usuario.
 * @param nombres Los nombres del usuario.
 * @param apellidos Los apellidos del usuario.
 * @param telefono El número de teléfono del usuario.
 *
 * @author Melissa Mireles Rendón
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
 * @param message Un mensaje de respuesta del servidor (puede ser éxito o error).
 */
data class RegisterResponse(
    val success: Boolean,
    val message: String
)


