package com.aistudyos.app.data.remote.dto.request
import com.google.gson.annotations.SerializedName

data class QuizSubmitRequest(
    @SerializedName("quizId")  val quizId: String,
    @SerializedName("answers") val answers: List<AnswerDto>
)

data class AnswerDto(
    @SerializedName("questionId")    val questionId: String,
    @SerializedName("selectedOption") val selectedOption: Int
)
