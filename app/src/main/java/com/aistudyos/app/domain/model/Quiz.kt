package com.aistudyos.app.domain.model

data class Quiz(
    val id: String,
    val materialId: String?,
    val title: String,
    val questions: List<Question>
)

data class Question(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctOption: Int,
    val explanation: String?
)

data class QuizResult(
    val quizId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Float,
    val weakTopics: List<String>,
    val answers: List<AnswerResult>
)

data class AnswerResult(
    val questionId: String,
    val selectedOption: Int,
    val correctOption: Int,
    val isCorrect: Boolean
)
