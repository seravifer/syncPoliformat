package service.impl

import data.Repository
import data.network.UpvService
import domain.UserInfo
import service.AuthenticationService
import utils.toCompletableFuture
import java.util.concurrent.CompletableFuture

class AuthenticationServiceImpl(
        private val repo: Repository,
        private val upvService: UpvService
) : AuthenticationService {

    override fun login(dni: String, password: String, remember: Boolean): CompletableFuture<Boolean> {
        if (remember) TODO("Guardar credenciales")
        val loginParams = mapOf(
                "cua" to "sakai",
                "estilo" to "500",
                "id" to "c",
                "vista" to "MSE",
                "dni" to dni,
                "clau" to password)
        return upvService.login(loginParams)
                .toCompletableFuture { response -> response.headers()["X-Sakai-Session"] != null }
    }

    override fun currentUser(): CompletableFuture<UserInfo> = repo.getCurrentUser()

    override fun logout(): CompletableFuture<Unit> {
        TODO("Borrar credenciales")
    }
}