import model.ContentEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PoliformatService {
    @GET("direct/content/site/{id}.json")
    fun resources(@Path("id") id: String): Call<ContentEntity>
}