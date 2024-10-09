package mx.acg.zazil.model

data class UpdateUserRequest(
    val uid: String,
    val nombres: String,
    val apellidos: String,
    val telefono: String,
    val email: String,
    val password: String,
    val foto_perfil: String = "" // En este momento lo dejamos vacío
)
