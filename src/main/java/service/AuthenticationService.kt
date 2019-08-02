package service

import domain.UserInfo

interface AuthenticationService {
    suspend fun login(dni: String = "", password: String = "", remember: Boolean = false): Boolean
    suspend fun existSavedCredentials(): Boolean
    suspend fun currentUser(): UserInfo
    suspend fun logout()
}
