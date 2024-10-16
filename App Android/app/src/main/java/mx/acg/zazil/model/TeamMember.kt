package mx.acg.zazil.model

/**
 * Modelo de datos que representa a un miembro del equipo.
 * Incluye el nombre del integrante, su correo electrónico y el recurso de imagen.
 */
data class TeamMember(
    val name: String, // Nombre del integrante
    val email: String, // Correo del integrante
    val imageRes: Int // Recurso de la imagen
)
