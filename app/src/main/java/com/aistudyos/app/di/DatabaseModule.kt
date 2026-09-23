package com.aistudyos.app.di

import android.content.Context
import androidx.room.Room
import com.aistudyos.app.core.common.constants.AppConstants
import com.aistudyos.app.data.local.db.AppDatabase
import com.aistudyos.app.data.local.db.dao.ChatMessageDao
import com.aistudyos.app.data.local.db.dao.MaterialDao
import com.aistudyos.app.data.local.db.dao.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DB_NAME
        )
            .fallbackToDestructiveMigration() // ✅ IMPORTANT (schema change safe)
            .build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(db: AppDatabase): SubjectDao {
        return db.subjectDao()
    }

    @Provides
    @Singleton
    fun provideMaterialDao(db: AppDatabase): MaterialDao {
        return db.materialDao()
    }

    @Provides
    @Singleton
    fun provideChatMessageDao(db: AppDatabase): ChatMessageDao {
        return db.chatMessageDao()
    }
}