package mx.acg.zazil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.acg.zazil.model.DeleteAccountApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeleteAccountViewModel : ViewModel() {

    private val deleteAccountApi: DeleteAccountApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://deleteuserbyid-dztx2pd2na-uc.a.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DeleteAccountApi::class.java)
    }

    fun deleteAccount(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = deleteAccountApi.deleteAccount(uid)
                if (response.isSuccessful) {
                    onSuccess() // Eliminar cuenta con éxito
                } else {
                    onError("Error al eliminar la cuenta")
                }
            } catch (e: Exception) {
                onError("Error de red: ${e.message}")
            }
        }
    }
}
