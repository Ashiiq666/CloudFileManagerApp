
package com.manager.filemanager.compose.feature.presentation.categorylist.apklist.components

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manager.filemanager.files.extensions.AppFilter
import com.manager.filemanager.files.extensions.AppInfo
import com.manager.filemanager.files.extensions.getInstalledApkInfo
import com.manager.filemanager.files.extensions.getUninstalledApkInfo
import kotlinx.coroutines.launch

class ApkListViewModel : ViewModel() {
    private val _apkListState = MutableLiveData<List<AppInfo>>()
    val apkListState: LiveData <List<AppInfo>> = _apkListState

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    fun loadApkList(context: Context, appFilter: AppFilter){
        viewModelScope.launch {
            val apkList = if (appFilter == AppFilter.UNINSTALLED_INTERNAL){
                getUninstalledApkInfo(context)
            } else{
                getInstalledApkInfo(context, appFilter)
            }
            _apkListState.value = apkList
            _loading.value = false
        }
    }

    fun update(context: Context, appFilter: AppFilter){
        _loading.value = true
        loadApkList(context, appFilter)
    }
}