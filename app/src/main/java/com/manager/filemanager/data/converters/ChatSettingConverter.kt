package com.manager.filemanager.data.converters

import androidx.room.TypeConverter
import com.manager.filemanager.compose.core.models.ChatSettings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatSettingsConverter {
    @TypeConverter
    fun fromChatSettings(chatSettings: ChatSettings): String {
        return Json.encodeToString(chatSettings)
    }

    @TypeConverter
    fun toChatSettings(chatSettings: String): ChatSettings {
        return Json.decodeFromString(chatSettings)
    }
}