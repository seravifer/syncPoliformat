package domain.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ShortNameAdapter {

    @ToJson
    fun toJson(@ShortName data: String): String = data

    @FromJson
    @ShortName
    fun fromJson(json: String): String {
        return json.toUpperCase().substring(0, 3)
    }

}