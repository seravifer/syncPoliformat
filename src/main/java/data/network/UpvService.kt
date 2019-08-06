package data.network

import data.network.scrapping.SubjectsList
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

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
    ) : Response<Unit>

    @GET("pls/soalu/sic_asi.Lista_asig")
    suspend fun subjects(): SubjectsList
}
