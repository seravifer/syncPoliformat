package model

import com.squareup.moshi.Json
import model.json.adapter.Clean

import java.net.URL
import java.nio.file.Path

class PoliformatFile(
        @Json(name = "author")
        val author: String? = null,
        @Json(name = "authorId")
        val authorId: String? = null,
        @Json(name = "container")
        val container: Path? = null,
        @Json(name = "copyrightAlert")
        val copyrightAlert: String? = null,
        @Json(name = "description")
        val description: String? = null,
        @Json(name = "endDate")
        val endDate: String? = null,
        @Json(name = "fromDate")
        val fromDate: String? = null,
        @Json(name = "modifiedDate")
        val modifiedDate: String? = null,
        @Json(name = "numChildren")
        val numChildren: Int? = null,
        @Json(name = "quota")
        val quota: String? = null,
        @Json(name = "size")
        val size: Int? = null,
        @Clean
        @Json(name = "title")
        val title: String? = null,
        @Json(name = "type")
        val type: String? = null,
        @Json(name = "url")
        val url: URL,
        @Json(name = "usage")
        val usage: String? = null,
        @Json(name = "hidden")
        val hidden: Boolean? = null,
        @Json(name = "visible")
        val visible: Boolean? = null,
        @Json(name = "entityReference")
        val entityReference: String? = null,
        @Json(name = "entityURL")
        val entityURL: String? = null,
        @Json(name = "entityTitle")
        val entityTitle: String? = null
) {
    val parentUrl: String by lazy {
        val urlString = url.toString()

        val index = if (isFolder) {
            urlString.substring(0, urlString.length - 1).lastIndexOf('/')
        } else {
            urlString.lastIndexOf('/')
        }

        url.toString().substring(0, index + 1)
    }

    val isFolder = type == "collection"

    override fun equals(other: Any?): Boolean = other is PoliformatFile && this.title == other.title

    override fun toString() = title + " - " + url
}

