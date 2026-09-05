package com.aistudyos.app.data.remote.mappers

import com.aistudyos.app.data.remote.dto.response.AnalyticsDashboardDto
import com.aistudyos.app.data.remote.dto.response.DayActivityDto
import com.aistudyos.app.domain.model.AnalyticsDashboard
import com.aistudyos.app.domain.model.DayActivity

fun AnalyticsDashboardDto.toDomain() = AnalyticsDashboard(
    totalSubjects = totalSubjects ?: 0,
    totalMaterials = totalMaterials ?: 0,
    notesGenerated = notesGenerated ?: 0,
    quizzesAttempted = quizzesAttempted ?: 0,
    averageScore = averageScore ?: 0f,
    weakTopicsCount = weakTopicsCount ?: 0,
    studyStreak = studyStreak ?: 0,

    // 🔥 CRASH FIX
    weeklyActivity = weeklyActivity?.map { it.toDomain() } ?: emptyList()
)

fun DayActivityDto.toDomain() = DayActivity(
    day = day ?: "",
    minutes = minutes ?: 0
)