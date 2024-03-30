
package com.manager.filemanager.files.provider.archive.common.mime
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.webkit.MimeTypeMap
import com.manager.filemanager.R

class MimeTypeHelper(private val context: Context) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getIconForMimeType(mimeType: String): Drawable? {
        val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val resourceId = getDrawableResourceId(fileExtension)
        return if (resourceId != 0) context.getDrawable(resourceId) else null
    }

    private fun getDrawableResourceId(fileExtension: String?): Int {
        return when (fileExtension) {
            "apk" -> R.drawable.file_apk_icon
            "jpg", "jpeg", "png", "gif" -> R.drawable.ic_image_category
            "mp4", "mkv", "avi", "webm" -> R.drawable.ic_video_category
            "mp3", "wav", "flac", "ogg" -> R.drawable.ic_music_category
            "zip", "rar", "7z", "tar", "gz" -> R.drawable.ic_doc_category

            // Adicione outros MIME types e seus respectivos ícones aqui
            else -> 0
        }
    }
}
