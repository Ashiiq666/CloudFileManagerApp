

package com.manager.filemanager.compose.core.presentation.components

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.manager.filemanager.compose.core.presentation.common.ChanneledViewModel
import com.manager.filemanager.compose.core.presentation.util.Screen
import com.manager.filemanager.compose.feature.presentation.categorylist.components.CategoryListScreen
import com.manager.filemanager.files.extensions.parcelable
import com.manager.filemanager.manager.category.adapter.CategoryFileModel
import com.manager.filemanager.manager.media.MediaViewScreen
import com.manager.filemanager.manager.media.model.MediaListInfo
import com.manager.filemanager.ui.util.Constants.Animation.navigateInAnimation
import com.manager.filemanager.ui.util.Constants.Animation.navigateUpAnimation

@Composable
fun NavigationComp(
    navController: NavHostController,
    categoryFileModel: CategoryFileModel,
    paddingValues: PaddingValues,
    toggleRotate: () -> Unit,

    ) {

    val navPipe = hiltViewModel<ChanneledViewModel>()
    navPipe
        .initWithNav(navController)
        .collectAsStateWithLifecycle(LocalLifecycleOwner.current)

    NavHost(
        navController = navController,
        startDestination = Screen.CategoryListScreen.route
    ) {
        composable(
            route = Screen.CategoryListScreen.route,
            enterTransition = { navigateInAnimation },
            exitTransition = { navigateUpAnimation },
            popEnterTransition = { navigateInAnimation },
            popExitTransition = { navigateUpAnimation }
        ) {
            CategoryListScreen(
                innerPadding = paddingValues,
                categoryFileModel = categoryFileModel,
                navigate = navPipe::navigate
            )
        }
        composable(
            route = Screen.MediaViewScreen.route,
            enterTransition = { navigateInAnimation },
            exitTransition = { navigateUpAnimation },
            popEnterTransition = { navigateInAnimation },
            popExitTransition = { navigateUpAnimation },
        ) { backStackEntry ->
            val mediaInfo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    backStackEntry.arguments?.getParcelable("mediaInfo", MediaListInfo::class.java)
                } else {
                    backStackEntry.arguments?.parcelable("mediaInfo")
                }

            MediaViewScreen(
                mediaListInfo = mediaInfo!!,
                paddingValues = paddingValues,
                toggleRotate = toggleRotate,
                navigateUp = navPipe::navigateUp)
        }
    }
}