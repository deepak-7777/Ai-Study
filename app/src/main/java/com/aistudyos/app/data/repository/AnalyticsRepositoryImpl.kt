package com.aistudyos.app.data.repository

import com.aistudyos.app.core.common.models.AppException
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.utils.toAppException
import com.aistudyos.app.data.remote.api.AnalyticsApiService
import com.aistudyos.app.data.remote.mappers.toDomain
import com.aistudyos.app.domain.model.AnalyticsDashboard
import com.aistudyos.app.domain.repository.AnalyticsRepository
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(private val api: AnalyticsApiService) : AnalyticsRepository {

    override suspend fun getDashboard(): Result<AnalyticsDashboard> = safeCall {
        val response = api.getDashboard()
        if (response.isSuccessful && response.body()?.success == true)
            Result.Success(response.body()!!.data!!.toDomain())
        else Result.Error(AppException.HttpException(response.code(), "Dashboard fetch failed"))
    }

    override suspend fun getWeakTopics(): Result<List<String>> = safeCall {
        val response = api.getWeakTopics()
        if (response.isSuccessful && response.body()?.success == true)
            Result.Success(response.body()!!.data ?: emptyList())
        else Result.Error(AppException.HttpException(response.code(), "Failed"))
    }

    private inline fun <T> safeCall(block: () -> Result<T>): Result<T> =
        try { block() } catch (e: Exception) { Result.Error(e.toAppException()) }
}
