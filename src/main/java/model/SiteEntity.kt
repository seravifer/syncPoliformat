package model

import com.squareup.moshi.Json

class SiteEntity(
        @Json(name = "site_collection")
        val siteCollection: Array<SubjectInfo>? = null
) : Entity()
