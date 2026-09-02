package com.aistudyos.app.domain.model

data class AnalyticsDashboard(
    val totalSubjects: Int = 0,
    val totalMaterials: Int = 0,
    val notesGenerated: Int = 0,
    val quizzesAttempted: Int = 0,
    val averageScore: Float = 0f,
    val weakTopicsCount: Int = 0,
    val studyStreak: Int = 0,
    val weeklyActivity: List<DayActivity> = emptyList()
)

data class DayActivity(
    val day: String,
    val minutes: Int
)