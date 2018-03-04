package data.network

import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool

object CredentialsStorageImpl : CredentialsStorage {
    override fun persistCredentials(credentials: Credentials): CompletableFuture<Unit> {
        val res = CompletableFuture<Unit>()
        ForkJoinPool.commonPool().execute {
            try {
                with(Credentials.credentialsFile) {
                    createNewFile()
                    printWriter().use {
                        it.println(credentials.token)
                        it.println(credentials.dns)
                    }
                }
            } catch (e: Exception) {
                res.completeExceptionally(e)
            }
            res.complete(Unit)
        }
        return res
    }

    override fun retrieveCredentials(): CompletableFuture<Credentials> {
        val res = CompletableFuture<Credentials>()
        ForkJoinPool.commonPool().execute {
            try {
                val data = Credentials.credentialsFile.readLines()
                val credentials = Credentials(data[0], data[1])
                res.complete(credentials)
            } catch (e: Exception) {
                res.completeExceptionally(e)
            }
        }
        return res
    }

    override fun removeCredentials(): CompletableFuture<Unit> {
        val res = CompletableFuture<Unit>()
        ForkJoinPool.commonPool().execute {
            try {
                Credentials.credentialsFile.delete()
                res.complete(Unit)
            } catch (e: IOException) {
                res.completeExceptionally(e)
            }
        }
        return res
    }
}