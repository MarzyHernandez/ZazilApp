package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Product
import mx.acg.zazil.model.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class ProductViewModel : ViewModel() {

    // Variable que almacena la lista de productos obtenida de la API
    var productList: List<Product> = emptyList()
        private set

    // Variable que almacena el producto seleccionado
    var selectedProduct: Product? = null
        private set

    // Método para cargar todos los productos desde la API
    fun loadProducts() {
        viewModelScope.launch {
            try {
                // Llamamos a la API y actualizamos la lista de productos
                productList = RetrofitInstance.productApi.getProducts()
            } catch (e: IOException) {
                // Manejo de errores de red o IO
                e.printStackTrace()
            } catch (e: HttpException) {
                // Manejo de errores HTTP
                e.printStackTrace()
            }
        }
    }

    // Método para seleccionar un producto por ID
    fun selectProductById(id: Int) {
        selectedProduct = productList.find { it.id == id }
    }
}
