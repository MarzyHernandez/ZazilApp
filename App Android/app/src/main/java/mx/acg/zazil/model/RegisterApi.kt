package mx.acg.zazil.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de la API para realizar el registro de usuarios usando Retrofit.
 * Define los endpoints relacionados con las operaciones de registro.
 *
 * Este servicio permite registrar nuevos usuarios en el sistema mediante una solicitud HTTP POST.
 *
 * @param user Un objeto de tipo [User] que contiene los datos del usuario a registrar.
 * @return Un objeto [Call] que encapsula la respuesta del servidor en un objeto [RegisterResponse].
 *
 * @author Melissa Mireles Rendón
 * @author Alberto Cebreros González
 */
interface RegisterApi {

    /**
     * Método para registrar un nuevo usuario en el servidor.
     *
     * @param user Objeto que representa al usuario que se desea registrar.
     * @return Un objeto [Call] que representa la respuesta del servidor.
     *         Si la solicitud es exitosa, contiene un objeto [RegisterResponse] con información sobre el registro.
     * @throws Exception Si ocurre un error al realizar la solicitud.
     */
    @POST("/") // Endpoint para registrar un usuario
    fun registerUser(@Body user: User): Call<RegisterResponse>
}
