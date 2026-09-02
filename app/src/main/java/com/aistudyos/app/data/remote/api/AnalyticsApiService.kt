package com.aistudyos.app.data.remote.api

import com.aistudyos.app.data.remote.dto.response.AnalyticsDashboardDto
import com.aistudyos.app.data.remote.dto.response.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsApiService {
    @GET("api/v1/analytics/dashboard")
    suspend fun getDashboard(): Response<ApiResponse<AnalyticsDashboardDto>>

    @GET("api/v1/analytics/weak-topics")
    suspend fun getWeakTopics(): Response<ApiResponse<List<String>>>
}
