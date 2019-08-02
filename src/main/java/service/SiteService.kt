package service

import domain.SubjectInfo

interface SiteService {
    suspend fun getSubjects(): List<SubjectInfo>
}
