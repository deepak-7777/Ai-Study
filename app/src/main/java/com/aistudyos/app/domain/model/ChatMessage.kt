package com.aistudyos.app.domain.model

data class ChatMessage(
    val id: String,
    val role: String,   // "user" | "assistant"
    val content: String,
    val sources: List<String>,
    val timestamp: String
) {
    val isUser get() = role == "user"
}
