package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Data class que representa un producto en la tienda.
 *
 * @param id Identificador único del producto.
 * @param descripcion Descripción detallada del producto.
 * @param id_categoria Identificador de la categoría a la que pertenece el producto (opcional).
 * @param precio_normal Precio original del producto.
 * @param cantidad Cantidad disponible del producto en inventario.
 * @param nombre Nombre del producto.
 * @param precio_rebajado Precio rebajado del producto, si aplica.
 * @param imagen URL de la imagen del producto.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class Product(
    val id: Int,
    val descripcion: String,
    val id_categoria: String?,
    val precio_normal: Double,
    val cantidad: Int,
    val nombre: String,
    val precio_rebajado: Double,
    val imagen: String
)

/**
 * Objeto singleton que proporciona instancias de API para gestionar productos utilizando Retrofit.
 *
 * Proporciona métodos para obtener todos los productos y para obtener un producto específico
 * por su identificador.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
object RetrofitInstance {
    val productApi: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getallproducts-dztx2pd2na-uc.a.run.app/") // Base URL para obtener todos los productos
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    val productDetailApi: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getproductbyid-dztx2pd2na-uc.a.run.app/") // Base URL para obtener producto por ID
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}
