
package com.manager.filemanager.compose.feature.provider

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.manager.filemanager.files.util.LocaleContextWrapper
import com.manager.filemanager.settings.preference.Preferences
import com.manager.filemanager.ui.style.StyleManager

abstract class BaseScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)

    }

    private fun applyTheme() {
        val styleManager = StyleManager()
        val optionStyle = StyleManager.OptionStyle.valueOf(Preferences.Appearance.appTheme)
        styleManager.setTheme(optionStyle, this)
    }

    override fun attachBaseContext(newBase: Context) {
        val context = LocaleContextWrapper.wrap(newBase)
        super.attachBaseContext(context)
    }
}
