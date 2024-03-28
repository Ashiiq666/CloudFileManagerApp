package com.manager.filemanager.compose.feature.presentation.categorylist.components

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manager.filemanager.compose.core.presentation.util.Screen
import com.manager.filemanager.compose.feature.presentation.categorylist.apklist.ApkListScreenBody
import com.manager.filemanager.compose.feature.presentation.categorylist.medialist.compenents.MediaGridView
import com.manager.filemanager.compose.feature.presentation.categorylist.medialist.viewmodel.MediaViewModel
import com.manager.filemanager.files.provider.archive.common.mime.MimeType
import com.manager.filemanager.manager.category.adapter.Category
import com.manager.filemanager.manager.category.adapter.CategoryFileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CategoryListScreen(
    innerPadding: PaddingValues,
    categoryFileModel: CategoryFileModel?,
    navigate: (route: String, args: Bundle) -> Unit
) {


    Column {
        when (categoryFileModel?.category) {
            Category.IMAGE -> GeneratingMediaList(
                innerPadding = innerPadding,
                mimeType = MimeType.IMAGE_ANY,
                navigate = navigate
            )

            Category.VIDEOS -> GeneratingMediaList(
                innerPadding = innerPadding,
                mimeType = MimeType.VIDEO_MP4,
                navigate = navigate
            )

            Category.AUDIOS -> LoadingBody()
            Category.DOCUMENTS -> LoadingBody()
            Category.APK -> ApkListScreenBody(innerPadding = innerPadding)
            else -> LoadingBody()
        }
    }
}


@Composable
fun GeneratingMediaList(
    innerPadding: PaddingValues,
    viewModel: MediaViewModel = viewModel(),
    mimeType: MimeType,
    navigate: (route: String, args: Bundle) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val loading by viewModel.loading.observeAsState(initial = true)

    val mediaUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mediaList = mediaUiState.mediaList

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.Unconfined) {
            viewModel.loadMediaList(context, mimeType)
        }
    }

    if (loading) {
        LoadingBody()
    } else {
        MediaGridView(mediaList = mediaList, paddingValues = innerPadding) {
            val args = Bundle()
            args.putParcelable("mediaInfo", it)
            navigate(Screen.MediaViewScreen.route, args)
        }
    }
}

@Composable
fun LoadingBody() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}