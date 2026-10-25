package com.aistudyos.app.domain.repository

import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun observeMessages(materialId: String): Flow<List<ChatMessage>>

    // 🔥 NEW
    fun observeMessagesBySubject(subjectId: String): Flow<List<ChatMessage>>

    suspend fun sendMessage(
        message: String,
        materialId: String?,
        subjectId: String?
    ): Result<ChatMessage>

    suspend fun loadHistory(
        materialId: String?,
        subjectId: String?
    ): Result<List<ChatMessage>>

    suspend fun saveUserMessage(
        message: ChatMessage,
        materialId: String?,
        subjectId: String?
    )
}