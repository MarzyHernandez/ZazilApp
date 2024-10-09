
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
import mx.acg.zazil.model.CartUpdate
import okhttp3.ResponseBody
import retrofit2.Response


class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<Cart?>()
    val cart: LiveData<Cart?> get() = _cart

    private val _cartUpdated = MutableLiveData<Boolean>()
    val cartUpdated: LiveData<Boolean> get() = _cartUpdated

    //cartState
    private val _cartState = MutableLiveData<Cart?>()
    val cartState: LiveData<Cart?> get() = _cartState


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

    // Método para agregar un producto al carrito
    fun addToCart(productId: Int, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cartUpdate = CartUpdate(
                    uid = uid,
                    id_producto = productId,
                    cantidad = 1
                )

                // Llama a la API para actualizar el carrito
                val response = CartRetrofitInstance.cartApi.updateCart(cartUpdate)

                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Producto agregado al carrito: $productId")
                } else {
                    Log.e("CartViewModel", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
                loadCartByUid(uid)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error agregando producto al carrito", e)
            }
        }
    }

    // Método para quitar un producto del carrito
    fun removeFromCart(productId: Int, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cartUpdate = CartUpdate(
                    uid = uid,
                    id_producto = productId,
                    cantidad = -1
                )
                val response = CartRetrofitInstance.cartApi.updateCart(cartUpdate)
                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Producto removido del carrito: $productId")
                } else {
                    Log.e("CartViewModel", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
                loadCartByUid(uid)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error removiendo producto del carrito", e)
            }
        }
    }


}