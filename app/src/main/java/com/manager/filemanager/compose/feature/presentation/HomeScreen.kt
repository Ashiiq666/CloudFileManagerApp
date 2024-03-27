
package com.manager.filemanager.compose.feature.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.manager.filemanager.compose.core.navigation.ChatRoute
import com.manager.filemanager.compose.core.navigation.NavigationComp
import com.manager.filemanager.compose.feature.provider.BaseScreen
import com.manager.filemanager.files.extensions.parcelable
import com.manager.filemanager.manager.category.adapter.CategoryFileModel
import com.manager.filemanager.ui.theme.FileManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreen : BaseScreen() {
    private var categoryFileModel: CategoryFileModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val startDestination = intent.getStringExtra("startDestination")!!

        if (startDestination != ChatRoute && bundle != null){
            categoryFileModel = bundle.parcelable("categoryFileModel")
        }

       /* if (bundle != null) {
            categoryFileModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("categoryFileModel", CategoryFileModel::class.java)
            } else {
                intent.parcelable("categoryFileModel")
            }
        }
*/
        setContent {
            val navController = rememberNavController()

            FileManagerTheme {
                Scaffold(
                    content = { innerPadding ->
                        NavigationComp(
                            navController = navController,
                            startDestination = startDestination,
                            paddingValues = innerPadding,
                            categoryFileModel = categoryFileModel
                        )
                    }
                )

            }
        }
    }
}


