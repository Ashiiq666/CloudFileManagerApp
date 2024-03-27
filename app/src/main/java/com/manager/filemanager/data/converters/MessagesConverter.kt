
package com.manager.filemanager.data.converters

import androidx.room.TypeConverter
import com.manager.filemanager.compose.core.models.Message
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessagesConverter {
    @TypeConverter
    fun fromMessages(messages: List<Message>): String {
        return  Json.encodeToString(messages)
    }
    @TypeConverter
    fun toMessages(messages: String): List<Message> {
        return Json.decodeFromString(messages)
    }
}