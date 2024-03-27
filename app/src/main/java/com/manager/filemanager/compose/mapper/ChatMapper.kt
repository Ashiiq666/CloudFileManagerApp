package com.manager.filemanager.compose.mapper

import com.manager.filemanager.compose.core.models.Chat
import com.manager.filemanager.data.entities.ChatEntity


fun ChatEntity.toChat() = Chat(
    id = id,
    chatSettings = chatSettings,
    messages = messages
)

fun Chat.toChatEntity() = ChatEntity(
    id = id,
    chatSettings = chatSettings,
    messages = messages
)