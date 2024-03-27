
package com.manager.filemanager.compose.feature.presentation.chat_screen

import com.manager.filemanager.compose.core.models.Chat

data class ChatUiState(
    val chatList: List<Chat> = emptyList(),
    val chat: Chat = Chat(),
)