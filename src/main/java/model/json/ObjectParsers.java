package model.json;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import model.*;
import model.json.adapter.*;

import java.util.Map;

public final class ObjectParsers {

    private ObjectParsers() {}

    private static final Moshi moshiParser = new Moshi.Builder()
            .add(new ContentAdapter())
            .add(new UrlAdapter())
            .add(new CleanAdapter())
            .add(new PosixDateAdapter())
            //.add(new SubjectManagerDumbAdapter())
            .build();

    public static final JsonAdapter<PoliformatContentEntity> POLIFORMAT_ENTITY_FILES_ADAPTER =
            moshiParser.adapter(PoliformatContentEntity.class);

    public static final JsonAdapter<PoliformatSiteEntity> POLIFORMAT_ENTITY_SUBJECT_ADAPTER =
            moshiParser.adapter(PoliformatSiteEntity.class);

    public static final JsonAdapter<UserInfo> USER_INFO_ADAPTER = moshiParser.adapter(UserInfo.class);

    public static final JsonAdapter<Map<String, String>> LAST_SUBJECT_UPDATE_ADAPTER =
            moshiParser.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

}