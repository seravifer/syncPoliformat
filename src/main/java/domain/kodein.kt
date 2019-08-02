package domain

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import domain.json.adapter.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val module = Kodein.Module("Domain", false, "") {
    bind<Moshi>() with singleton { Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(ContentAdapter())
            .add(UrlAdapter())
            .add(CleanAdapter())
            .add(PosixDateAdapter())
            .add(ShortNameAdapter())
            .build() }
    bind<JsonAdapter<ContentEntity>>() with singleton { instance<Moshi>().adapter(ContentEntity::class.java) }
    bind<JsonAdapter<Map<String, String>>>() with singleton { instance<Moshi>().adapter(
            Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)) }
}
