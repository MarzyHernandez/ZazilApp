package mx.acg.zazil.model

data class Order(
    val id: Int,
    val direccion_envio: DireccionEnvio,
    val monto_total: Double,
    val fecha_envio: String?,
    val fecha_entrega: String?,
    val metodo_pago: String,
    val productos: List<ProductDetails>,
    val fecha_pedido: String,
    val uid: String,
    val estado: String
)

data class Address(
    val codigo_postal: String,
    val estado: String,
    val ciudad: String,
    val calle: String,
    val numero_interior: String?,
    val pais: String,
    val colonia: String
)

data class ProductDetails(
    val precio: Double,
    val imagen: String,
    val cantidad: Int,
    val nombre: String
)
