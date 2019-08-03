package syncPoliformat

import domain.SubjectInfo
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import utils.OS
import java.io.File
import java.nio.file.Paths

sealed class ApplicationFiles {
    object UserHome : ApplicationFiles()
    object ConfigFolder : ApplicationFiles()
    object PoliformatFolder : ApplicationFiles()
    object SubjectsFile : ApplicationFiles()
    object CredentialsFile : ApplicationFiles()
}

val fileModule = Kodein.Module("Files", false, "") {
    bind() from singleton { OS.host() }
    bind(ApplicationFiles.UserHome) from provider { File(System.getProperty("user.home")) }
    bind(ApplicationFiles.ConfigFolder) from provider {
        when (instance<OS>()) {
            OS.WINDOWS -> Paths.get(System.getenv("APPDATA")).toFile()
            else -> instance<File>(ApplicationFiles.UserHome).resolve(".local").resolve(".share")
        }.resolve("syncPoliformat")
    }
    bind() from factory { subjectInfo: SubjectInfo ->
        instance<File>(ApplicationFiles.ConfigFolder).resolve("${subjectInfo.id}.json")
    }
    bind(ApplicationFiles.PoliformatFolder) from provider { instance<File>(ApplicationFiles.UserHome).resolve("Poliformat") }
    bind(ApplicationFiles.SubjectsFile) from provider { instance<File>(ApplicationFiles.ConfigFolder).resolve("subjects.json") }
    bind(ApplicationFiles.CredentialsFile) from provider { instance<File>(ApplicationFiles.ConfigFolder).resolve("credentials") }
}
