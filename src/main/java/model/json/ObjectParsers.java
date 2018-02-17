package model.json;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.PoliformatEntity;
import model.json.adapter.CleanAdapter;
import model.json.adapter.ContentAdapter;
import model.json.adapter.UrlAdapter;

public final class ObjectParsers {

    private ObjectParsers() {}

    private static Moshi moshiParser = new Moshi.Builder()
            .add(new ContentAdapter())
            .add(new UrlAdapter())
            .add(new CleanAdapter())
            .build();

    public static JsonAdapter<PoliformatEntity> ENTITY_PARSER = moshiParser.adapter(PoliformatEntity.class);

}
