package controller

import domain.SubjectInfo
import domain.UserInfo
import javafx.stage.Stage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val module = Kodein.Module("Controllers", false, "") {
    import(service.module)
    bind() from factory { stage: Stage -> LoginController(instance(), stage) }
    bind() from factory { stage: Stage, user: UserInfo ->
        HomeController(instance(), instance(), stage, user, factory(), factory(), instance(), instance("poliformat")) }
    bind() from factory { subjectInfo: SubjectInfo -> SubjectComponent(subjectInfo, instance()) }
}
