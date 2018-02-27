package service

import domain.SubjectInfo
import java.util.concurrent.Future

interface SubjectService {
    fun subjectInfo(id: String): Future<List<SubjectInfo>>
}