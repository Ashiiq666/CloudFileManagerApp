
package com.manager.filemanager.compose.feature.presentation.chat_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manager.filemanager.compose.core.models.Message

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    list: List<Message>,
    paddingValues: PaddingValues
) {

    LazyColumn(
        contentPadding = paddingValues,
        state = state,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(list) { msg ->
            MessageItem(
                modifier = Modifier,
                message = msg)
        }
    }
}