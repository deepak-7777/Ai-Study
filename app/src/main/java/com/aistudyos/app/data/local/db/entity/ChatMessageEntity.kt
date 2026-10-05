package com.aistudyos.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(

    @PrimaryKey
    val id: String,   // sessionId string me convert karenge

    val role: String, // "user" / "assistant"

    val content: String,  // answer yahan store hoga

    val sources: String?,  // JSON

    val timestamp: Long,

    val materialId: String?,

    val subjectId: String?
)