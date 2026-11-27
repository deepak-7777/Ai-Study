package com.aistudyos.app.presentation.material.upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.databinding.FragmentUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UploadViewModel by viewModels()
    private val args: UploadFragmentArgs by navArgs()

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            viewModel.selectedFileUri  = uri
            viewModel.selectedFileName = uri.lastPathSegment ?: "file"
            binding.tvFileName.text    = viewModel.selectedFileName
            binding.tvFileName.isVisible = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.cardPickPdf.setOnClickListener {
            viewModel.selectedFileType = "PDF"
            pickFile("application/pdf")
        }
        binding.cardPickAudio.setOnClickListener {
            viewModel.selectedFileType = "AUDIO"
            pickFile("audio/*")
        }

        binding.btnUpload.setOnClickListener {
            val title = binding.etTitle.text.toString()
            viewModel.upload(args.subjectId, title)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uploadState.collect { state ->
                    binding.progressBar.isVisible = state is UiState.Loading
                    binding.btnUpload.isEnabled   = state !is UiState.Loading
                    when (state) {
                        is UiState.Success -> {
                            binding.tvSuccess.isVisible = true
                            binding.tvSuccess.text = "Upload successful! Processing started."
                            viewModel.reset()
                        }
                        is UiState.Error -> {
                            binding.tvError.isVisible = true
                            binding.tvError.text = state.message
                        }
                        else -> {
                            binding.tvSuccess.isVisible = false
                            binding.tvError.isVisible   = false
                        }
                    }
                }
            }
        }
    }

    private fun pickFile(mimeType: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = mimeType }
        pickFileLauncher.launch(intent)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
