package com.aistudyos.app.presentation.analytics

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aistudyos.app.R
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.databinding.FragmentAnalyticsBinding
import com.aistudyos.app.domain.model.AnalyticsDashboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    private var _b: FragmentAnalyticsBinding? = null
    private val b get() = _b!!
    private val viewModel: AnalyticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentAnalyticsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.analyticsState.collect { state ->
                    b.progressBar.isVisible  = state is UiState.Loading
                    b.contentGroup.isVisible = state is UiState.Success
                    if (state is UiState.Success) bindData(state.data)
                }
            }
        }
    }

    private fun bindData(data: AnalyticsDashboard) {
        b.tvTotalSubjects.text   = data.totalSubjects.toString()
        b.tvTotalMaterials.text  = data.totalMaterials.toString()
        b.tvNotesGenerated.text  = data.notesGenerated.toString()
        b.tvQuizAttempted.text   = data.quizzesAttempted.toString()
        b.tvAvgScore.text        = "${data.averageScore.toInt()}%"
        b.tvWeakTopics.text      = data.weakTopicsCount.toString()
        b.tvStreak.text          = "${data.studyStreak} days"
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
