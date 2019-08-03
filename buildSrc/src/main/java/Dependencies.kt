object Deps {
    const val jfoenix = Artifact.jfoenix + ":" + Version.jfoenix
    const val jsoup = Artifact.jsoup + ":" + Version.jsoup
    const val jspoon = Artifact.jspoon + ":" + Version.jspoon
    const val moshi = Artifact.moshi + ":" + Version.moshi
    const val moshi_kotlin = Artifact.moshi_kotlin + ":" + Version.moshi
    const val logback = Artifact.logback + ":" + Version.logback
    const val groovy = Artifact.groovy + ":" + Version.groovy
    const val kotlin_logging = Artifact.kotlin_logging + ":" + Version.kotlin_logging
    const val retrofit = Artifact.retrofit + ":" + Version.retrofit
    const val retrofit_moshi = Artifact.retrofit_moshi + ":" + Version.retrofit
    const val retrofit_jspoon = Artifact.retrofit_jspoon + ":" + Version.retrofit_jspoon
    const val system_tray = Artifact.system_tray + ":" + Version.system_tray
    const val kodein_generic_jvm = Artifact.kodein_generic_jvm + ":" + Version.kodein
    const val mockk = Artifact.mockk + ":" + Version.mockk
    const val kotlinx_coroutines_core = Artifact.kotlinx_coroutines_core + ":" + Version.kotlinx_coroutines
    const val kotlinx_coroutines_fx = Artifact.kotlinx_coroutines_fx + ":" + Version.kotlinx_coroutines
    const val junit = Artifact.junit + ":" + Version.junit
    const val okhttp_logging_interceptor = Artifact.okhttp_logging_interceptor + ":" + Version.okhttp
}

object Artifact {
    const val jfoenix = "com.jfoenix:jfoenix"
    const val jsoup = "org.jsoup:jsoup"
    const val jspoon = "pl.droidsonroids:jspoon"
    const val moshi = "com.squareup.moshi:moshi"
    const val moshi_kotlin = "com.squareup.moshi:moshi-kotlin"
    const val kotlin_logging = "io.github.microutils:kotlin-logging"
    const val logback = "ch.qos.logback:logback-classic"
    const val groovy = "org.codehaus.groovy:groovy"
    const val retrofit = "com.squareup.retrofit2:retrofit"
    const val retrofit_moshi = "com.squareup.retrofit2:converter-moshi"
    const val retrofit_jspoon = "pl.droidsonroids.retrofit2:converter-jspoon"
    const val system_tray = "com.dorkbox:SystemTray"
    const val kodein_generic_jvm = "org.kodein.di:kodein-di-generic-jvm"
    const val mockk = "io.mockk:mockk"
    const val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    const val kotlinx_coroutines_fx = "org.jetbrains.kotlinx:kotlinx-coroutines-javafx"
    const val junit = "org.junit.jupiter:junit-jupiter"
    const val okhttp_logging_interceptor = "com.squareup.okhttp3:logging-interceptor"
}

object Version {
    const val kotlin = "1.3.41"
    const val jfoenix = "8.0.9"
    const val jsoup = "1.12.1"
    const val jspoon = "1.3.2"
    const val moshi = "1.8.0"
    const val kotlin_logging = "1.7.2"
    const val logback = "1.2.3"
    const val groovy = "2.5.7"
    const val retrofit = "2.6.1"
    const val retrofit_jspoon = "1.3.2"
    const val system_tray = "3.17"
    const val kodein = "6.3.3"
    const val mockk = "1.9.3"
    const val kotlinx_coroutines = "1.3.0-RC"
    const val junit = "5.5.1"
    const val okhttp = "4.0.1"
}
