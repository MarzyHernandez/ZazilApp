package mx.acg.zazil.model

data class MakeOrder(
    val uid: String,
    val codigo_postal: Int,
    val estado: String,
    val ciudad: String,
    val calle: String,
    val numero_interior: String,
    val pais: String,
    val colonia: String
)