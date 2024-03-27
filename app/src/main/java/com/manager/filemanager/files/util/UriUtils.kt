package com.manager.filemanager.files.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlin.io.path.pathString
import android.media.MediaMetadataRetriever

fun Uri.isFromApps(): Boolean =
    scheme.toString() == "content" && toString().contains("Android/")

fun Context.uriToPath(uri: Uri?): String? {
    if (uri == null) return null
    val proj = arrayOf(MediaStore.MediaColumns.DATA)
    var path: String? = null
    val cursor: Cursor? = contentResolver.query(uri, proj, null, null, null)
    if (cursor != null && cursor.count != 0) {
        cursor.moveToFirst()
        path = try {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.getString(columnIndex)
        } catch (_: IllegalArgumentException) {
            null
        }
    }
    cursor?.close()
    return path ?: uri.fileProviderPath.pathString
}

fun Uri.getVideoDuration(context: Context): Long {
    val retriever = MediaMetadataRetriever()

    return try {
        retriever.setDataSource(context, this)
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        durationStr?.toLongOrNull() ?: 0
    } catch (e: Exception) {
        0

    }
}
