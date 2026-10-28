package com.aistudyos.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(

    @SerializedName("sessionId")
    val sessionId: Long,

    @SerializedName("answer")
    val answer: String,

    @SerializedName("sources")
    val sources: List<SourceDto>?,

    @SerializedName("timestamp")
    val timestamp: String
) {
    data class SourceDto(
        @SerializedName("chunkId") val chunkId: Long,
        @SerializedName("preview") val preview: String
    )
}