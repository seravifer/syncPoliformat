package data.network

import domain.ContentEntity
import domain.SiteEntity
import domain.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PoliformatService {
    @GET("direct/content/site/{id}.json")
    fun resources(@Path("id") id: String): Call<ContentEntity>

    @GET("direct/user/current.json")
    fun currentUser(): Call<UserInfo>

    @GET("site.json")
    fun siteInfo(): Call<SiteEntity>
}