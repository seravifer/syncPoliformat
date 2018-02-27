package service

import domain.PoliformatFile
import java.util.concurrent.Future

interface FileService {
    fun downloadFile(file: PoliformatFile): Future<Unit>
}