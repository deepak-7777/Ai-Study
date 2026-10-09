package com.aistudyos.app.data.repository

import com.aistudyos.app.core.common.models.AppException
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.utils.toAppException
import com.aistudyos.app.data.local.db.dao.ChatMessageDao
import com.aistudyos.app.data.local.db.entity.ChatMessageEntity
import com.aistudyos.app.data.remote.api.ChatApiService
import com.aistudyos.app.data.remote.dto.request.ChatRequest
import com.aistudyos.app.domain.model.ChatMessage
import com.aistudyos.app.domain.repository.ChatRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val dao: ChatMessageDao,
    private val gson: Gson
) : ChatRepository {

    override fun observeMessages(materialId: String): Flow<List<ChatMessage>> =
        dao.observeByMaterial(materialId)
            .map { list -> list.map { it.toDomain() } }

    override fun observeMessagesBySubject(subjectId: String): Flow<List<ChatMessage>> =
        dao.observeBySubject(subjectId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun sendMessage(
        message: String,
        materialId: String?,
        subjectId: String?
    ): Result<ChatMessage> = safeCall {

        val request = ChatRequest(
            question = message,
            materialId = materialId,
            subjectId = subjectId
        )

        val response = api.ask(request)

        if (response.isSuccessful && response.body()?.success == true) {

            val dto = response.body()!!.data!!

            val entity = ChatMessageEntity(
                id = System.currentTimeMillis().toString() + "_" + Random.nextInt(1000),
                role = "assistant",
                content = dto.answer ?: "",
                sources = dto.sources?.map { it.preview }?.let { gson.toJson(it) },

                // ✅ FIXED (IMPORTANT)
                timestamp = dto.timestamp?.toLongOrNull() ?: System.currentTimeMillis(),

                materialId = materialId,
                subjectId = subjectId
            )

            dao.insert(entity)

            Result.Success(entity.toDomain())

        } else {
            Result.Error(
                AppException.HttpException(response.code(), "Chat failed")
            )
        }
    }

    override suspend fun loadHistory(
        materialId: String?,
        subjectId: String?
    ): Result<List<ChatMessage>> = safeCall {

        val response = api.getHistory(materialId, subjectId)

        if (response.isSuccessful && response.body()?.success == true) {

            val list = response.body()!!.data ?: emptyList()

            val entities = list.map { dto ->
                ChatMessageEntity(
                    id = System.currentTimeMillis().toString() + "_" + Random.nextInt(1000),
                    role = "assistant",
                    content = dto.answer ?: "",
                    sources = dto.sources?.map { it.preview }?.let { gson.toJson(it) },

                    // ✅ FIXED
                    timestamp = dto.timestamp?.toLongOrNull() ?: System.currentTimeMillis(),

                    materialId = materialId,
                    subjectId = subjectId
                )
            }

            Result.Success(entities.map { it.toDomain() })

        } else {
            Result.Error(
                AppException.HttpException(response.code(), "History fetch failed")
            )
        }
    }

    private fun ChatMessageEntity.toDomain(): ChatMessage {

        val srcList: List<String> =
            if (sources != null) {
                gson.fromJson(
                    sources,
                    object : TypeToken<List<String>>() {}.type
                ) ?: emptyList()
            } else emptyList()

        return ChatMessage(
            id = id,
            role = role,
            content = content,
            sources = srcList,
            timestamp = timestamp.toString() // UI me String
        )
    }

    private inline fun <T> safeCall(block: () -> Result<T>): Result<T> =
        try {
            block()
        } catch (e: Exception) {
            Result.Error(e.toAppException())
        }

    override suspend fun saveUserMessage(
        message: ChatMessage,
        materialId: String?,
        subjectId: String?
    ) {

        val entity = ChatMessageEntity(
            id = message.id,
            role = "user",
            content = message.content,
            sources = null,

            timestamp = System.currentTimeMillis(),

            materialId = materialId,
            subjectId = subjectId
        )

        dao.insert(entity)
    }
}