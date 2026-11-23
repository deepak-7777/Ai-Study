package com.aistudyos.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class MaterialDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("subjectId")   // ✅ FIX
    val subjectId: String,

    @SerializedName("displayName") // ✅ already correct
    val title: String?,

    @SerializedName("materialType") // ✅ FIX
    val type: String,

    @SerializedName("cloudinaryUrl") // ✅ FIX
    val fileUrl: String?,

    @SerializedName("processingStatus") // ✅ FIX
    val processingStatus: String,

    @SerializedName("fileSizeBytes") // ✅ FIX
    val fileSize: Long?,

    @SerializedName("duration")
    val duration: Int?,

    @SerializedName("createdAt") // ✅ FIX
    val createdAt: String,

    @SerializedName("pinned")
    val isPinned: Boolean = false

)