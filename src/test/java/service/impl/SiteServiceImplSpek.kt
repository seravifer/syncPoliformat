package service.impl

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import data.Repository
import domain.SubjectInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import service.SiteService
import utils.Utils
import java.io.File
import java.util.concurrent.CompletableFuture

object SiteServiceImplSpek : Spek({
    given("a SiteService") {
        val subjects = List(10) {
            SubjectInfo(shortName = "$it",
                    title = "Subject $it",
                    id = "GRA_${it}_${Utils.curso}") }
        val subjectNames = subjects.associate { it.id to "Real Name ${it.shortName.toInt()}" }
        val subjectsFile = File("testSubjects.json").apply { writeText("{}") }

        val repository = mock<Repository> {
            on { getSiteSubjects() } doReturn CompletableFuture.completedFuture(subjects)
            on { getSiteSubjectNames() } doReturn CompletableFuture.completedFuture(subjectNames)
        }

        val siteService: SiteService = SiteServiceImpl(repository, subjectsFile)
        on("getSubjects") {
            val correctSubjects = siteService.getSubjects().get()
            it ("should call the repository site methods") {
                verify(repository, times(1)).getSiteSubjectNames()
                verify(repository, times(1)).getSiteSubjects()
            }

            it ("should return a list of SubjectInfo the correct names") {
                assert(correctSubjects.all { it.name == subjectNames[it.id] })
            }

            it ("should create or update the last update date") {
                assert(subjectsFile.exists())
            }

            subjectsFile.delete()
        }
    }
})