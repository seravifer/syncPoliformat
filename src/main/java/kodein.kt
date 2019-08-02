import domain.SubjectInfo
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import utils.OS
import java.io.File
import java.nio.file.Paths

val fileModule = Kodein.Module("Files", false, "") {
    bind<OS>() with singleton { OS.host() }
    bind() from factory { subjectInfo: SubjectInfo ->
        instance<File>("app").resolve("${subjectInfo.id}.json")
    }
    bind("userHome") from provider { File(System.getProperty("user.home")) }
    bind("app") from provider {
        when(instance<OS>()) {
            OS.WINDOWS -> Paths.get(System.getenv("APPDATA")).toFile()
            else -> instance<File>("userHome").resolve(".local").resolve(".share")
        }.resolve("syncPoliformat")
    }
    bind("poliformat") from provider { instance<File>("userHome").resolve("Poliformat") }
    bind("subjects") from provider { instance<File>("app").resolve("subjects.json") }
    bind("credentials") from provider { instance<File>("app").resolve("credentials") }
}

val appComponent = Kodein {
    import(controller.module)
}
