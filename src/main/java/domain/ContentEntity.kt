package domain

import com.squareup.moshi.Json
import utils.Tree

import java.util.HashMap

class ContentEntity(
        @Json(name = "content_collection")
        val collection: Array<PoliformatFile> = arrayOf()
) : Entity() {

    fun toFileTree(): Tree<PoliformatFile> {
        val aux = HashMap<String, Tree<PoliformatFile>>()
        var parent = Tree(collection[0])
        val root = parent
        aux[parent.data.url.toString()] = parent

        for (i in 1 until collection.size) {
            val current = Tree(collection[i])
            parent = aux[current.data.parentUrl]!!
            parent.addChild(current)
            aux[current.data.url.toString()] = current
        }

        return root
    }

}
