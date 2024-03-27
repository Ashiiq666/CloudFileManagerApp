
package com.manager.filemanager.data.repository

import com.manager.filemanager.data.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun upsert(chat: ChatEntity)

    suspend fun getAllChat(): Flow<List<ChatEntity>>

    suspend fun delete(chat: ChatEntity)
}