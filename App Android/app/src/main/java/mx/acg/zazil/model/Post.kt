package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Query("id") postId: Int): Post
}

object PostRetrofitInstance {
    val postApi: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://getallpost-dztx2pd2na-uc.a.run.app/")  // Cambia a tu URL de la API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }
}
