package domain

import com.squareup.moshi.Json
import domain.json.adapter.PosixDate

import java.util.Date

class SubjectInfo(
        @Json(name = "contactEmail")
        var contactEmail: String? = null,
        @Json(name = "contactName")
        var contactName: String? = null,
        @PosixDate
        @Json(name = "createdDate")
        var createdDate: Date? = null,
        @Json(name = "description")
        var description: String? = null,
        @Json(name = "htmlDescription")
        var htmlDescription: String? = null,
        @Json(name = "htmlShortDescription")
        var shortName: String? = null,
        @Json(name = "id")
        var id: String,
        @PosixDate
        @Json(name = "lastModified")
        var lastModified: Date? = null,
        @Json(name = "maintainRole")
        var maintainRole: String? = null,
        @PosixDate
        @Json(name = "modifiedDate")
        var modifiedDate: Date? = null,
        @Json(name = "owner")
        var owner: String? = null,
        @Json(name = "providerGroupId")
        var providerGroupId: String? = null,
        @Json(name = "reference")
        var reference: String? = null,
        @Json(name = "shortDescription")
        var shortDescription: String? = null,
        @Json(name = "title")
        var title: String,
        @Json(name = "type")
        var type: String? = null,
        @Json(name = "userRoles")
        var userRoles: List<String>? = null,
        @Json(name = "activeEdit")
        var activeEdit: Boolean? = null,
        @Json(name = "customPageOrdered")
        var customPageOrdered: Boolean? = null,
        @Json(name = "empty")
        var empty: Boolean? = null,
        @Json(name = "joinable")
        var joinable: Boolean? = null,
        @Json(name = "pubView")
        var pubView: Boolean? = null,
        @Json(name = "published")
        var published: Boolean? = null,
        @Json(name = "softlyDeleted")
        var softlyDeleted: Boolean? = null,
        @Json(name = "entityReference")
        var entityReference: String? = null,
        @Json(name = "entityURL")
        var entityURL: String? = null,
        @Json(name = "entityId")
        var entityId: String? = null,
        @Json(name = "entityTitle")
        var entityTitle: String? = null
) {

    @Transient
    var lastUpdate = ""
    @Transient
    var name = "non-fetched-name"

    val isRealSubject: Boolean = maintainRole == "profesor" && type == "siteupv"
}
