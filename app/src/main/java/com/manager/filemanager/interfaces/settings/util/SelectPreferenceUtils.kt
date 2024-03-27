
package com.manager.filemanager.interfaces.settings.util

import com.manager.filemanager.interfaces.settings.PopupSettingsListener

class SelectPreferenceUtils {
    private lateinit var listener: PopupSettingsListener

    fun addItemSelectedOnListener(itemSelected: Int, itemSelectedFolderFirst: Boolean) {

        listener.onItemSelectedActionSort(itemSelected, itemSelectedFolderFirst)

    }
    companion object {
        private var instance: SelectPreferenceUtils? = null

        fun getInstance(): SelectPreferenceUtils {
            if (instance == null) {
                instance = SelectPreferenceUtils()
            }
            return instance!!
        }
    }

}