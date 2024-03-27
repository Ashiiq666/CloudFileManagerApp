
package com.manager.filemanager.data.di

import android.content.Context
import androidx.room.Room
import com.manager.filemanager.compose.core.presentation.util.Prompt
import com.manager.filemanager.data.datasource.AppDatabase
import com.manager.filemanager.data.datasource.AppMigrations
import com.manager.filemanager.data.datasource.ChatDao
import com.manager.filemanager.data.repository.ChatRepository
import com.manager.filemanager.data.repository.ChatRepositoryImpl
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "app_database"
        ).addMigrations(AppMigrations.MIGRATION_1_2).build()
    }

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    @Singleton
    fun provideChatRepository(chatDao: ChatDao): ChatRepository {
        return ChatRepositoryImpl(chatDao)
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-pro", apiKey = ""
        )
    }

    @Provides
    @Singleton
    fun provideChat(generativeModel: GenerativeModel): Chat {
        val chat = generativeModel.startChat(
            history = listOf(content(
                role = "user"
            ) {
                text(Prompt.FILE_OPERATIONS)
            }, content(
                role = "model"
            ) {
                text(Prompt.FILE_OPERATIONS_MODEL)
            }, content(
                role = "user"
            ) {
                text(Prompt.FILE_OPERATIONS_EXAMPLE)
            }, content(
                role = "model",
            ) {
                text(Prompt.FILE_OPERATIONS_EXAMPLE_MODEL)
            })
        )

        return chat
    }
}