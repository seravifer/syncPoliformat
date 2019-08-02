package data.network

import utils.EncryptedStorage
import java.io.File

class CredentialsStorageImpl(private val credentialsFile: File) : CredentialsStorage {
    private val encryptedStorage = EncryptedStorage(credentialsFile)

    override suspend fun persistCredentials(credentials: Credentials) {
        encryptedStorage.encrypt("${credentials.token}\n${credentials.dns}")
    }

    override suspend fun retrieveCredentials(): Credentials {
        return encryptedStorage.decrypt()
    }

    override suspend fun removeCredentials() {
        credentialsFile.delete()
    }

    override suspend fun hasCredentials(): Boolean {
        return credentialsFile.exists()
    }
}
