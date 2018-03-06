package data.network

import utils.Settings
import java.io.File

data class Credentials(val token: String, val dns: String) {
    companion object {
        val credentialsFile: File
            get() = Settings.appDirectory.resolve("credentials")
    }
}