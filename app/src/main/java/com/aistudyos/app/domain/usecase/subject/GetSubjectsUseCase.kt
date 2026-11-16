package com.aistudyos.app.domain.usecase.subject

import com.aistudyos.app.domain.model.Subject
import com.aistudyos.app.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubjectsUseCase @Inject constructor(private val repo: SubjectRepository) {
    operator fun invoke(): Flow<List<Subject>> = repo.observeSubjects()
}
