
package com.manager.filemanager.manager.media.model

import android.os.Parcelable
import com.bumptech.glide.load.Key
import com.manager.filemanager.files.provider.archive.common.mime.MimeType
import kotlinx.parcelize.Parcelize
import java.nio.ByteBuffer
import java.security.MessageDigest

@Parcelize
data class MediaKey(
    val id: Long = 0,
    val mimeType: MimeType
): Key, Parcelable {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val data = ByteBuffer.allocate(20)
            .putLong(id)
        messageDigest.update(data)
        messageDigest.update(mimeType.value.toByteArray(Key.CHARSET))
    }
}