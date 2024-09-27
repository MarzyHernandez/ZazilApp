package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Cart
import mx.acg.zazil.model.CartRetrofitInstance

class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<Cart?>()
    val cart: LiveData<Cart?> get() = _cart

    // Método para cargar el carrito
    fun loadCartByUid(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cartResponse = CartRetrofitInstance.cartApi.getCartByUid(uid)
                _cart.postValue(cartResponse)
                Log.d("CartViewModel", "Carrito cargado: ${cartResponse.productos.size} productos")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error cargando el carrito", e)
            }
        }
    }
}
