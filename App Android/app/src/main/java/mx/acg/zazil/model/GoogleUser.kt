package mx.acg.zazil.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class GoogleUser {
    private val client = OkHttpClient()

    @Throws(IOException::class)
    fun sendUserData(email: String, uid: String): String? {
        val url = "https://newuser-dztx2pd2na-uc.a.run.app"
        val json = JSONObject().apply {
            put("email", email)
            put("uid", uid)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                response.body?.string()
            } else {
                throw IOException("Unexpected code $response")
            }
        }
    }
}