package data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UpvService {
    @FormUrlEncoded
    @POST("exp/aute_intranet")
    @Headers(value = ["Content-Type: application/x-www-form-urlencoded"])
    fun login(@FieldMap params: Map<String, String>) : Call<ResponseBody>
}