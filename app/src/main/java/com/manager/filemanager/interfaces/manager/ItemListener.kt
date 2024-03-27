
package com.manager.filemanager.interfaces.manager

import com.manager.filemanager.manager.category.adapter.CategoryFileModel
import java.nio.file.Path

interface ItemListener {

    fun openFileCategory(path: Path, categoryFileModel: CategoryFileModel)
    fun refreshItem()
    fun openItemWith(path: Path)

}