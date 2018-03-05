package domain.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import domain.ContentEntity
import domain.SiteEntity
import domain.UserInfo
import domain.json.adapter.*

private val moshiParser = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(ContentAdapter())
        .add(UrlAdapter())
        .add(CleanAdapter())
        .add(PosixDateAdapter())
        .add(ShortNameAdapter())
        .build()

val ContentEntityAdapter = moshiParser.adapter(ContentEntity::class.java)
val SiteEntityAdapter = moshiParser.adapter(SiteEntity::class.java)
val UserInfoAdapter: JsonAdapter<UserInfo> = moshiParser.adapter(UserInfo::class.java)
val LastSubjectUpdateAdapter = moshiParser.adapter<Map<String, String>>(
        Types.newParameterizedType(Map::class.java, String::class.java, String::class.java))
