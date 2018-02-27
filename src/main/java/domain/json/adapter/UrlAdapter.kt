package domain.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.net.URL

class UrlAdapter {

    @ToJson
    internal fun toJson(url: URL): String = url.toString()


    @FromJson
    internal fun fromJson(json: String): URL {
        return URL(json)
    }

}
