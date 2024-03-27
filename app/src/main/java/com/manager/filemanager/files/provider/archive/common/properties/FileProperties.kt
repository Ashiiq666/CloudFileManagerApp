
package com.manager.filemanager.files.provider.archive.common.properties

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.manager.filemanager.files.provider.archive.common.mime.MediaType


data class FileProperties(var title: String,
                          var property: String,
                          var isMedia: Boolean = false,
                          var mediaType: MediaType = MediaType.VIDEO,
                          var mediaPath: String = "") : Parcelable {
    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean(),
        parcel.readSerializable() as MediaType,
        parcel.readString() ?: "",
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(property)
    }

    companion object CREATOR : Parcelable.Creator<FileProperties> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): FileProperties {
            return FileProperties(parcel)
        }

        override fun newArray(size: Int): Array<FileProperties?> {
            return arrayOfNulls(size)
        }
    }
}
