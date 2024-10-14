package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Data class que representa una publicación del blog.
 *
 * @param id Identificador único de la publicación.
 * @param fecha Fecha de creación de la publicación.
 * @param contenido Contenido de la publicación.
 * @param categoria Categoría a la que pertenece la publicación.
 * @param titulo Título de la publicación.
 * @param imagen URL de la imagen asociada a la publicación.
 * @param meGusta Número de "me gusta" que ha recibido la publicación.
 * @param autor Nombre del autor de la publicación.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
data class Post(
    val id: Int,
    val fecha: String,
    val contenido: String,
    val categoria: String,
    val titulo: String,
    val imagen: String,
    val meGusta: Int,
    val autor: String
)

/**
 * Interfaz que define las operaciones de la API relacionadas con las publicaciones.
 *
 * Proporciona métodos para obtener una lista de publicaciones y obtener una publicación específica
 * por su identificador.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
interface PostApi {
    /**
     * Obtiene una lista de todas las publicaciones.
     *
     * @return Lista de publicaciones.
     */
    @GET("posts")
    suspend fun getPosts(): List<Post>

    /**
     * Obtiene una publicación específica por su identificador.
     *
     * @param postId Identificador de la publicación que se desea obtener.
     * @return La publicación correspondiente al identificador proporcionado.
     */
    @GET("posts/{id}")
    suspend fun getPostById(@Query("id") postId: Int): Post
}

/**
 * Objeto singleton que proporciona una instancia de la API de publicaciones utilizando Retrofit.
 *
 * Utiliza una configuración de Retrofit para conectarse al servidor de publicaciones.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
object PostRetrofitInstance {
    val postApi: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getallpost-dztx2pd2na-uc.a.run.app/")  // Cambia a tu URL de la API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }
}
