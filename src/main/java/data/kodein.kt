package data

import data.network.CookieJarImpl
import data.network.CredentialsHandler
import data.network.CredentialsStorage
import data.network.CredentialsStorageImpl
import data.network.PoliformatService
import data.network.UpvService
import mu.KLogging
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import syncPoliformat.ApplicationFiles
import java.util.concurrent.TimeUnit

object Data : KLogging() {
    val module = Kodein.Module("Data", false, "") {
        import(domain.module)
        bind() from singleton {
            OkHttpClient.Builder().apply {
                cookieJar(instance())
                readTimeout(15, TimeUnit.MINUTES)
                if (logger.isDebugEnabled) {
                    val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            logger.debug(message)
                        }
                    })
                    interceptor.level = HttpLoggingInterceptor.Level.BASIC
                    interceptor.redactHeader("X-Sakai-Session")
                    addInterceptor(interceptor)
                }
            }.build().apply { dispatcher.maxRequests = 20 }
        }
        bind("upv") from singleton {
            Retrofit.Builder()
                    .baseUrl("https://www.upv.es/")
                    .client(instance())
                    .addConverterFactory(JspoonConverterFactory.create())
                    .build()
        }
        bind("poliformat") from singleton {
            Retrofit.Builder()
                    .baseUrl("https://poliformat.upv.es/")
                    .client(instance())
                    .addConverterFactory(MoshiConverterFactory.create(instance()))
                    .build() }
        bind<UpvService>() with singleton { instance<Retrofit>("upv").create() }
        bind<PoliformatService>() with singleton { instance<Retrofit>("poliformat").create() }
        bind<Repository>() with singleton { DataRepository(instance(), instance()) }
        bind<CredentialsStorage>() with singleton { CredentialsStorageImpl(instance(ApplicationFiles.CredentialsFile)) }
        bind<CredentialsHandler>() with singleton { CookieJarImpl }
        bind<CookieJar>() with singleton { CookieJarImpl }
    }
}
