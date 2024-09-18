package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Product
import mx.acg.zazil.model.RetrofitInstance

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>() // Lista de todos los productos
    val products: LiveData<List<Product>> get() = _products

    private val _selectedProduct = MutableLiveData<Product?>() // Producto seleccionado
    val selectedProduct: LiveData<Product?> get() = _selectedProduct

    // Método para cargar la lista de productos
    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productList = RetrofitInstance.productApi.getProducts()
                _products.postValue(productList)
                Log.d("ProductViewModel", "Productos cargados: ${productList.size}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error cargando productos", e)
            }
        }
    }

    // Método para cargar un producto específico por ID
    fun loadProductById(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = RetrofitInstance.productDetailApi.getProductById(productId)
                _selectedProduct.postValue(product)
                Log.d("ProductViewModel", "Producto cargado: ${product.nombre}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error cargando producto por ID", e)
            }
        }
    }
}
