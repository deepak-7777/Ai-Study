package com.aistudyos.app.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ChatRequest(

    // ✅ FIX: backend expects "question"
    @SerializedName("question")
    val question: String,

    @SerializedName("materialId")
    val materialId: String? = null,

    @SerializedName("subjectId")
    val subjectId: String? = null
)