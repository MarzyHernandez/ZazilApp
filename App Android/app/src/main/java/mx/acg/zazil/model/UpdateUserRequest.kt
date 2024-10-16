package mx.acg.zazil.model

/**
 * Data class que representa la solicitud para actualizar el perfil de un usuario.
 *
 * Esta clase contiene los campos necesarios para realizar una actualización del perfil
 * de un usuario en la base de datos. Los datos se envían al servidor mediante una solicitud
 * HTTP para actualizar la información del usuario.
 *
 * @param uid El ID único del usuario (UID) que se está actualizando.
 * @param nombres Los nombres del usuario.
 * @param apellidos Los apellidos del usuario.
 * @param telefono El número de teléfono del usuario.
 * @param email La dirección de correo electrónico del usuario.
 * @param password La contraseña del usuario.
 * @param foto_perfil La URL de la foto de perfil del usuario. Se establece como vacío por defecto.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class UpdateUserRequest(
    val uid: String,
    val nombres: String,
    val apellidos: String,
    val telefono: String,
    val email: String,
    val password: String,
    val foto_perfil: String = "" // En este momento lo dejamos vacío
)
