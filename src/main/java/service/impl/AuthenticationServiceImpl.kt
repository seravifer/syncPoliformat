package service.impl

import data.Repository
import data.network.*
import domain.UserInfo
import service.AuthenticationService
import utils.toCompletableFuture
import java.util.concurrent.CompletableFuture

class AuthenticationServiceImpl(
        private val repo: Repository,
        private val poliformatService: PoliformatService,
        private val upvService: UpvService,
        private val credentialsStorage: CredentialsStorage,
        private val credentialsHandler: CredentialsHandler
) : AuthenticationService {

    override fun login(dni: String, password: String, remember: Boolean): CompletableFuture<Boolean> {
        return if (dni.isNotBlank() && password.isNotBlank()) {
            upvService.login(dni, password).toCompletableFuture { response ->
                val loggedIn = response.headers()["X-Sakai-Session"] != null
                val credentials = credentialsHandler.getCredentials()
                if (remember && loggedIn && credentials != null) {
                    credentialsStorage.persistCredentials(credentials)
                }
                loggedIn
            }
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