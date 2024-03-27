
package com.manager.filemanager.settings.preference;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDataStore;


public class SettingsDataStore extends PreferenceDataStore {
    private final AppPreference mAppPref;

    public SettingsDataStore() {
        super();
        mAppPref = AppPreference.getInstance();
    }

    @Override
    public void putString(String key, @Nullable String value) {
        mAppPref.setPref(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        mAppPref.setPref(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        mAppPref.setPref(key, value);
    }

    @Override
    public void putFloat(String key, float value) {
        mAppPref.setPref(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mAppPref.setPref(key, value);
    }

    @NonNull
    @Override
    public String getString(String key, @Nullable String defValue) {
        return (String) mAppPref.get(key);
    }

    @Override
    public int getInt(String key, int defValue) {
        return (int) mAppPref.get(key);
    }

    @Override
    public long getLong(String key, long defValue) {
        return (long) mAppPref.get(key);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return (float) mAppPref.get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return (boolean) mAppPref.get(key);
    }
}