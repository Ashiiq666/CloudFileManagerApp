
package com.manager.filemanager.compose.core.presentation.util

sealed class Screen(val route: String){
    object CategoryListScreen : Screen("category_list_screen")
    object MediaViewScreen : Screen("media_screen")

    operator fun invoke() = route
}
