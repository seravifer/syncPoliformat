package data.network

import appModule
import com.github.salomonbrys.kodein.instance
import java.io.File
import utils.Crypt
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool

object CredentialsStorageImpl : CredentialsStorage {
    override fun persistCredentials(credentials: Credentials): CompletableFuture<Unit> {
        val res = CompletableFuture<Unit>()
        ForkJoinPool.commonPool().execute {
            try {
                Crypt.encrypt("${credentials.token}\n${credentials.dns}")
                /*with(appModule.instance<File>("credentials")) {
                    createNewFile()
                    printWriter().use {
                        it.println(credentials.token)
                        it.println(credentials.dns)
                    }
                }*/
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
                res.complete(Crypt.decrypt())
                /*val data = appModule.instance<File>("credentials").readLines()
                val credentials = Credentials(data[0], data[1])
                res.complete(credentials)*/
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
                appModule.instance<File>("credentials").delete()
                res.complete(Unit)
            } catch (e: IOException) {
                res.completeExceptionally(e)
            }
        }
        return res
    }
}
