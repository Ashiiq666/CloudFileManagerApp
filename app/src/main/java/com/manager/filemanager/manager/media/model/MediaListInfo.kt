package com.manager.filemanager.manager.media.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListInfo(
    val mediaList: List<Media>,
    val currentMedia: Media
): Parcelable

fun List<Media>.toMediaListInfo(currentMedia: Media) =
    MediaListInfo(this, currentMedia)
