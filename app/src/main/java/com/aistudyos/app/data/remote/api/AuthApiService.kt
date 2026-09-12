package com.aistudyos.app.data.remote.api

import com.aistudyos.app.data.remote.dto.request.LoginRequest
import com.aistudyos.app.data.remote.dto.request.RegisterRequest
import com.aistudyos.app.data.remote.dto.response.ApiResponse
import com.aistudyos.app.data.remote.dto.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/google")
    suspend fun googleLogin(@Body body: Map<String, String>): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body body: Map<String, String>): Response<ApiResponse<Unit>>
}