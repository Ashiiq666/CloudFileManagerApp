package com.etb.filemanager.settings.preference

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.etb.filemanager.R
import com.etb.filemanager.activity.BaseActivity
import com.etb.filemanager.activity.SettingsActivity
import com.etb.filemanager.util.file.style.StyleManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class AppearancePreferences : PreferenceFragment() {

    private lateinit var mCurrentTheme: String


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_appearance, rootKey)
        preferenceManager.preferenceDataStore = SettingsDataStore()

        val themesEntries = requireContext().resources.getStringArray(R.array.themes_entries)
        val themesValues = requireContext().resources.getStringArray(R.array.themes_values)

        mCurrentTheme = Preferences.Appearance.appTheme
        val mCurrentTheIndex = themesValues.indexOf(mCurrentTheme)

        val appThemeSummary = themesEntries[mCurrentTheIndex]

        //App theme
        val appTheme =
            findPreference<Preference>("app_theme")
                ?: throw IllegalArgumentException("Preference not found: app_theme")

        appTheme.summary = appThemeSummary
        appTheme.setOnPreferenceClickListener { preference ->
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.themes_title))
                .setSingleChoiceItems(themesEntries, mCurrentTheIndex) { dialog, which ->
                    if (which != mCurrentTheIndex) {
                        val theme = StyleManager.OptionStyle.valueOf(themesValues[which])
                        Preferences.Appearance.appTheme = theme.name
                        (activity as SettingsActivity).restart()

                    }

                    dialog.cancel()
                }

                .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which ->
                }.show()
            true
        }


    }

    override fun getTitle(): Int {
        return R.string.pref_cat_appearance
    }

}



