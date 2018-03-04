package utils

import domain.Resource
import java.util.LinkedList

class ResourceTree<T : Resource>(val data: T) {

    private val children: MutableList<ResourceTree<T>> = LinkedList()

    fun addChild(child: ResourceTree<T>): Boolean {
        return children.add(child)
    }

    fun removeChild(child: ResourceTree<T>): Boolean {
        return children.remove(child)
    }

    fun getChildren(): List<ResourceTree<T>> {
        return children
    }

    fun print() {
        print("", true)
    }

    private fun print(prefix: String, isTail: Boolean) {
        println(prefix + (if (isTail) "└── " else "├── ") + data.toString())

        for (i in 0 until children.size - 1) {
            children[i].print(prefix + if (isTail) "    " else "│   ", false)
        }

        if (children.size > 0) {
            children[children.size - 1].print(prefix + if (isTail) "    " else "│   ", true)
        }
    }

}
