package mx.acg.zazil.viewmodel

import UserProfile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.Json

/**
 * ViewModel para gestionar la lógica relacionada con el perfil del usuario.
 * Se encarga de obtener los datos del perfil a través de una llamada a la API.
 *
 * @property _profileData Estado interno que almacena los datos del perfil del usuario.
 * @property profileData Flows que expone los datos del perfil como un StateFlow.
 * @property errorMessage LiveData que contiene mensajes de error relacionados con la obtención de datos.
 *
 * @author Alberto Cebreros González
 * @author Melissa Mireles Rendón
 */
class ProfileViewModel : ViewModel() {
    private val _profileData = MutableStateFlow<UserProfile?>(null)
    val profileData: StateFlow<UserProfile?> = _profileData

    // Para manejar errores o mensajes
    val errorMessage = MutableLiveData<String>()

    /**
     * Método para obtener los datos del perfil del usuario a partir de su UID.
     * Realiza la solicitud en un hilo de fondo utilizando corutinas.
     *
     * @param uid El ID único del usuario (UID) para obtener el perfil.
     */
    fun fetchUserProfile(uid: String) {
        viewModelScope.launch {
            try {
                println("Fetching user profile for UID: $uid")
                val profile = getUserProfileFromApi(uid)
                println("Profile data fetched: $profile")
                _profileData.value = profile
            } catch (e: Exception) {
                println("Error fetching profile: ${e.message}")
                errorMessage.postValue("Error al obtener los datos del perfil")
            }
        }
    }

    /**
     * Método privado que realiza una solicitud GET a la API para obtener el perfil del usuario.
     *
     * @param uid El ID único del usuario (UID) para obtener el perfil.
     * @return Un objeto UserProfile que contiene los datos del usuario.
     * @throws Exception Si ocurre un error durante la conexión o la serialización.
     */
    private suspend fun getUserProfileFromApi(uid: String): UserProfile {
        return withContext(Dispatchers.IO) {
            val url = URL("https://getuserdata-dztx2pd2na-uc.a.run.app/?uid=$uid")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Configurar el JSON parser para que ignore campos desconocidos
                    val jsonParser = Json { ignoreUnknownKeys = true }
                    println(response)
                    // Parsear la respuesta JSON
                    jsonParser.decodeFromString<UserProfile>(response)
                } else {
                    throw Exception("Error de servidor: código de respuesta $responseCode")
                }
            } catch (e: kotlinx.serialization.SerializationException) {
                println("Error de serialización: ${e.localizedMessage}")
                throw Exception("Error al parsear la respuesta del servidor")
            } catch (e: Exception) {
                throw Exception("Error en la conexión: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }
    }
}
