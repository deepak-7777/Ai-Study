package com.aistudyos.app.data.local.db.dao

import androidx.room.*
import com.aistudyos.app.data.local.db.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM chat_messages WHERE materialId = :materialId ORDER BY timestamp ASC")
    fun observeByMaterial(materialId: String): Flow<List<ChatMessageEntity>>

    // 🔥 NEW
    @Query("SELECT * FROM chat_messages WHERE subjectId = :subjectId ORDER BY timestamp ASC")
    fun observeBySubject(subjectId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<ChatMessageEntity>)
}