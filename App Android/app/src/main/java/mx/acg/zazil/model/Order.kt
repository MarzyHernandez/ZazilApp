package mx.acg.zazil.model

data class Order(
    val id: Int,
    val direccion_envio: Address,
    val monto_total: Double,
    val fecha_pedido: String,
    val estado: String,
    val productos: List<ProductDetails>,
)

data class Address(
    val codigo_postal: Int,
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
