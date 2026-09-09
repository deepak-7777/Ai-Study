package com.aistudyos.app.data.remote.dto.response
import com.google.gson.annotations.SerializedName

/**
 * Generic API envelope returned by all backend endpoints.
 */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data")    val data: T?,
    @SerializedName("error")   val error: String?
)

data class PagedResponse<T>(
    @SerializedName("content")       val content: List<T>,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("totalPages")    val totalPages: Int,
    @SerializedName("page")         val page: Int,
    @SerializedName("size")         val size: Int
)
