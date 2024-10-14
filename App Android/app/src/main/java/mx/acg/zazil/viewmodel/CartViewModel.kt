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

/**
 * ViewModel para gestionar la lógica relacionada con el carrito de compras.
 *
 * Este ViewModel interactúa con la API del carrito y maneja el estado del carrito
 * en la aplicación. Permite cargar el carrito de un usuario, agregar y quitar productos
 * del carrito, y notificar a la interfaz de usuario sobre los cambios en el estado del carrito.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<Cart?>()
    val cart: LiveData<Cart?> get() = _cart

    private val _cartUpdated = MutableLiveData<Boolean>()
    val cartUpdated: LiveData<Boolean> get() = _cartUpdated

    // Estado del carrito
    private val _cartState = MutableLiveData<Cart?>()
    val cartState: LiveData<Cart?> get() = _cartState

    /**
     * Carga el carrito de compras del usuario utilizando su UID.
     * Se realiza una llamada a la API para obtener el carrito y actualizar el estado.
     *
     * @param uid El ID único del usuario.
     */
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

    /**
     * Agrega un producto al carrito de compras.
     *
     * @param productId El ID del producto a agregar.
     * @param uid El ID único del usuario.
     */
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

    /**
     * Quita un producto del carrito de compras.
     *
     * @param productId El ID del producto a quitar.
     * @param uid El ID único del usuario.
     */
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
