package com.manager.filemanager.compose.core.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manager.filemanager.files.extensions.navigate
import com.manager.filemanager.files.extensions.parcelable
import com.manager.filemanager.manager.media.MediaViewScreen
import com.manager.filemanager.manager.media.model.MediaListInfo

const val MediaViewRoute = "media_view"


fun NavGraphBuilder.mediaViewScreen(
    paddingValues: PaddingValues
){

    composable(
        route = MediaViewRoute,
    ){

        val mediaListInfo = it.arguments?.parcelable<MediaListInfo>("mediaInfo")
        MediaViewScreen(
            mediaListInfo = mediaListInfo!!,
            paddingValues = paddingValues,
            toggleRotate = { /*TODO*/ }) {

        }
    }
}

fun NavController.navigateToMediaView(arg: Bundle, navOptions: NavOptions? = null){
    navigate(MediaViewRoute, arg, navOptions)
}