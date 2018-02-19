package utils

import model.PoliformatFile

import java.util.LinkedList

class Tree<T>(val data: T) {

    private val children: MutableList<Tree<T>>

    init {
        this.children = LinkedList()
    }

    fun addChild(child: Tree<T>): Boolean {
        return children.add(child)
    }

    fun removeChild(child: Tree<T>): Boolean {
        return children.remove(child)
    }

    fun getChildren(): List<Tree<T>> {
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

    fun merge(localTree: Tree<PoliformatFile>): List<PoliformatFile>? {
        return null
    }

}
