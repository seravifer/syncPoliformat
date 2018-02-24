package utils

import javafx.concurrent.Task as JavaFxTask

class Task<T>(val task: Task<T>.() -> T) : JavaFxTask<T>() {

    private var onSucceeded: ((T) -> Unit)? = null
    private var onFailed: ((Throwable) -> Unit)? = null

    override fun call() = task()

    infix fun onSucceeded(block: (T) -> Unit) : Task<T> {
        onSucceeded = block
        return this
    }

    override fun succeeded() {
        super.succeeded()
        onSucceeded?.invoke(value)
    }

    infix fun onFailed(block: (Throwable) -> Unit): Task<T> {
        onFailed = block
        return this
    }

    override fun failed() {
        super.failed()
        onFailed?.invoke(exception)
    }

    fun toThread(name: String? = null, isDaemon: Boolean = true) = Thread(this).apply {
        this.isDaemon = isDaemon
        if (name != null) this.name = name
    }
}

fun <T> task(block: Task<T>.() -> T) = Task(block)
