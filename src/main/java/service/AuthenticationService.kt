package service

import domain.UserInfo
import java.util.concurrent.CompletableFuture

interface AuthenticationService {
    fun login(dni: String, password: String, remember: Boolean = false): CompletableFuture<Boolean>
    fun currentUser(): CompletableFuture<UserInfo>
    fun logout(): CompletableFuture<Unit>
}