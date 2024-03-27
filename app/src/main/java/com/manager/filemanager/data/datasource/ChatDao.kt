package com.manager.filemanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.manager.filemanager.data.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsert(chatEntity: ChatEntity)
    @Query("SELECT * FROM ChatEntity")
    fun getAllChat(): Flow<List<ChatEntity>>
    @Delete
    suspend fun delete(chatEntity: ChatEntity)
}