package network;

import model.PoliformatEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PoliformatService {
    @GET("content/site/{json}")
    Call<PoliformatEntity> getSubject(@Path("json") String json);
}
