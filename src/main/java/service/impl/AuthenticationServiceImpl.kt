package service.impl

import data.Repository
import data.network.CredentialsHandler
import data.network.CredentialsStorage
import data.network.PoliformatService
import data.network.UpvService
import domain.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import service.AuthenticationService
import kotlin.coroutines.CoroutineContext

class AuthenticationServiceImpl(
        private val repo: Repository,
        private val poliformatService: PoliformatService,
        private val upvService: UpvService,
        private val credentialsStorage: CredentialsStorage,
        private val credentialsHandler: CredentialsHandler
) : AuthenticationService, CoroutineScope {
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext = parentJob + Dispatchers.IO

    override suspend fun login(dni: String, password: String, remember: Boolean): Boolean = withContext(coroutineContext) {
        if (dni.isNotBlank() && password.isNotBlank()) {
            val response = upvService.login(dni, password)
            val loggedIn = response.headers()["X-Sakai-Session"] != null
            val credentials = credentialsHandler.getCredentials()
            if (remember && loggedIn && credentials != null) {
                credentialsStorage.persistCredentials(credentials)
            }
            loggedIn
        } else if (existSavedCredentials()) {
            val credentials = credentialsStorage.retrieveCredentials()
            credentialsHandler.loadCredentials(credentials)
            poliformatService.login()
            true
        } else {
            false
        }
    }

    override suspend fun existSavedCredentials(): Boolean {
        return credentialsStorage.hasCredentials()
    }

    override suspend fun currentUser(): UserInfo = withContext(coroutineContext) {
        repo.getCurrentUser()
    }

    override suspend fun logout() = withContext(coroutineContext) {
        credentialsHandler.cleanCredentials()
        credentialsStorage.removeCredentials()
    }
}
