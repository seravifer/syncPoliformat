package utils

import javafx.application.Platform
import java.util.concurrent.Executor

object JavaFXExecutor : Executor {
    override fun execute(command: Runnable?) {
        Platform.runLater(command)
    }
}