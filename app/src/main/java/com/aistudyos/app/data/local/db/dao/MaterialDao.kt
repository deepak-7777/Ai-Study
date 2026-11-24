package com.aistudyos.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.aistudyos.app.data.local.db.entity.MaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Query("""
        SELECT * FROM materials
        WHERE subjectId = :subjectId
        ORDER BY isPinned DESC, createdAt DESC
    """)
    fun observeBySubject(
        subjectId: String
    ): Flow<List<MaterialEntity>>

    @Query("""
        SELECT * FROM materials
        WHERE id = :id
    """)
    suspend fun getById(
        id: String
    ): MaterialEntity?

    @Upsert
    suspend fun upsertAll(
        materials: List<MaterialEntity>
    )

    @Query("""
        UPDATE materials
        SET processingStatus = :status
        WHERE id = :id
    """)
    suspend fun updateStatus(
        id: String,
        status: String
    )

    @Query("""
        UPDATE materials
        SET title = :title
        WHERE id = :id
    """)
    suspend fun updateTitle(
        id: String,
        title: String
    )

    @Query("""
        UPDATE materials
        SET isPinned = :pinned
        WHERE id = :id
    """)
    suspend fun updatePinned(
        id: String,
        pinned: Boolean
    )

    @Query("""
        DELETE FROM materials
        WHERE id = :id
    """)
    suspend fun deleteById(
        id: String
    )

    @Query("""
        SELECT * FROM materials
    """)
    fun observeAll(): Flow<List<MaterialEntity>>
}