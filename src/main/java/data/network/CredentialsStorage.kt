package data.network

interface CredentialsStorage {
    suspend fun persistCredentials(credentials: Credentials)
    suspend fun retrieveCredentials(): Credentials
    suspend fun removeCredentials()
    suspend fun hasCredentials(): Boolean
}
