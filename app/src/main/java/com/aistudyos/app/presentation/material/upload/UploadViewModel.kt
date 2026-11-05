package com.aistudyos.app.presentation.material.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.domain.model.Material
import com.aistudyos.app.domain.usecase.material.UploadMaterialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadUseCase: UploadMaterialUseCase
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UiState<Material>>(UiState.Idle)
    val uploadState: StateFlow<UiState<Material>> = _uploadState

    var selectedFileUri: Uri? = null
    var selectedFileName: String = ""
    var selectedFileType: String = "PDF"

    fun upload(subjectId: String, title: String) {
        val uri = selectedFileUri ?: return
        viewModelScope.launch {
            _uploadState.value = UiState.Loading
            _uploadState.value = when (val r = uploadUseCase(subjectId, title, selectedFileType, uri)) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.exception.message)
                else -> UiState.Error("Unexpected")
            }
        }
    }

    fun reset() { _uploadState.value = UiState.Idle }
}
