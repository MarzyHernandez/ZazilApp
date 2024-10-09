package mx.acg.zazil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.acg.zazil.model.Post
import mx.acg.zazil.model.PostRetrofitInstance

/**
 * ViewModel para gestionar la lógica de negocio y los datos de la pantalla de publicaciones (Blog).
 * Proporciona funciones para cargar una lista de publicaciones y para cargar un post específico
 * según su ID.
 *
 * @property posts LiveData que contiene una lista de objetos Post. Utilizado para observar y
 * mostrar publicaciones en la interfaz de usuario.
 * @property selectedPost LiveData que contiene un objeto Post seleccionado. Puede ser observado para
 * mostrar los detalles de una publicación específica.
 */
class PostViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>() // Lista de todos los posts
    val posts: LiveData<List<Post>> get() = _posts

    private val _selectedPost = MutableLiveData<Post?>() // Post seleccionado
    val selectedPost: LiveData<Post?> get() = _selectedPost

    /**
     * Método para cargar la lista de publicaciones desde la API.
     * Realiza la solicitud en un hilo secundario utilizando Dispatchers.IO para evitar bloquear el hilo principal.
     * Los resultados se publican en el LiveData _posts, que puede ser observado por la interfaz de usuario.
     */
    fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postList = PostRetrofitInstance.postApi.getPosts()
                _posts.postValue(postList)
                Log.d("PostViewModel", "Posts cargados: ${postList.size}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error cargando posts", e)
            }
        }
    }

    /**
     * Método para cargar un post específico por su ID desde la API.
     * Realiza la solicitud en un hilo secundario utilizando Dispatchers.IO para evitar bloquear el hilo principal.
     * Los resultados se publican en el LiveData _selectedPost, que puede ser observado para mostrar los detalles del post.
     *
     * @param postId El ID de la publicación que se desea cargar.
     */
    fun loadPostById(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val post = PostRetrofitInstance.postApi.getPostById(postId)
                _selectedPost.postValue(post)
                Log.d("PostViewModel", "Post cargado: ${post.titulo}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error cargando post por ID", e)
            }
        }
    }
}
