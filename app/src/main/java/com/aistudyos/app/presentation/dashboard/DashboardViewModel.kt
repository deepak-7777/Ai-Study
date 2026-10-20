package com.aistudyos.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.data.local.prefs.SessionManager
import com.aistudyos.app.domain.model.AnalyticsDashboard
import com.aistudyos.app.domain.repository.AuthRepository
import com.aistudyos.app.domain.usecase.analytics.GetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetDashboardUseCase,
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _dashboardState =
        MutableStateFlow<UiState<AnalyticsDashboard>>(UiState.Loading)
    val dashboardState: StateFlow<UiState<AnalyticsDashboard>> = _dashboardState

    private val _userName = MutableStateFlow("Student")
    val userName: StateFlow<String> = _userName

    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl: StateFlow<String?> = _avatarUrl

    init {
        loadDashboard()
    }

    fun loadDashboard(forceRefresh: Boolean = false) {
        viewModelScope.launch {

            _userName.value = sessionManager.getUserName() ?: "Student"

            if (forceRefresh || _dashboardState.value !is UiState.Success) {
                _dashboardState.value = UiState.Loading
            }

            _dashboardState.value = when (val r = getDashboardUseCase()) {
                is Result.Success -> {
                    val safeData = r.data ?: AnalyticsDashboard()
                    UiState.Success(safeData)
                }
                is Result.Error -> UiState.Error(r.exception.message ?: "Error")
                else -> UiState.Idle
            }
        }
    }
}