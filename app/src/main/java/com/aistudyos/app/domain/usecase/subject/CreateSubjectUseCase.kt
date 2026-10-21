package com.aistudyos.app.domain.usecase.subject

import com.aistudyos.app.core.common.models.AppException
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.Subject
import com.aistudyos.app.domain.repository.SubjectRepository
import javax.inject.Inject

class CreateSubjectUseCase @Inject constructor(private val repo: SubjectRepository) {
    suspend operator fun invoke(name: String, description: String?, color: String?): Result<Subject> {
        if (name.isBlank()) return Result.Error(AppException.ValidationException("Subject name is required."))
        return repo.createSubject(name.trim(), description?.trim(), color)
    }
}
