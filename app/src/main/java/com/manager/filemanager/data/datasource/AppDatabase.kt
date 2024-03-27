
package com.manager.filemanager.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manager.filemanager.data.converters.ChatSettingsConverter
import com.manager.filemanager.data.converters.MessagesConverter
import com.manager.filemanager.data.entities.ChatEntity

@Database(entities = [ChatEntity::class], version = 1, exportSchema = true)
@TypeConverters(ChatSettingsConverter::class, MessagesConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
}