package service

import domain.SubjectInfo
import java.util.concurrent.Future

interface SiteService {
    fun getSubjects(): Future<List<SubjectInfo>>
}