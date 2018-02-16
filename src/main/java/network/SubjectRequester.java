package network;

import com.squareup.moshi.Moshi;
import network.adapter.ContentAdapter;
import network.adapter.UrlAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class SubjectRequester {
    private SubjectRequester() {}

    private static Moshi moshiParser = new Moshi.Builder()
            .add(new ContentAdapter())
            .add(new UrlAdapter())
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://poliformat.upv.es/direct/")
            .addConverterFactory(MoshiConverterFactory.create(moshiParser))
            .build();

    public static final PoliformatService POLIFORMAT_SERVICE = retrofit.create(PoliformatService.class);
}
