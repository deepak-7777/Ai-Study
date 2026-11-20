package com.aistudyos.app.domain.model

import com.aistudyos.app.core.common.enums.MaterialType
import com.aistudyos.app.core.common.enums.ProcessingStatus

data class Material(
    val id: String,
    val subjectId: String,
    val title: String,
    val type: MaterialType,
    val fileUrl: String?,
    val processingStatus: ProcessingStatus,
    val fileSize: Long?,
    val duration: Int?,
    val createdAt: String,
    val isPinned: Boolean = false
)