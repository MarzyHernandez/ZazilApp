package mx.acg.zazil.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mx.acg.zazil.model.FAQItem
import mx.acg.zazil.model.FAQService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ViewModel para manejar la lógica de obtención de datos
class   FAQViewModel : ViewModel() {
    private val _faqItems = mutableStateOf<List<FAQItem>>(emptyList())
    val faqItems: State<List<FAQItem>> = _faqItems

    private val _isLoading = mutableStateOf(true) // Estado de carga inicializado en verdadero
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchFAQs()
    }

    private fun fetchFAQs() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://getallfaq-dztx2pd2na-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FAQService::class.java)
        service.getAllFAQs().enqueue(object : Callback<List<FAQItem>> {
            override fun onResponse(call: Call<List<FAQItem>>, response: Response<List<FAQItem>>) {
                if (response.isSuccessful) {
                    _faqItems.value = response.body() ?: emptyList()
                }
                _isLoading.value = false // Ocultar el loader cuando se completa la carga
            }

            override fun onFailure(call: Call<List<FAQItem>>, t: Throwable) {
                // Manejo de errores
                t.printStackTrace()
                _isLoading.value = false // Ocultar el loader incluso si hay un error
            }
        })
    }
}
