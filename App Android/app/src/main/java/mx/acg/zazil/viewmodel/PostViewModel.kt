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
import mx.acg.zazil.model.RetrofitInstance

class PostViewModel : ViewModel() {
    // Lista de todos los posts
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    // Post seleccionado
    private val _selectedPost = MutableLiveData<Post?>()
    val selectedPost: LiveData<Post?> get() = _selectedPost

    // Método para cargar la lista de posts
    fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postList = PostRetrofitInstance.postApi.getPosts()  // Llamada a la API para obtener todos los posts
                _posts.postValue(postList)
                Log.d("PostViewModel", "Posts cargados: ${postList.size}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error cargando posts", e)
            }
        }
    }

    // Método para cargar un post específico por ID
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
