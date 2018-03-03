package utils

import java.nio.file.Paths
import java.util.LinkedList

class Tree<T>(val data: T) {

    private val children: MutableList<Tree<T>> = LinkedList()

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

    fun merge(localTree: Tree<T>): List<Pair<T, String>> {
        val rootPath = localTree.data.toString()
        val newFiles = LinkedList<Pair<T, String>>()
        val dequeOld = LinkedList<Tree<T>>()
        val dequeNew = LinkedList<Pair<Tree<T>, String>>()

        dequeOld.add(this)
        dequeNew.add(localTree to rootPath)

        var ptrA: Tree<T>
        var ptrB: Pair<Tree<T>, String>

        while (dequeOld.size != 0) {
            ptrA = dequeOld.first
            ptrB = dequeNew.first

            if (ptrA.data == ptrB.first.data) {
                dequeOld.removeFirst()
                dequeNew.removeFirst()

                for (child in ptrA.getChildren()) {
                    dequeOld.addFirst(child)
                }

                for (child in ptrB.first.getChildren()) {
                    val path = Paths.get(ptrB.second, child.data.toString()).toString()
                    dequeNew.addFirst(child to path)
                }

            } else {
                val file = dequeNew.removeFirst()
                newFiles.add(file.first.data to file.second)

                for (child in ptrB.first.getChildren()) {
                    val path = Paths.get(ptrB.second, child.data.toString()).toString()
                    dequeNew.addFirst(child to path)
                }
            }
        }

        while (dequeNew.size != 0) {
            ptrB = dequeNew.removeFirst()

            for (child in ptrB.first.getChildren()) {
                val path = Paths.get(ptrB.second, child.data.toString()).toString()
                dequeNew.addFirst(child to path)
            }

            newFiles.add(ptrB.first.data to ptrB.second)
        }

        return newFiles
    }

}
