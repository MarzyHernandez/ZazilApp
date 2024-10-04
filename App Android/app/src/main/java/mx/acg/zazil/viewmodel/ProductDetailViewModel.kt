package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Product
import mx.acg.zazil.model.ProductApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailViewModel : ViewModel() {
    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> get() = _selectedProduct

    object RetrofitInstance {
        val productDetailApi: ProductApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://getproductbyid-dztx2pd2na-uc.a.run.app/") // Base URL para obtener producto por ID
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductApi::class.java)
        }
    }

    // Método para cargar un producto específico por ID
    fun loadProductById(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = RetrofitInstance.productDetailApi.getProductById(productId)
                _selectedProduct.postValue(product)
                Log.d("ProductDetailViewModel", "Producto cargado: ${product.nombre}")
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error cargando producto", e)
                _selectedProduct.postValue(null) // Manejar el caso donde no se encuentra el producto
            }
        }
    }
}
