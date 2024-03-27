package com.manager.filemanager.compose.core.extensions

import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.manager.filemanager.compose.core.models.Message
import com.manager.filemanager.compose.core.models.Participant

fun List<Message>.hasPendingMessage(): Boolean = any { it.isPending }

fun List<Message>.toContent(): List<Content> {
    val filteredList =
        filter { !it.isPending && it.participant != Participant.ERROR && it.participant != Participant.USER_ERROR }

    return filteredList.map { msg ->
        val role = if (msg.participant == Participant.USER) "user" else "model"
        content(role = role) {
            text(msg.text)
        }
    }
}
