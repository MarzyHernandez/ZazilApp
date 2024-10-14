import kotlinx.serialization.Serializable

/**
 * Data class que representa el perfil de un usuario en la aplicación.
 *
 * Esta clase almacena información detallada sobre un usuario, incluyendo sus datos personales
 * y detalles sobre su historial de compras. Está diseñada para ser utilizada con la serialización,
 * facilitando su conversión a y desde formatos como JSON.
 *
 * @param id El identificador único del perfil del usuario.
 * @param apellidos Los apellidos del usuario.
 * @param carrito_activo Identificador del carrito activo del usuario (puede ser nulo).
 * @param pedidos Lista de identificadores de pedidos asociados con el usuario (puede ser nula).
 * @param telefono El número de teléfono del usuario.
 * @param nombres Los nombres del usuario.
 * @param email La dirección de correo electrónico del usuario.
 * @param uid El ID único del usuario (UID).
 * @param foto_perfil La URL de la foto de perfil del usuario (puede ser nula).
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
@Serializable
class UserProfile(
    val id: String,
    val apellidos: String,
    val carrito_activo: String?,
    val pedidos: List<Int>?,
    val telefono: String,
    val nombres: String,
    val email: String,
    val uid: String,
    val foto_perfil: String?
)
