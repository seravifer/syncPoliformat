package utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.CompletableFuture

fun <T, R> Call<T>.toCompletableFuture(transform: (response: Response<T>) -> R): CompletableFuture<R> {
    val result = CompletableFuture<R>()
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            result.completeExceptionally(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            if (response?.isSuccessful == true) {
                result.complete(transform(response))
            } else {
                result.completeExceptionally(HttpException(response))
            }
        }

    })
    return result
}
