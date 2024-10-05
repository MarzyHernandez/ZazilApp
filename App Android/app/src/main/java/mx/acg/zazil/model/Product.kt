
package mx.acg.zazil.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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