package network.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContentAdapter {
    @ToJson String toJson(Path path) {
        return path.toString();
    }

    @FromJson
    Path fromJson(String path) {
        return Paths.get(path);
    }
}
