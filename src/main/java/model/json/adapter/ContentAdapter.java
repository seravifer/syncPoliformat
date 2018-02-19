package model.json.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContentAdapter {

    private final CleanAdapter clean = new CleanAdapter();

    @ToJson String toJson(Path path) {
        return path.toString();
    }

    @FromJson
    Path fromJson(String path) {
        return Paths.get(clean.fromJson(path));
    }

}
