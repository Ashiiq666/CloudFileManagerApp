package com.manager.filemanager.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manager.filemanager.files.util.Args
import com.manager.filemanager.files.util.LocaleContextWrapper
import com.manager.filemanager.files.util.putArgs
import com.manager.filemanager.settings.preference.Preferences
import com.manager.filemanager.ui.style.StyleManager

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)

    }

    private fun applyTheme() {
        val styleManager = StyleManager()
        val optionStyle = StyleManager.OptionStyle.valueOf(Preferences.Appearance.appTheme)
        styleManager.setTheme(optionStyle, this)
    }

    @Suppress("DEPRECATION")
    fun applyConfigurationChangesToActivities(savedInstanceState: Bundle) {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putArgs(Args(savedInstanceState))
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finishAffinity()
    }
    override fun attachBaseContext(newBase: Context) {
        val context = LocaleContextWrapper.wrap(newBase)
        super.attachBaseContext(context)
    }
}
