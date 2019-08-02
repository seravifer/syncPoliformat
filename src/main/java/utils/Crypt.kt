package utils

import data.network.Credentials
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

private const val ALGO = "AES/ECB/PKCS5Padding"
private val key = "ÑtF0fÓ*^7ü;$#".toByteArray()

class EncryptedStorage(private val fileCredentials: File) {
    fun encrypt(data: String) {
        val key = generateKey()
        val cipher = Cipher.getInstance(ALGO)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val outputBytes = cipher.doFinal(data.toByteArray())

        val outputStream = FileOutputStream(fileCredentials)
        outputStream.write(outputBytes)
        outputStream.flush()
        outputStream.close()
    }

    fun decrypt(): Credentials {
        val key = generateKey()
        val cipher = Cipher.getInstance(ALGO)
        cipher.init(Cipher.DECRYPT_MODE, key)

        val inputStream = FileInputStream(fileCredentials)
        val inputBytes = ByteArray(fileCredentials.length().toInt())
        inputStream.read(inputBytes)

        val outputBytes = cipher.doFinal(inputBytes)

        val data = String(outputBytes).split("\n")

        return Credentials(data[0], data[1])
    }

    private fun generateKey(): Key {
        return SecretKeySpec(key, "AES")
    }
}
