package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Modelo que representa un carrito de compras.
 * Contiene la información sobre el estado del carrito, el monto total, la fecha de creación,
 * el identificador de usuario (UID), y la lista de productos dentro del carrito.
 *
 * @property id Identificador del carrito.
 * @property estado Estado del carrito (true si está activo, false si no lo está).
 * @property monto_total Monto total de los productos en el carrito.
 * @property fecha_creacion Fecha de creación del carrito.
 * @property uid Identificador único del usuario propietario del carrito.
 * @property productos Lista de productos dentro del carrito.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class Cart(
    val id: String,
    val estado: Boolean,
    val monto_total: Double,
    val fecha_creacion: FechaCreacion,
    val uid: String,
    val productos: List<CartProduct>
)

/**
 * Modelo que representa la fecha de creación del carrito.
 * Utiliza segundos y nanosegundos para almacenar la fecha.
 *
 * @property _seconds Segundos desde el epoch (Unix timestamp).
 * @property _nanoseconds Nanosegundos adicionales.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class FechaCreacion(
    val _seconds: Long,
    val _nanoseconds: Long
)

/**
 * Modelo que representa un producto dentro del carrito de compras.
 * Contiene el identificador del producto y la cantidad agregada al carrito.
 *
 * @property id_producto Identificador único del producto.
 * @property cantidad Cantidad del producto en el carrito.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class CartProduct(
    val id_producto: Int,
    val cantidad: Int
)

/**
 * Modelo de datos que representa el cuerpo de la solicitud para actualizar el carrito.
 * Se utiliza cuando se agrega o actualiza un producto en el carrito.
 *
 * @property uid Identificador único del usuario propietario del carrito.
 * @property id_producto Identificador del producto a agregar o actualizar.
 * @property cantidad Cantidad del producto a agregar o actualizar.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
data class CartUpdate(
    val uid: String,
    val id_producto: Int,
    val cantidad: Int
)

/**
 * Singleton que configura y crea una instancia de Retrofit para la API del carrito.
 * Esta instancia de Retrofit se usa para realizar solicitudes a la API del carrito de compras.
 *
 * @property cartApi Instancia de la interfaz CartApi creada a partir de Retrofit.
 * Esta instancia se usa para acceder a los endpoints de la API del carrito.
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
object CartRetrofitInstance {
    val cartApi: CartApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getactivecart-dztx2pd2na-uc.a.run.app/") // Base URL de la API del carrito
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApi::class.java)
    }
}
