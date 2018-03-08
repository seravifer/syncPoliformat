import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import controller.HomeController
import controller.LoginController
import controller.SubjectComponent
import data.DataRepository
import data.Repository
import data.network.*
import domain.ContentEntity
import domain.SubjectInfo
import domain.UserInfo
import domain.json.adapter.*
import javafx.stage.Stage
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import service.AuthenticationService
import service.FileService
import service.SiteService
import service.SubjectService
import service.impl.AuthenticationServiceImpl
import service.impl.FileServiceImpl
import service.impl.SiteServiceImpl
import service.impl.SubjectServiceImpl
import utils.JavaFXExecutor
import utils.OS
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

val appModule = ConfigurableKodein().apply {
    addConfig {
        import(dataModule)
        extend(fileModule)
        import(serviceModule)
    }
}

val fileModule = Kodein {
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

val dataModule = Kodein.Module {
    import(domainModule)
    bind<OkHttpClient>() with singleton { OkHttpClient.Builder().cookieJar(instance()).readTimeout(15, TimeUnit.MINUTES).build() }
    bind<Retrofit>("upv") with singleton { Retrofit.Builder()
            .baseUrl("https://www.upv.es/")
            .client(instance())
            .addConverterFactory(JspoonConverterFactory.create())
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .callbackExecutor(JavaFXExecutor)
            .build()
    }
    bind<Retrofit>("poliformat") with singleton { Retrofit.Builder()
            .baseUrl("https://poliformat.upv.es/")
            .client(instance())
            .addConverterFactory(MoshiConverterFactory.create(instance()))
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .callbackExecutor(JavaFXExecutor)
            .build() }
    bind<UpvService>() with singleton { instance<Retrofit>("upv").create(UpvService::class.java) }
    bind<PoliformatService>() with singleton { instance<Retrofit>("poliformat").create(PoliformatService::class.java) }
    bind<Repository>() with singleton { DataRepository(instance(), instance()) }
    bind<CredentialsStorage>() with singleton { CredentialsStorageImpl }
    bind<CredentialsHandler>() with singleton { CookieJarImpl }
    bind<CookieJar>() with singleton { CookieJarImpl }
}

val domainModule = Kodein.Module {
    bind<Moshi>() with singleton { Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(ContentAdapter())
            .add(UrlAdapter())
            .add(CleanAdapter())
            .add(PosixDateAdapter())
            .add(ShortNameAdapter())
            .build() }
    bind<JsonAdapter<ContentEntity>>() with singleton { instance<Moshi>().adapter(ContentEntity::class.java) }
    bind<JsonAdapter<Map<String, String>>>() with singleton { instance<Moshi>().adapter<Map<String, String>>(
            Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)) }
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

inline fun <reified A, reified T : Any> Kodein.build(arg: A): T = factory<A, T>()(arg)