package domain

import com.squareup.moshi.Json
import utils.ResourceTree
import java.nio.file.Paths
import java.util.*

class ContentEntity(
        @Json(name = "content_collection")
        val collection: Array<PoliformatFile> = arrayOf()
) : Entity() {

    init {
        val aux = HashMap<String, ResourceTree<PoliformatFile>>()
        var parent = ResourceTree(collection[0])
        parent.data.localPath = Paths.get(parent.data.title)
        aux[parent.data.url.toString()] = parent

        for (i in 1 until collection.size) {
            val current = ResourceTree(collection[i])
            parent = aux[current.data.parentUrl]!!
            val localPath = parent.data.localPath.resolve(current.data.title)
            current.data.localPath = localPath
            parent.addChild(current)
            aux[current.data.url.toString()] = current
        }
    }
}
