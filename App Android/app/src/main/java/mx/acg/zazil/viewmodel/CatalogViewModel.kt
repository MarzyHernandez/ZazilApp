package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import mx.acg.zazil.model.Product
import mx.acg.zazil.model.RetrofitInstance

class CatalogViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData para manejar posibles errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Método para obtener los productos desde la API
    fun getProducts() {
        _isLoading.value = true // Inicia la carga
        viewModelScope.launch {
            try {
                val productList = RetrofitInstance.productApi.getProducts()
                _products.value = productList
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false // Finaliza la carga
            }
        }
    }
}
