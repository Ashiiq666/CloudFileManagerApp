package com.manager.filemanager.data.repository

import com.manager.filemanager.data.datasource.ChatDao
import com.manager.filemanager.data.entities.ChatEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
): ChatRepository {

    override suspend fun upsert(chat: ChatEntity) = chatDao.upsert(chat)
    override suspend fun getAllChat(): Flow<List<ChatEntity>> = chatDao.getAllChat()

    override suspend fun delete(chat: ChatEntity) = chatDao.delete(chat)
}