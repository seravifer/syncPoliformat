package service

import domain.SubjectInfo
import java.util.concurrent.CompletableFuture

interface SiteService {
    fun getSubjects(): CompletableFuture<List<SubjectInfo>>
}