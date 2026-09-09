package com.aistudyos.app.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.domain.model.AnalyticsDashboard
import com.aistudyos.app.domain.usecase.analytics.GetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getDashboardUseCase: GetDashboardUseCase
) : ViewModel() {

    private val _analyticsState = MutableStateFlow<UiState<AnalyticsDashboard>>(UiState.Loading)
    val analyticsState: StateFlow<UiState<AnalyticsDashboard>> = _analyticsState

    init {
        viewModelScope.launch {
            _analyticsState.value = when (val r = getDashboardUseCase()) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.exception.message)
                else -> UiState.Idle
            }
        }
    }
}
