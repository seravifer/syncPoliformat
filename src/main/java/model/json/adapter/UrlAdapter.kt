package model.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.net.MalformedURLException
import java.net.URL

class UrlAdapter {

    @ToJson
    internal fun toJson(url: URL): String {
        return url.toString()
    }

    @FromJson
    internal fun fromJson(json: String): URL? {
        try {
            return URL(json)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        }

    }

}
