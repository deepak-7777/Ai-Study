package com.aistudyos.app.data.remote.api

import com.aistudyos.app.data.remote.dto.request.ChatRequest
import com.aistudyos.app.data.remote.dto.response.ApiResponse
import com.aistudyos.app.data.remote.dto.response.ChatMessageDto
import retrofit2.Response
import retrofit2.http.*

interface ChatApiService {

    @POST("api/v1/chat/ask")
    suspend fun ask(
        @Body request: ChatRequest
    ): Response<ApiResponse<ChatMessageDto>>

    @GET("api/v1/chat/history")
    suspend fun getHistory(
        @Query("materialId") materialId: String? = null,
        @Query("subjectId") subjectId: String? = null
    ): Response<ApiResponse<List<ChatMessageDto>>>
}