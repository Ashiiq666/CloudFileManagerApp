package com.manager.filemanager.files.extensions

import com.manager.filemanager.manager.adapter.FileModel
import com.manager.filemanager.manager.files.filelist.FileSortOptions
import com.manager.filemanager.settings.preference.Preferences


fun List<FileModel>.sortFileModel(): List<FileModel> {
    val fileSortOptions = FileSortOptions(
        Preferences.Popup.sortBy,
        Preferences.Popup.orderFiles,
        Preferences.Popup.isDirectoriesFirst
    )
    return this.sortedWith(fileSortOptions.createComparator())
}
