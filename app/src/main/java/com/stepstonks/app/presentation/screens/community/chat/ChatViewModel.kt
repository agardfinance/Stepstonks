package com.stepstonks.app.presentation.screens.community.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepstonks.app.domain.model.ChatMessage
import com.stepstonks.app.domain.model.MessageType
import com.stepstonks.app.domain.model.MOCK_MESSAGES
import com.stepstonks.app.domain.model.NEW_MESSAGES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = MOCK_MESSAGES,
    val isTyping: Boolean = false,
    val onlineCount: Int = 247
)

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val botUsernames = listOf("NeonStrider", "CryptoFitness", "BlockchainRunner", "StepstonksPro", "TokenHunter", "MoonWalker_X")
    private var messageIndex = 0

    init {
        simulateLiveChat()
    }

    private fun simulateLiveChat() {
        viewModelScope.launch {
            while (true) {
                delay((8_000L..20_000L).random())
                val isTypingShow = (0..2).random() != 0
                if (isTypingShow) {
                    _uiState.value = _uiState.value.copy(isTyping = true)
                    delay((1_500L..3_000L).random())
                }
                val content = NEW_MESSAGES[messageIndex % NEW_MESSAGES.size]
                messageIndex++
                val newMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    userId = "bot_${messageIndex}",
                    username = botUsernames.random(),
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + newMsg,
                    isTyping = false,
                    onlineCount = (200..500).random()
                )
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            userId = "current_user",
            username = "You",
            content = content,
            timestamp = System.currentTimeMillis(),
            isCurrentUser = true
        )
        _uiState.value = _uiState.value.copy(messages = _uiState.value.messages + msg)
    }

    fun toggleReaction(messageId: String, emoji: String) {
        val messages = _uiState.value.messages.map { msg ->
            if (msg.id == messageId) {
                val current = msg.reactions.toMutableMap()
                if (current.containsKey(emoji)) {
                    current[emoji] = (current[emoji] ?: 0) + 1
                } else {
                    current[emoji] = 1
                }
                msg.copy(reactions = current)
            } else msg
        }
        _uiState.value = _uiState.value.copy(messages = messages)
    }
}

private fun ClosedRange<Long>.random() = (endInclusive - start) * Math.random().toLong() + start
