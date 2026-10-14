package com.aistudyos.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyos.app.core.common.models.Result
import com.aistudyos.app.core.common.models.UiState
import com.aistudyos.app.domain.model.ChatMessage
import com.aistudyos.app.domain.repository.ChatRepository
import com.aistudyos.app.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private var materialId: String? = null
    private var subjectId: String? = null

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _sendState = MutableStateFlow<UiState<ChatMessage>>(UiState.Idle)
    val sendState: StateFlow<UiState<ChatMessage>> = _sendState

    fun init(materialId: String?, subjectId: String?) {
        this.materialId = materialId
        this.subjectId = subjectId

        if (materialId != null) {
            chatRepository.observeMessages(materialId)
                .onEach { _messages.value = it }
                .launchIn(viewModelScope)
        } else if (subjectId != null) {
            chatRepository.observeMessagesBySubject(subjectId)
                .onEach { _messages.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {

            _sendState.value = UiState.Loading

            val userMsg = ChatMessage(
                id = System.currentTimeMillis().toString() + "_u",
                role = "user",
                content = text,
                sources = emptyList(),
                timestamp = System.currentTimeMillis().toString()
            )

            // ✅ ONLY DB
            chatRepository.saveUserMessage(userMsg, materialId, subjectId)

            when (val r = sendMessageUseCase(text, materialId, subjectId)) {

                is Result.Success -> {
                    _sendState.value = UiState.Success(r.data)
                }

                is Result.Error -> {
                    _sendState.value = UiState.Error(r.exception.message)
                }

                else -> {
                    _sendState.value = UiState.Idle
                }
            }
        }
    }
}