

package com.manager.filemanager.compose.feature.presentation.categorylist.medialist.compenents

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import com.bumptech.glide.load.DecodeFormat
import com.manager.filemanager.files.provider.archive.common.mime.MimeType
import com.manager.filemanager.files.provider.archive.common.mime.isASpecificTypeOfMime
import com.manager.filemanager.files.util.getVideoDuration
import com.manager.filemanager.manager.media.model.Media
import com.manager.filemanager.manager.media.model.MediaKey
import com.manager.filemanager.manager.media.model.MediaListInfo
import com.manager.filemanager.manager.media.model.toMediaListInfo
import com.manager.filemanager.ui.util.Constants
import com.manager.filemanager.ui.util.advancedShadow
import com.manager.filemanager.ui.util.formatMinSec


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItem(
    media: Media,
    modifier: Modifier = Modifier,
    preloadRequestBuilder: RequestBuilder<Drawable>
) {

    val context = LocalContext.current
    val duration =
        if (media.mimeType.isASpecificTypeOfMime(MimeType.VIDEO_MP4)) media.uri.getVideoDuration(
            context
        ).formatMinSec()
        else null

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(100.dp)
    ) {

        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            model = media.uri,
            contentScale = ContentScale.Crop,
            contentDescription = "Image"){
            it.thumbnail(preloadRequestBuilder)
                .signature(MediaKey(media.id, media.mimeType))
                .format(DecodeFormat.PREFER_RGB_565)
                .override(270)
                .encodeQuality(40)

        }


        AnimatedVisibility(
            visible = duration != null,
            enter = Constants.Animation.enterAnimation,
            exit = Constants.Animation.exitAnimation,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            VideoDurationHeader(duration = duration.toString())
        }
    }

}

@Composable
fun VideoDurationHeader(modifier: Modifier = Modifier, duration: String) {
    Row(
        modifier = modifier
            .padding(all = 8.dp)
            .advancedShadow(
                cornersRadius = 8.dp, shadowBlurRadius = 6.dp, alpha = 0.3f
            ),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
            text = duration,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(2.dp))
        Image(
            modifier = Modifier
                .size(16.dp)
                .advancedShadow(
                    cornersRadius = 2.dp, shadowBlurRadius = 6.dp, alpha = 0.1f, offsetY = (-2).dp
                ),
            imageVector = Icons.Rounded.PlayCircle,
            colorFilter = ColorFilter.tint(color = Color.White),
            contentDescription = "Video"
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.MediaComponent(
    media: Media,
    preloadRequestBuilder: RequestBuilder<Drawable>,
    onItemClick: (Media) -> Unit,
) {

    MediaItem(
        media = media,
        modifier = Modifier
            .animateItemPlacement()
            .combinedClickable(
                onClick = { onItemClick(media) }
            ),
        preloadRequestBuilder = preloadRequestBuilder,
    )
}

@Composable
fun MediaGridView(
    mediaList: List<Media>,
    paddingValues: PaddingValues,
    gridState: LazyGridState = rememberLazyGridState(),
    onMediaClick: (mediaInfo: MediaListInfo) -> Unit = {}

) {

    val preloadingData = rememberGlidePreloadingData(
        data = mediaList,
        preloadImageSize = Size(48f, 48f)
    ) { media: Media, requestBuilder: RequestBuilder<Drawable> ->
        requestBuilder
            .signature(MediaKey(media.id, media.mimeType))
            .encodeQuality(30)
            .load(media.uri)
    }

    @Composable
    fun mediaGrid() {

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            columns = GridCells.Adaptive(100.dp),
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {


            items(
                items = mediaList,
                key = { it.toString() },
                span = { GridItemSpan(1) }
            ) { item ->
                val mediaIndex = mediaList.indexOf(item).coerceAtLeast(0)
                val (media, preloadRequestBuilder) = preloadingData[mediaIndex]
                MediaComponent(
                    media = media,
                    onItemClick = { onMediaClick(mediaList.toMediaListInfo(it)) },
                    preloadRequestBuilder = preloadRequestBuilder
                )
            }
        }
    }
    mediaGrid()
}