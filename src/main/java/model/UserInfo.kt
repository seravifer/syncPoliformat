package model

import com.squareup.moshi.Json
import model.json.adapter.PosixDate

import java.net.URL
import java.util.Date

class UserInfo(
        @PosixDate
        @Json(name = "createdDate")
        val createdDate: Date? = null,
        @Json(name = "displayId")
        val displayId: String? = null,
        @Json(name = "displayName")
        val displayName: String? = null,
        @Json(name = "eid")
        val eid: String? = null,
        @Json(name = "email")
        val email: String? = null,
        @Json(name = "firstName")
        val firstName: String? = null,
        @Json(name = "id")
        val id: String? = null,
        @PosixDate
        @Json(name = "lastModified")
        val lastModified: Date? = null,
        @Json(name = "lastName")
        val lastName: String? = null,
        @PosixDate
        @Json(name = "modifiedDate")
        val modifiedDate: Date? = null,
        @Json(name = "owner")
        val owner: String? = null,
        @Json(name = "reference")
        val reference: String? = null,
        @Json(name = "sortName")
        val sortName: String? = null,
        @Json(name = "type")
        val type: String? = null,
        @Json(name = "url")
        val url: URL? = null,
        @Json(name = "entityReference")
        val entityReference: String? = null,
        @Json(name = "entityURL")
        val entityURL: URL? = null,
        @Json(name = "entityId")
        val entityId: String? = null,
        @Json(name = "entityTitle")
        val entityTitle: String? = null
)