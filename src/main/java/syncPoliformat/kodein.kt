package syncPoliformat

import org.kodein.di.Kodein

val appComponent by Kodein.lazy {
    import(fileModule, allowOverride = true)
    import(controller.module)
}
