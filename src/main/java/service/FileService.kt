package service

import domain.SubjectInfo


interface FileService {
    suspend fun syncSubjectFiles(subjectInfo: SubjectInfo): Now
}

typealias Now = String
