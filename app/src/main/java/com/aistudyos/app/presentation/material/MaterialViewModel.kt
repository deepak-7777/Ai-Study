package com.aistudyos.app.presentation.material

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyos.app.domain.model.Material
import com.aistudyos.app.domain.repository.MaterialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialViewModel @Inject constructor(
    private val repo: MaterialRepository
) : ViewModel() {

    private var currentSubjectId: String? = null

    fun getMaterials(subjectId: String): StateFlow<List<Material>> {
        currentSubjectId = subjectId
        return repo.observeMaterials(subjectId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    }

    fun fetchMaterials(subjectId: String) {
        currentSubjectId = subjectId
        viewModelScope.launch {
            repo.fetchMaterials(subjectId)
        }
    }

    fun renameMaterial(
        id: String,
        title: String
    ) {
        viewModelScope.launch {
            repo.renameMaterial(
                id,
                title
            )
            currentSubjectId?.let {
                repo.fetchMaterials(it)
            }
        }
    }

    fun pinMaterial(
        id: String,
        pinned: Boolean
    ) {
        viewModelScope.launch {
            repo.pinMaterial(
                id,
                pinned
            )
            currentSubjectId?.let {
                repo.fetchMaterials(it)
            }
        }
    }

    fun deleteMaterial(
        id: String
    ) {
        viewModelScope.launch {
            repo.deleteMaterial(id)
            currentSubjectId?.let {
                repo.fetchMaterials(it)
            }
        }
    }
}