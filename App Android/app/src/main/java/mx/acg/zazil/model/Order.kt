package mx.acg.zazil.model

/**
 * Data class que representa una orden de compra.
 *
 * Esta clase contiene toda la información relacionada con un pedido,
 * incluyendo detalles de la dirección de envío, monto total, fechas,
 * métodos de pago y productos incluidos en la orden.
 *
 * @param id Identificador único de la orden.
 * @param direccion_envio Objeto [DireccionEnvio] que contiene los detalles de la dirección de envío.
 * @param monto_total Monto total de la orden.
 * @param fecha_envio Fecha en la que se envía la orden (puede ser nula).
 * @param fecha_entrega Fecha en la que se espera la entrega de la orden (puede ser nula).
 * @param metodo_pago Método de pago utilizado para la orden.
 * @param productos Lista de objetos [ProductDetails] que representan los productos en la orden.
 * @param fecha_pedido Fecha en la que se realizó el pedido.
 * @param uid Identificador único del usuario que realiza la compra.
 * @param estado Estado actual de la orden (ej. "En proceso", "Entregado", etc.).
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class Order(
    val id: Int,
    val direccion_envio: Address,
    val monto_total: Double,
    val fecha_envio: String?,
    val fecha_entrega: String?,
    val metodo_pago: String,
    val productos: List<ProductDetails>,
    val fecha_pedido: String,
    val uid: String,
    val estado: String
)

/**
 * Data class que representa una dirección de envío.
 *
 * Esta clase contiene los detalles necesarios para la entrega de un pedido.
 *
 * @param codigo_postal Código postal de la dirección.
 * @param estado Estado de la dirección.
 * @param ciudad Ciudad de la dirección.
 * @param calle Calle de la dirección.
 * @param numero_interior Número interior de la dirección (opcional).
 * @param pais País de la dirección.
 * @param colonia Colonia de la dirección.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class Address(
    val codigo_postal: String,
    val estado: String,
    val ciudad: String,
    val calle: String,
    val numero_interior: String?,
    val pais: String,
    val colonia: String
)

/**
 * Data class que representa los detalles de un producto en una orden.
 *
 * Esta clase contiene información sobre el producto, incluyendo el precio,
 * imagen, cantidad y nombre del producto.
 *
 * @param precio Precio del producto.
 * @param imagen URL de la imagen del producto.
 * @param cantidad Cantidad del producto en la orden.
 * @param nombre Nombre del producto.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class ProductDetails(
    val precio: Double,
    val imagen: String,
    val cantidad: Int,
    val nombre: String
)
