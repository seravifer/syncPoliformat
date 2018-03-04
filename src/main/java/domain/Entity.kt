package domain

import com.squareup.moshi.Json

abstract class Entity {
    @Json(name = "entityPrefix")
    var entityPrefix: String? = null
}
