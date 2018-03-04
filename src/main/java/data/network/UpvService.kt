package data.network

import data.network.scrapping.SubjectsList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.concurrent.CompletableFuture

interface UpvService {
    @FormUrlEncoded
    @POST("exp/aute_intranet")
    @Headers(value = ["Content-Type: application/x-www-form-urlencoded"])
    fun login(@FieldMap params: Map<String, String>) : Call<ResponseBody>

    @GET("pls/soalu/sic_asi.Lista_asig")
    fun subjects(): CompletableFuture<SubjectsList>
}