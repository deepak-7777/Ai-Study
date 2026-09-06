package com.aistudyos.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AnalyticsDashboardDto(
    @SerializedName("totalSubjects") val totalSubjects: Int?,
    @SerializedName("totalMaterials") val totalMaterials: Int?,
    @SerializedName("notesGenerated") val notesGenerated: Int?,
    @SerializedName("quizzesAttempted") val quizzesAttempted: Int?,
    @SerializedName("averageScore") val averageScore: Float?,
    @SerializedName("weakTopicsCount") val weakTopicsCount: Int?,
    @SerializedName("studyStreak") val studyStreak: Int?,
    @SerializedName("weeklyActivity") val weeklyActivity: List<DayActivityDto>?
)

data class DayActivityDto(
    @SerializedName("day") val day: String?,
    @SerializedName("minutes") val minutes: Int?
)