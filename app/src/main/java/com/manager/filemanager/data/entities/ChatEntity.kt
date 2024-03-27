package com.manager.filemanager.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.manager.filemanager.compose.core.models.ChatSettings
import com.manager.filemanager.compose.core.models.Message
import com.manager.filemanager.data.converters.ChatSettingsConverter
import com.manager.filemanager.data.converters.MessagesConverter

@Entity
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @TypeConverters(ChatSettingsConverter::class)
    val chatSettings: ChatSettings = ChatSettings(),
    @TypeConverters(MessagesConverter::class)
    val messages: List<Message> = emptyList()
)
