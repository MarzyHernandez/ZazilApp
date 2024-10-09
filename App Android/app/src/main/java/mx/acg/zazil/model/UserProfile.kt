import kotlinx.serialization.Serializable

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
    val foto_perfil:String?
)