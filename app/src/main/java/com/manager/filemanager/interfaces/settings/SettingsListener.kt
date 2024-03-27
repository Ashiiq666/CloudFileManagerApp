
package com.manager.filemanager.interfaces.settings

import android.content.SharedPreferences

interface SettingsListener {


    fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String)

}