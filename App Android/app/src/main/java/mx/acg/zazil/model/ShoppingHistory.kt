package mx.acg.zazil.model

// Data class que representa los detalles de una dirección de envío
data class DireccionEnvio(
    val codigo_postal: Int,  // Código postal de la dirección
    val estado: String,       // Estado de la dirección
    val ciudad: String,       // Ciudad de la dirección
    val calle: String,        // Calle de la dirección
    val numero_interior: String?, // Número interior de la dirección (opcional)
    val pais: String,         // País de la dirección
    val colonia: String       // Colonia de la dirección
)

/**
 * Data class que representa los detalles de un producto dentro de un pedido.
 *
 * @param precio El precio del producto.
 * @param imagen La URL de la imagen del producto.
 * @param cantidad La cantidad de este producto en el pedido.
 * @param nombre El nombre del producto.
 */
data class Producto(
    val precio: Double,       // Precio del producto
    val imagen: String,       // Imagen del producto
    val cantidad: Int,        // Cantidad del producto en el pedido
    val nombre: String         // Nombre del producto
)

/**
 * Data class que representa el historial de compras de un usuario.
 *
 * @param id Identificador único del historial de compras.
 * @param direccion_envio La dirección de envío asociada al pedido.
 * @param fecha_pedido La fecha en la que se realizó el pedido.
 * @param estado El estado actual del pedido.
 * @param monto_total El monto total del pedido.
 * @param fecha_envio La fecha de envío del pedido (puede ser nula).
 * @param fecha_entrega La fecha de entrega del pedido (puede ser nula).
 * @param metodo_pago El método de pago utilizado para el pedido.
 * @param productos Lista de productos que se incluyen en el pedido.
 * @param uid El ID del usuario que realizó el pedido.
 */
data class ShoppingHistory(
    val id: Int,                   // Identificador del historial de compras
    val direccion_envio: DireccionEnvio, // Dirección de envío del pedido
    val fecha_pedido: String,      // Fecha en que se realizó el pedido
    val estado: String,            // Estado del pedido
    val monto_total: Double,       // Monto total del pedido
    val fecha_envio: String?,      // Fecha de envío (puede ser nula)
    val fecha_entrega: String?,     // Fecha de entrega (puede ser nula)
    val metodo_pago: String,       // Método de pago utilizado
    val productos: List<Producto>,  // Lista de productos en el pedido
    val uid: String                // ID del usuario
)
