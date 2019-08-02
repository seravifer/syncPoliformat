package data

import data.network.*
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import utils.JavaFXExecutor
import java.util.concurrent.TimeUnit

val module = Kodein.Module("Data", false, "") {
    import(domain.module)
    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
                .cookieJar(instance())
                .readTimeout(15, TimeUnit.MINUTES)
                .build().apply { dispatcher().maxRequests = 20 }
    }
    bind<Retrofit>("upv") with singleton { Retrofit.Builder()
            .baseUrl("https://www.upv.es/")
            .client(instance())
            .addConverterFactory(JspoonConverterFactory.create())
            .callbackExecutor(JavaFXExecutor)
            .build()
    }
    bind<Retrofit>("poliformat") with singleton { Retrofit.Builder()
            .baseUrl("https://poliformat.upv.es/")
            .client(instance())
            .addConverterFactory(MoshiConverterFactory.create(instance()))
            .callbackExecutor(JavaFXExecutor)
            .build() }
    bind<UpvService>() with singleton { instance<Retrofit>("upv").create() }
    bind<PoliformatService>() with singleton { instance<Retrofit>("poliformat").create() }
    bind<Repository>() with singleton { DataRepository(instance(), instance()) }
    bind<CredentialsStorage>() with singleton { CredentialsStorageImpl(instance("credentials")) }
    bind<CredentialsHandler>() with singleton { CookieJarImpl }
    bind<CookieJar>() with singleton { CookieJarImpl }
}
