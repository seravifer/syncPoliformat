package data.network

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import domain.json.adapter.CleanAdapter
import domain.json.adapter.ContentAdapter
import domain.json.adapter.PosixDateAdapter
import domain.json.adapter.UrlAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private val httpClient = OkHttpClient.Builder()
        .cookieJar(NonPersistentCookieJar)
        .build()

private val upvRetrofit = Retrofit.Builder()
        .baseUrl("https://www.upv.es/")
        .client(httpClient)
        .build()

private val moshiParser = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(ContentAdapter())
        .add(UrlAdapter())
        .add(CleanAdapter())
        .add(PosixDateAdapter())
        .build()

private val poliformatRetrofit = Retrofit.Builder()
        .baseUrl("https://poliformat.upv.es/")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshiParser))
        .addCallAdapterFactory(Java8CallAdapterFactory.create())
        .build()

object Intranet : UpvService by upvRetrofit.create(UpvService::class.java)

object Poliformat : PoliformatService by poliformatRetrofit.create(PoliformatService::class.java)