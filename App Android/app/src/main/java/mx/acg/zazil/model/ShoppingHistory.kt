package mx.acg.zazil.model

// Data class que representa los detalles de una dirección de envío
data class DireccionEnvio(
    val codigo_postal: Int,
    val estado: String,
    val ciudad: String,
    val calle: String,
    val numero_interior: String?,
    val pais: String,
    val colonia: String
)

// Data class que representa los detalles de un producto dentro de un pedido
data class Producto(
    val precio: Double,
    val imagen: String,
    val cantidad: Int,
    val nombre: String
)

// Data class que representa el historial de compras de un usuario
data class ShoppingHistory(
    val id: Int,
    val direccion_envio: DireccionEnvio,
    val fecha_pedido: String,
    val estado: String,
    val monto_total: Double,
    val fecha_envio: String?,
    val fecha_entrega: String?,
    val metodo_pago: String,
    val productos: List<Producto>,
    val uid: String
)
