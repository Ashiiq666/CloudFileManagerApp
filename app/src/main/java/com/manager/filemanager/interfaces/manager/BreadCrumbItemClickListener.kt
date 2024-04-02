
package com.manager.filemanager.interfaces.manager

import com.manager.filemanager.data.di.BreadItem

interface BreadCrumbItemClickListener {
    fun onItemClick(
        item: BreadItem,
        position: Int,
    )
}