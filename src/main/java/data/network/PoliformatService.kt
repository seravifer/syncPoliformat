package data.network

import domain.ContentEntity
import domain.SiteEntity
import domain.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import java.util.concurrent.CompletableFuture

interface PoliformatService {
    @GET("direct/content/site/{id}.json")
    fun resources(@Path("id") id: String): CompletableFuture<ContentEntity>

    @GET("direct/user/current.json")
    fun currentUser(): CompletableFuture<UserInfo>

    @GET("site.json")
    fun siteInfo(): CompletableFuture<SiteEntity>

    @GET
    fun downloadFile(@Url fileUrl: String): CompletableFuture<ResponseBody>
}