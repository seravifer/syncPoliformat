package data.network

import data.network.scrapping.SubjectsList
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UpvService {
    @FormUrlEncoded
    @POST("exp/aute_intranet")
    @Headers(value = ["Content-Type: application/x-www-form-urlencoded"])
    suspend fun login(@Field("dni") dni: String, @Field("clau") password: String,
              @FieldMap predefinedParams: Map<String, String> = mapOf(
                      "cua" to "sakai",
                      "estilo" to "500",
                      "id" to "c",
                      "vista" to "MSE")
    ) : Response<ResponseBody>

    @GET("pls/soalu/sic_asi.Lista_asig")
    suspend fun subjects(): SubjectsList
}
