package service.impl

import data.Repository
import data.network.UpvService
import domain.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import service.AuthenticationService
import java.util.concurrent.CompletableFuture

class AuthenticationServiceImpl(
        private val repo: Repository,
        private val upvService: UpvService
) : AuthenticationService {

    override fun login(dni: String, password: String, remember: Boolean): CompletableFuture<Boolean> {
        if (remember) TODO("Guardar credenciales")
        val res = CompletableFuture<Boolean>()
        val loginParams = mapOf(
                "cua" to "sakai",
                "estilo" to "500",
                "id" to "c",
                "vista" to "MSE",
                "dni" to dni,
                "clau" to password)
        upvService.login(loginParams)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response?.isSuccessful == true) {
                            val loggedIn = response.headers()["X-Sakai-Session"] != null
                            res.complete(loggedIn)
                        } else {
                            res.completeExceptionally(HttpException(response))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        res.completeExceptionally(t)
                    }
                })
        return res
    }

    override fun currentUser(): CompletableFuture<UserInfo> = repo.getCurrentUser()

    override fun logout(): CompletableFuture<Unit> {
        TODO("Borrar credenciales")
    }
}