package service

import domain.ContentEntity

interface SubjectService {
    suspend fun subjectContentResources(id: String): ContentEntity
}
