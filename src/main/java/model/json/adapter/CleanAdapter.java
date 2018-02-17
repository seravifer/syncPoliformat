package model.json.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class CleanAdapter {
    public @ToJson String toJson(@Clean String data) {
        return data;
    }

    public @FromJson @Clean String fromJson(String json) {
        return json.replaceAll("[\\\\/:*?\"<>|]", "").replaceAll(" +$", "");
    }
}
