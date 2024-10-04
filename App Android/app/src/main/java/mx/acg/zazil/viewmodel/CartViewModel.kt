package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.AddToCartRequest
import mx.acg.zazil.model.Cart
import mx.acg.zazil.model.CartApi
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<Cart?>()
    val cart: LiveData<Cart?> get() = _cart

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Singleton para Retrofit de la API del carrito
    object CartRetrofitInstance {
        val cartApi: CartApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://getactivecart-dztx2pd2na-uc.a.run.app/") // Base URL de la API del carrito
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CartApi::class.java)
        }
    }

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

    // Obtener el carrito del usuario por UID
    fun fetchCart(uid: String) {
        viewModelScope.launch {
            try {
                val cartData = CartRetrofitInstance.cartApi.getCartByUid(uid)
                _cart.value = cartData
                Log.d(
                    "CartViewModel",
                    "Carrito actualizado con ${cartData.productos.size} productos"
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener el carrito: ${e.message}"
            }
        }
    }

    // Agregar producto al carrito
    fun addProductToCart(uid: String, productId: Int, quantity: Int) {
        viewModelScope.launch {
            try {
                // Asegúrate de que el uid esté presente
                if (uid.isNotBlank()) {
                    Log.d("CartViewModel", "UID encontrado: $uid")

                    // Enviar la solicitud a la API con el uid en la URL
                    CartRetrofitInstance.cartApi.addProductToCart(
                        uid = uid,
                        productId = productId,
                        quantity = quantity
                    )

                    // Refrescar el carrito después de agregar el producto
                    fetchCart(uid)
                } else {
                    Log.e("CartViewModel", "El UID está vacío, no se puede agregar al carrito")
                }
            } catch (e: HttpException) {
                Log.e("CartViewModel", "Error al agregar producto al carrito: ${e.message()}")
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("CartViewModel", "Error body: $errorBody")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error al agregar producto al carrito: ${e.message}")
            }
        }
    }
}