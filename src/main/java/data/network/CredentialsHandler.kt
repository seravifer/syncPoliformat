package data.network

interface CredentialsHandler {
    fun getCredentials(): Credentials?
    fun loadCredentials(credentials: Credentials)
    fun cleanCredentials()
}