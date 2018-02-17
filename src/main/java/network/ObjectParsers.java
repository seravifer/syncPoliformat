package network;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.PoliformatEntity;
import network.adapter.CleanAdapter;
import network.adapter.ContentAdapter;
import network.adapter.UrlAdapter;

public final class ObjectParsers {
    private ObjectParsers() {}

    private static Moshi moshiParser = new Moshi.Builder()
            .add(new ContentAdapter())
            .add(new UrlAdapter())
            .add(new CleanAdapter())
            .build();

    public static JsonAdapter<PoliformatEntity> ENTITY_PARSER = moshiParser.adapter(PoliformatEntity.class);
}
