package com.aistudyos.app.domain.usecase.quiz

import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.Quiz
import com.aistudyos.app.domain.repository.QuizRepository
import javax.inject.Inject

class GenerateQuizUseCase @Inject constructor(private val repo: QuizRepository) {
    suspend operator fun invoke(materialId: String): Result<Quiz> = repo.generateQuiz(materialId)
}
