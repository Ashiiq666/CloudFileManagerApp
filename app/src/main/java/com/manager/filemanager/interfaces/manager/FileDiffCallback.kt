
package com.manager.filemanager.interfaces.manager

import androidx.recyclerview.widget.DiffUtil
import com.manager.filemanager.manager.adapter.FileModel

class FileDiffCallback(
    private val oldList: List<FileModel>,
    private val newList: List<FileModel>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
