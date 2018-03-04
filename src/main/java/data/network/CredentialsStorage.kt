package data.network

import java.util.concurrent.CompletableFuture

interface CredentialsStorage {
    fun persistCredentials(credentials: Credentials): CompletableFuture<Unit>
    fun retrieveCredentials(): CompletableFuture<Credentials>
    fun removeCredentials(): CompletableFuture<Unit>
}