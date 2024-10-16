package mx.acg.zazil.model

/**
 * Data class para representar la dirección de envío.
 *
 * Esta clase encapsula toda la información necesaria para la dirección de envío
 * que se utilizará durante el proceso de compra.
 *
 * @param codigo_postal El código postal de la dirección de envío.
 * @param estado El estado de la dirección de envío.
 * @param ciudad La ciudad de la dirección de envío.
 * @param calle La calle de la dirección de envío.
 * @param numero_interior El número interior de la dirección de envío (opcional).
 * @param pais El país de la dirección de envío.
 * @param colonia La colonia de la dirección de envío.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class ShippingAddress(
    val codigo_postal: String,
    val estado: String,
    val ciudad: String,
    val calle: String,
    val numero_interior: String,
    val pais: String,
    val colonia: String
)

/**
 * Data class para la respuesta del GET.
 *
 * Esta clase representa la respuesta del servidor al solicitar la dirección de envío
 * asociada con un usuario. Puede contener una dirección de envío válida o ser nula
 * si no hay dirección registrada.
 *
 * @param direccion_envio La dirección de envío obtenida, si está disponible.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class ShippingResponse(
    val direccion_envio: ShippingAddress?
)
