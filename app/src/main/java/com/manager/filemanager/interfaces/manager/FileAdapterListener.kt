
package com.manager.filemanager.interfaces.manager

import com.manager.filemanager.manager.adapter.FileModel

interface FileAdapterListener {

    fun onLongClickListener(item: FileModel, isActionMode: Boolean)

    fun onItemClick(item: FileModel, path: String, isDirectory: Boolean)
}