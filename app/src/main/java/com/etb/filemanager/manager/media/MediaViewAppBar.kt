/*
 * Copyright (c)  2023  Juan Nascimento
 * Part of FileManagerSphere - MediaViewAppBar.kt
 * SPDX-License-Identifier: GPL-3.0-or-later
 * More details at: https://www.gnu.org/licenses/
 */

package com.etb.filemanager.manager.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.etb.filemanager.ui.style.Back40P
import com.etb.filemanager.ui.util.Constants
import kotlinx.coroutines.launch

@Composable
fun MediaViewAppBar(
    showUI: Boolean,
    showInfo: Boolean,
    showDate: Boolean,
    paddingValues: PaddingValues,
    bottomSheetState: AppBottomSheetState,
    onGoBack: () -> Unit
) {

    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = showUI,
        enter = Constants.Animation.enterAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        exit = Constants.Animation.exitAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION),
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Back40P, Color.Transparent)
                    )
                )
                .padding(top = paddingValues.calculateTopPadding())
                .padding(start = 5.dp, end = if (showInfo) 8.dp else 16.dp)
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onGoBack) {
                Image(
                    imageVector = Icons.Outlined.ArrowBack,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "Go back",
                    modifier = Modifier
                        .height(48.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showDate) {
                    Text(
                        text = "",
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
            if (showInfo) {
                IconButton(
                    onClick = {
                        scope.launch {
                            bottomSheetState.show()
                        }
                    }
                ) {

                    Image(
                        imageVector = Icons.Outlined.Info,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = "info",
                        modifier = Modifier
                            .height(48.dp)
                    )
                }
            }
        }
    }
}