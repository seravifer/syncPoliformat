package service

import data.Data
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import service.impl.AuthenticationServiceImpl
import service.impl.FileServiceImpl
import service.impl.SiteServiceImpl
import service.impl.SubjectServiceImpl
import syncPoliformat.ApplicationFiles

val module = Kodein.Module("Services", false, "") {
    import(Data.module)
    bind<AuthenticationService>() with provider { AuthenticationServiceImpl(instance(), instance(), instance(), instance(), instance()) }
    bind<FileService>() with provider {
        FileServiceImpl(
                instance(), instance(),
                instance(ApplicationFiles.SubjectsFile), instance(ApplicationFiles.PoliformatFolder), factory(),
                instance(), instance())
    }
    bind<SiteService>() with provider {
        SiteServiceImpl(instance(), instance(ApplicationFiles.SubjectsFile), instance())
    }
    bind<SubjectService>() with provider { SubjectServiceImpl(instance()) }
}
