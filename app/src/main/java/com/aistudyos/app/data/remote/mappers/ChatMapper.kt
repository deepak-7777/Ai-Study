package com.aistudyos.app.data.remote.mappers

import com.aistudyos.app.data.remote.dto.response.ChatMessageDto
import com.aistudyos.app.domain.model.ChatMessage

fun ChatMessageDto.toDomain(): ChatMessage {

    return ChatMessage(
        id = sessionId.toString(),
        role = "assistant",
        content = answer ?: "",
        sources = sources?.map { it.preview } ?: emptyList(),
        timestamp = timestamp
    )
}