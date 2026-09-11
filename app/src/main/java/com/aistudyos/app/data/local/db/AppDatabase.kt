package com.aistudyos.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aistudyos.app.data.local.db.dao.ChatMessageDao
import com.aistudyos.app.data.local.db.dao.MaterialDao
import com.aistudyos.app.data.local.db.dao.SubjectDao
import com.aistudyos.app.data.local.db.entity.ChatMessageEntity
import com.aistudyos.app.data.local.db.entity.MaterialEntity
import com.aistudyos.app.data.local.db.entity.SubjectEntity

@Database(
    entities = [SubjectEntity::class, MaterialEntity::class, ChatMessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun materialDao(): MaterialDao
    abstract fun chatMessageDao(): ChatMessageDao
}
