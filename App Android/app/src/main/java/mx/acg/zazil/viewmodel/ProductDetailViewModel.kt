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
import mx.acg.zazil.model.RetrofitInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel para gestionar la lógica de negocio y los datos de la pantalla de detalles del producto.
 * Proporciona una función para cargar un producto específico por su ID.
 *
 * @property selectedProduct LiveData que contiene un objeto Product seleccionado. Puede ser observado
 * para mostrar los detalles de un producto específico en la interfaz de usuario.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ProductDetailViewModel : ViewModel() {
    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> get() = _selectedProduct

    /**
     * Método para cargar un producto específico por ID.
     * Realiza la solicitud en un hilo secundario utilizando Dispatchers.IO para evitar bloquear el hilo principal.
     * Los resultados se publican en el LiveData _selectedProduct, que puede ser observado por la interfaz de usuario.
     *
     * @param productId El ID del producto que se desea cargar.
     */
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
