
package com.manager.filemanager.compose.core.models

data class Chat(
    val id: Int = 0,
    val chatSettings: ChatSettings = ChatSettings(),
    val messages: List<Message> = emptyList(),
)
