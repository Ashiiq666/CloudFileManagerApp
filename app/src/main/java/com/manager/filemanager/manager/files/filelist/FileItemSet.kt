

package com.manager.filemanager.manager.files.filelist

import android.os.Parcel
import android.os.Parcelable

import com.manager.filemanager.manager.adapter.FileModel
import com.manager.filemanager.files.compat.writeParcelableListCompat
import com.manager.filemanager.files.util.LinkedMapSet
import com.manager.filemanager.files.util.readParcelableListCompat

class FileItemSet() : LinkedMapSet<String, FileModel>(FileModel::filePath), Parcelable {
    constructor(parcel: Parcel) : this() {
        addAll(parcel.readParcelableListCompat())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelableListCompat(toList(), flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FileItemSet> {
        override fun createFromParcel(parcel: Parcel): FileItemSet = FileItemSet(parcel)

        override fun newArray(size: Int): Array<FileItemSet?> = arrayOfNulls(size)
    }
}

fun fileItemSetOf(vararg files: FileModel) = FileItemSet().apply { addAll(files) }