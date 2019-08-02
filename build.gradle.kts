import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", Version.kotlin))
    }
}

plugins {
    java
    kotlin("jvm") version Version.kotlin
    application
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(Deps.jfoenix)
    implementation(Deps.jsoup)
    implementation(Deps.jspoon)
    implementation(Deps.moshi)
    implementation(Deps.moshi_kotlin) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
    implementation(kotlin("stdlib-jdk8", Version.kotlin))
    implementation(kotlin("reflect", Version.kotlin))
    implementation(Deps.kotlinx_coroutines_core)
    implementation(Deps.kotlinx_coroutines_fx)
    implementation(Deps.kotlin_logging)
    implementation(Deps.logback)
    runtimeOnly(Deps.groovy) // Necesario para calcualar el directorio donde guardar el log

    implementation(Deps.retrofit)
    implementation(Deps.retrofit_moshi)
    implementation(Deps.retrofit_jspoon)

    implementation(Deps.system_tray)

    implementation(Deps.kodein_generic_jvm)
    testImplementation(Deps.spek_jvm)
    testRuntimeOnly(Deps.spek_junit_runner)
    testImplementation (Deps.mockito_kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
val appVersion = "1.0"
tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    test {
        doFirst {
            jvmArgs("-Ddni=${System.getProperty("dni")}", "-Dclau=${System.getProperty("clau")}")
            println(System.getProperty("dni"))
        }
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }

    application {
        mainClassName = "App"
        version = appVersion
    }
}

tasks.withType(KotlinCompile::class)
        .forEach {
            it.kotlinOptions { freeCompilerArgs = listOf("-Xnew-inference") }
        }

val fatJar by tasks.creating(Jar::class.java) {
    archiveBaseName.set(project.name)
    manifest {
        attributes(
                "Implementation-Title" to "Gradle Jar File",
                "Implementation-Version" to appVersion,
                "Main-Class" to application.mainClassName)
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    with(tasks.jar.get())
}
