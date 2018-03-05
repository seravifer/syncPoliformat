package service.impl

import data.Repository
import data.network.*
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
        private val poliformatService: PoliformatService,
        private val upvService: UpvService,
        private val credentialsStorage: CredentialsStorage,
        private val credentialsHandler: CredentialsHandler
) : AuthenticationService {

    // TODO: Abstraer mejor la conversion de Call<ResponseBody> a CompleatbleFuture<Boolean>
    override fun login(dni: String, password: String, remember: Boolean): CompletableFuture<Boolean> {
        return if (dni.isNotBlank() && password.isNotBlank()) {
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
                                val credentials = credentialsHandler.getCredentials()
                                if (remember && loggedIn && credentials != null) {
                                    credentialsStorage.persistCredentials(credentials)
                                }
                                res.complete(loggedIn)
                            } else {
                                res.completeExceptionally(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            res.completeExceptionally(t)
                        }
                    })
            res
        } else if (existSavedCredentials()) {
            credentialsStorage.retrieveCredentials().thenComposeAsync {
                credentialsHandler.loadCredentials(it)
                poliformatService.login().thenApply { true }
            }
        } else {
            CompletableFuture.completedFuture(false)
        }
    }

    override fun existSavedCredentials(): Boolean {
        return Credentials.credentialsFile.exists()
    }

    override fun currentUser(): CompletableFuture<UserInfo> = repo.getCurrentUser()

    override fun logout(): CompletableFuture<Unit> {
        credentialsHandler.cleanCredentials()
        return credentialsStorage.removeCredentials()
    }
}