
package com.manager.filemanager.compose.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manager.filemanager.compose.feature.presentation.chat_screen.ChatScreen
import com.manager.filemanager.compose.feature.presentation.chat_screen.ChatViewModel

const val ChatRoute = "chat"

fun NavGraphBuilder.chatScreen(
    paddingValues: PaddingValues
) {
    composable(
        route = ChatRoute
    ) {
        val viewModel: ChatViewModel = hiltViewModel()
        val uiState by viewModel.state.collectAsStateWithLifecycle()

        ChatScreen(uiState = uiState,
            paddingValues = paddingValues,
            onClickSendMsg = { msg ->
                viewModel.sendMessage(msg)
            },
            onClickChat = { viewModel.setCurrentChat(it) }) { viewModel.newChat() }
    }
}

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    navigate(ChatRoute, navOptions)
}
