package com.aistudyos.app.domain.usecase.analytics

import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.domain.model.AnalyticsDashboard
import com.aistudyos.app.domain.repository.AnalyticsRepository
import javax.inject.Inject

class GetDashboardUseCase @Inject constructor(private val repo: AnalyticsRepository) {
    suspend operator fun invoke(): Result<AnalyticsDashboard> = repo.getDashboard()
}
