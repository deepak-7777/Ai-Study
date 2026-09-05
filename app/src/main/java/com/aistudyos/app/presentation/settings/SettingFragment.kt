package com.aistudyos.app.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aistudyos.app.R
import com.aistudyos.app.databinding.FragmentSettingBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingBinding.bind(view)

        observeUser()

        binding.settingImage.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUser()
    }

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.user.collect { user ->

                    user?.let {

                        // 👤 Name
                        binding.settingName.text = it.name

                        // 📝 About
                        binding.settingAbout.text = it.about ?: "No bio"

                        // 🖼 Image
                        if (!it.avatarUrl.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(it.avatarUrl)
                                .placeholder(R.drawable.ic_person)
                                .into(binding.settingImage)
                        } else {
                            binding.settingImage.setImageResource(R.drawable.ic_person)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}