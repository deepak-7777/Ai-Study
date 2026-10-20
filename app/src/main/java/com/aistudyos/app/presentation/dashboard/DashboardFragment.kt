package com.aistudyos.app.presentation.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aistudyos.app.R
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.databinding.FragmentDashboardBinding
import com.aistudyos.app.domain.model.AnalyticsDashboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _b: FragmentDashboardBinding? = null
    private val b get() = _b!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentDashboardBinding.bind(view)

        setupClicks()
        observeData()
    }

    private fun setupClicks() {
        b.swipeRefresh.setOnRefreshListener {
            b.swipeRefresh.isRefreshing = true
            viewModel.loadDashboard(true)
        }

        b.cardSubjects.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_subjects)
        }

//        b.cardChat.setOnClickListener {
//          findNavController().navigate(R.id.action_dashboard_to_chat)
//        }

        b.cardRevision.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_revision)
        }

//        b.cardAnalytics.setOnClickListener {
//           findNavController().navigate(R.id.action_dashboard_to_analytics)
//        }

        b.notification.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 👤 Username
                launch {
                    viewModel.userName.collect {
                        b.tvGreeting.text = "Hello, ${it ?: "Student"} 👋"
                    }
                }

                // 📊 Dashboard Data
                launch {
                    viewModel.dashboardState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {
                                b.shimmerLayout.isVisible = true
                                b.contentGroup.isVisible = false
                                b.layoutError.isVisible = false
                            }

                            is UiState.Success -> {
                                b.shimmerLayout.isVisible = false
                                b.contentGroup.isVisible = true
                                b.layoutError.isVisible = false

                                b.swipeRefresh.isRefreshing = false

                                bindDashboard(state.data)
                            }

                            is UiState.Error -> {
                                b.shimmerLayout.isVisible = false
                                b.contentGroup.isVisible = false
                                b.layoutError.isVisible = true

                                b.swipeRefresh.isRefreshing = false
                                b.tvError.text = state.message
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun bindDashboard(data: AnalyticsDashboard) {

        // 🔥 SAFE DATA BINDING
        b.tvSubjectsCount.text = data.totalSubjects.toString()
        b.tvMaterialsCount.text = data.totalMaterials.toString()
        b.tvNotesCount.text = data.notesGenerated.toString()
        b.tvQuizzesCount.text = data.quizzesAttempted.toString()
        b.tvAvgScore.text = "${data.averageScore.toInt()}%"
        b.tvStreak.text = "${data.studyStreak} days"
        b.tvWeakTopics.text = data.weakTopicsCount.toString()

        // 🔥 CRASH FIX (IMPORTANT)
        val weeklyActivity = data.weeklyActivity.orEmpty()

        weeklyActivity.forEach {
            // future UI usage (safe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}