package utils

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

    fun merge(localTree: Tree<T>): List<T> {
        val newFiles = LinkedList<T>()
        val dequeOld = LinkedList<Tree<T>>()
        val dequeNew = LinkedList<Tree<T>>()

        dequeOld.add(this)
        dequeNew.add(localTree)

        var ptrA: Tree<T>
        var ptrB: Tree<T>

        while (dequeOld.size != 0) {
            ptrA = dequeOld.first
            ptrB = dequeNew.first

            if (ptrA.data == ptrB.data) {
                dequeOld.removeFirst()
                dequeNew.removeFirst()

                for (child in ptrA.getChildren()) {
                    dequeOld.addFirst(child)
                }

                for (child in ptrB.getChildren()) {
                    dequeNew.addFirst(child)
                }

            } else {
                val file = dequeNew.removeFirst()
                newFiles.add(file.data)

                for (child in ptrB.getChildren()) {
                    dequeNew.addFirst(child)
                }
            }
        }

        while (dequeNew.size != 0) {
            ptrB = dequeNew.removeFirst()

            for (child in ptrB.getChildren()) {
                dequeNew.addFirst(child)
            }

            newFiles.add(ptrB.data)
        }

        return newFiles
    }

}
