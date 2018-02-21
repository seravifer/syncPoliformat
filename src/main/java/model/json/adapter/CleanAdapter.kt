package model.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class CleanAdapter {

    @ToJson
    fun toJson(@Clean data: String): String = data

    @FromJson
    @Clean
    fun fromJson(json: String): String {
        return json.replace("[\\\\/:*?\"<>|]".toRegex(), "")
                .replace(" +$".toRegex(), "")
    }

}
