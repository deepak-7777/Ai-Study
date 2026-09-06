package com.aistudyos.app.domain.repository

import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.AnalyticsDashboard

interface AnalyticsRepository {
    suspend fun getDashboard(): Result<AnalyticsDashboard>
    suspend fun getWeakTopics(): Result<List<String>>
}
