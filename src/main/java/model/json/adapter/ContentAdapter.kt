package model.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.nio.file.Path
import java.nio.file.Paths

class ContentAdapter {

    private val clean = CleanAdapter()

    @ToJson
    internal fun toJson(path: Path): String {
        return path.toString()
    }

    @FromJson
    internal fun fromJson(path: String): Path {
        return Paths.get(clean.fromJson(path))
    }

}
