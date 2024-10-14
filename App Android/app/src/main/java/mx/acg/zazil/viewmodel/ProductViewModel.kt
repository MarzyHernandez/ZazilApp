// ProductViewModel.kt
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

/**
 * ViewModel para gestionar la lógica de negocio y los datos de los productos.
 * Proporciona funciones para cargar productos específicos por ID.
 *
 * @property loadedProducts LiveData que contiene un mapa de productos cargados, indexados por su ID.
 * Este mapa permite acceder a los productos de manera eficiente.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ProductViewModel : ViewModel() {

    private val _loadedProducts = MutableLiveData<Map<Int, Product>>() // Mapa de productos cargados por ID
    val loadedProducts: LiveData<Map<Int, Product>> get() = _loadedProducts

    private val productsMap = mutableMapOf<Int, Product>()  // Mapa para almacenar los productos cargados

    /**
     * Método para cargar un producto específico por ID.
     * Realiza la solicitud en un hilo secundario utilizando Dispatchers.IO para evitar bloquear el hilo principal.
     * Al finalizar, llama al callback proporcionado con el producto cargado.
     *
     * @param productId El ID del producto que se desea cargar.
     * @param onProductLoaded Callback que se invoca con el producto cargado.
     */
    fun loadProductById(productId: Int, onProductLoaded: (Product) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = RetrofitInstance.productDetailApi.getProductById(productId)
                onProductLoaded(product)
                Log.d("ProductViewModel", "Producto cargado: ${product.nombre}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error cargando producto por ID", e)
            }
        }
    }
}
