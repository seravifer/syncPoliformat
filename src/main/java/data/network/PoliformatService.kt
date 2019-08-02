package data.network

import domain.ContentEntity
import domain.SiteEntity
import domain.UserInfo
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PoliformatService {
    @GET("direct/content/site/{id}.json")
    suspend fun resources(@Path("id") id: String): ContentEntity

    @GET("direct/user/current.json")
    suspend fun currentUser(): UserInfo

    @GET("direct/site.json")
    suspend fun siteInfo(): SiteEntity

    @GET("portal/login")
    suspend fun login(): ResponseBody

    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody
}
