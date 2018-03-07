import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import controller.HomeController
import controller.LoginController
import controller.SubjectComponent
import data.DataRepository
import data.Repository
import data.network.*
import domain.SubjectInfo
import domain.UserInfo
import javafx.stage.Stage
import okhttp3.CookieJar
import service.AuthenticationService
import service.FileService
import service.SiteService
import service.SubjectService
import service.impl.AuthenticationServiceImpl
import service.impl.FileServiceImpl
import service.impl.SiteServiceImpl
import service.impl.SubjectServiceImpl
import utils.OS
import java.io.File
import java.nio.file.Paths

val appModule = ConfigurableKodein().apply {
    addConfig {
        import(dataModule)
        import(fileModule)
        import(serviceModule)
        bind<OS>() with singleton { OS.host() }
    }
}

val fileModule = Kodein.Module {
    bind("appFolder") from provider {
        when(instance<OS>()) {
            OS.WINDOWS -> Paths.get(System.getenv("APPDATA"))
            else -> Paths.get(System.getProperty("user.home"), ".local", "share")
        }.resolve("syncPoliformat").toFile()
    }
    bind("subjects") from provider { instance<File>("appFolder").resolve("subjects.json") }
}

val dataModule = Kodein.Module {
    bind<UpvService>() with singleton { Intranet }
    bind<PoliformatService>() with singleton { Poliformat }
    bind<Repository>() with singleton { DataRepository(instance(), instance()) }
    bind<CredentialsStorage>() with singleton { CredentialsStorageImpl }
    bind<CredentialsHandler>() with singleton { CookieJarImpl }
    bind<CookieJar>() with singleton { CookieJarImpl }
}

val serviceModule = Kodein.Module {
    bind<AuthenticationService>() with provider { AuthenticationServiceImpl(instance(), instance(), instance(), instance(), instance()) }
    bind<FileService>() with provider { FileServiceImpl(instance(), instance(), instance("subjects")) }
    bind<SiteService>() with provider { SiteServiceImpl(instance(), instance("subjects")) }
    bind<SubjectService>() with provider { SubjectServiceImpl(instance()) }
}

fun controllerModule(stage: Stage) = Kodein.Module {
    bind() from provider { LoginController(instance(), stage) }
    bind() from factory { user: UserInfo -> HomeController(instance(), instance(), stage, user) }
    bind() from factory { subjectInfo: SubjectInfo -> SubjectComponent(subjectInfo, instance()) }
}