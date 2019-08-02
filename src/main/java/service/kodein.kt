package service

import fileModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import service.impl.AuthenticationServiceImpl
import service.impl.FileServiceImpl
import service.impl.SiteServiceImpl
import service.impl.SubjectServiceImpl

val module = Kodein.Module("Services", false, "") {
    import(fileModule)
    import(data.module)
    bind<AuthenticationService>() with provider { AuthenticationServiceImpl(instance(), instance(), instance(), instance(), instance()) }
    bind<FileService>() with provider { FileServiceImpl(instance(), instance(), instance("subjects")) }
    bind<SiteService>() with provider { SiteServiceImpl(instance(), instance("subjects")) }
    bind<SubjectService>() with provider { SubjectServiceImpl(instance()) }
}
