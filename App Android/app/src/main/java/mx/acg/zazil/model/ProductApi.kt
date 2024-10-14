package mx.acg.zazil.model

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz que define las operaciones de la API para gestionar productos.
 * Utiliza Retrofit para realizar solicitudes HTTP y obtener datos relacionados con productos.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface ProductApi {

    /**
     * Obtiene la lista de todos los productos disponibles en la tienda.
     *
     * @return Una lista de objetos [Product] que representan los productos disponibles.
     * @throws Exception Si ocurre un error al realizar la solicitud.
     */
    @GET("/") // Obtener todos los productos
    suspend fun getProducts(): List<Product>

    /**
     * Obtiene un producto específico basado en su identificador único.
     *
     * @param productId El identificador único del producto que se desea obtener.
     * @return Un objeto [Product] que representa el producto solicitado.
     * @throws Exception Si ocurre un error al realizar la solicitud o si el producto no se encuentra.
     */
    @GET("/") // Obtener un producto por ID
    suspend fun getProductById(@Query("id") productId: Int): Product
}
