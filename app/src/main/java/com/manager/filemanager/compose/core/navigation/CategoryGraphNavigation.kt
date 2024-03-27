package com.manager.filemanager.compose.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.manager.filemanager.manager.category.adapter.CategoryFileModel

const val CategoryGraphPattern = "category"

fun NavController.navigateToCategoryGraph(navOptions: NavOptions? = null) {
    navigate(CategoryGraphPattern, navOptions)
}

fun NavGraphBuilder.categoryGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    categoryFileModel: CategoryFileModel?
) {
    navigation(
        startDestination = CategoryListRoute, route = CategoryGraphPattern
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
    }
}