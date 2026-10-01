package com.aistudyos.app.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aistudyos.app.R
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _b: FragmentChatBinding? = null
    private val b get() = _b!!

    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()

    private lateinit var chatAdapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentChatBinding.bind(view)

        chatAdapter = ChatAdapter()

        b.rvMessages.layoutManager =
            LinearLayoutManager(requireContext()).apply { stackFromEnd = true }

        b.rvMessages.adapter = chatAdapter

        viewModel.init(args.materialId, args.subjectId)

        b.btnSend.setOnClickListener {
            val text = b.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
                b.etMessage.setText("")
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.messages.collect { messages ->
                        chatAdapter.submitList(messages)
                        if (messages.isNotEmpty()) {
                            b.rvMessages.scrollToPosition(messages.size - 1)
                        }
                    }
                }

                launch {
                    viewModel.sendState.collect { state ->
                        b.progressTyping.isVisible = state is UiState.Loading
                        b.btnSend.isEnabled = state !is UiState.Loading
                    }
                }

            }
        }

        b.etMessage.requestFocus()

        b.etMessage.post {
            val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                    as android.view.inputmethod.InputMethodManager
            imm.showSoftInput(b.etMessage, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
