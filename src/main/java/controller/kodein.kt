package controller

import domain.SubjectInfo
import domain.UserInfo
import org.kodein.di.Kodein
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.factory2
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import syncPoliformat.App
import syncPoliformat.ApplicationFiles
import java.util.WeakHashMap

object AppScope : Scope<App> {
    private val registryMap = WeakHashMap<App, ScopeRegistry>()
    override fun getRegistry(context: App): ScopeRegistry =
            registryMap.computeIfAbsent(context) { StandardScopeRegistry() }
}

val module = Kodein.Module("Controllers", false, "") {
    import(service.module)
    bind() from scoped(AppScope).multiton { navigationHandler: NavigationHandler ->
        LoginController(instance(), context.stage, navigationHandler)
    }
    bind() from scoped(AppScope).multiton { user: UserInfo, navigationHandler: NavigationHandler ->
        HomeController(instance(), instance(), context.stage, user, factory(), navigationHandler, instance(ApplicationFiles.PoliformatFolder)) }
    bind() from scoped(AppScope).singleton { NavigationHandler(instance(), context, factory(), factory2()) }
    bind() from factory { subjectInfo: SubjectInfo -> SubjectComponent(subjectInfo, instance()) }
}
