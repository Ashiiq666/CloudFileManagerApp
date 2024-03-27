package com.manager.filemanager.compose.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.manager.filemanager.manager.category.adapter.CategoryFileModel

@Composable
fun NavigationComp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    paddingValues: PaddingValues,
    categoryFileModel: CategoryFileModel?
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {

        categoryListScreen(
            paddingValues = paddingValues,
            categoryFileModel = categoryFileModel,
            onNavigateToMediaView = {
                navController.navigateToMediaView(it)
            }
        )

        mediaViewScreen(
            paddingValues = paddingValues
        )

        chatScreen(
            paddingValues = paddingValues
        )
    }
}