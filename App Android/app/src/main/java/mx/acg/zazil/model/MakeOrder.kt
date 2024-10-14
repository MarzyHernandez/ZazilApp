package mx.acg.zazil.model

/**
 * Clase de datos que representa una orden de compra.
 *
 * Esta clase se utiliza para enviar la información relacionada con el pedido del usuario,
 * incluyendo los detalles de la dirección de envío.
 *
 * @param uid Identificador único del usuario.
 * @param codigo_postal Código postal de la dirección de envío.
 * @param estado Estado de la dirección de envío.
 * @param ciudad Ciudad de la dirección de envío.
 * @param calle Calle de la dirección de envío.
 * @param numero_interior Número interior del domicilio (opcional).
 * @param pais País de la dirección de envío.
 * @param colonia Colonia de la dirección de envío.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
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
