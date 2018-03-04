package domain

import java.nio.file.Path

interface Resource {
    val name: String
    var localPath: Path
}