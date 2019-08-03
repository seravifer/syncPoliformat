package service.impl

import data.Repository
import domain.SubjectInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance
import service.SiteService
import syncPoliformat.appComponent
import utils.Utils
import java.io.File

class SiteServiceImplTest {
    val subjects = List(10) {
        SubjectInfo(shortName = "$it",
                title = "Subject $it",
                id = "GRA_${it}_${Utils.curso}") }
    val subjectNames = subjects.associate { it.id to "Real Name ${it.shortName.toInt()}" }
    val subjectsFile = File("testSubjects.json")

    @BeforeEach
    fun prepare() {
        subjectsFile.writeText("{}")
    }

    @Test
    fun `should call the repository`() {
        val repository = mockk<Repository> {
            coEvery { getSiteSubjects() } returns subjects
            coEvery { getSiteSubjectNames() } returns subjectNames
        }
        coVerify(exactly = 1) { repository.getSiteSubjectNames() }
        coVerify(exactly = 1) { repository.getSiteSubjects() }
    }

    @Test
    fun `should return a list of SubjectInfo the correct names`() {
        val repository = mockk<Repository> {
            coEvery { getSiteSubjects() } returns subjects
            coEvery { getSiteSubjectNames() } returns subjectNames
        }
        val siteService: SiteService = SiteServiceImpl(repository, subjectsFile, appComponent.direct.instance())
        val correctSubjects = runBlocking { siteService.getSubjects() }
        assert(correctSubjects.all { it.name == subjectNames[it.id] })
    }

    @AfterEach
    fun clean() {
        subjectsFile.delete()
    }
}
