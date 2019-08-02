package service.impl

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyBlocking
import data.Repository
import domain.SubjectInfo
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import service.SiteService
import utils.Utils
import java.io.File

object SiteServiceImplSpek : Spek({
    describe("a SiteService") {
        val subjects = List(10) {
            SubjectInfo(shortName = "$it",
                    title = "Subject $it",
                    id = "GRA_${it}_${Utils.curso}") }
        val subjectNames = subjects.associate { it.id to "Real Name ${it.shortName.toInt()}" }
        val subjectsFile = File("testSubjects.json").apply { writeText("{}") }

        val repository = mock<Repository> {
            onBlocking { getSiteSubjects() } doReturn subjects
            onBlocking { getSiteSubjectNames() } doReturn subjectNames
        }

        val siteService: SiteService = SiteServiceImpl(repository, subjectsFile)
        describe("getSubjects") {
            val correctSubjects = runBlocking { siteService.getSubjects() }
            it ("should call the repository site methods") {
                verifyBlocking(repository, times(1)) { getSiteSubjectNames() }
                verifyBlocking(repository, times(1)) { getSiteSubjects() }
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
