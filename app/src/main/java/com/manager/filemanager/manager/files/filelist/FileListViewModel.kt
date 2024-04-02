
package com.manager.filemanager.manager.files.filelist

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.manager.filemanager.files.app.application
import com.manager.filemanager.files.provider.archive.common.FilePathProvider
import com.manager.filemanager.files.util.CloseableLiveData
import com.manager.filemanager.files.util.ContextUtils.context
import com.manager.filemanager.files.util.RootPath
import com.manager.filemanager.files.util.Stateful
import com.manager.filemanager.manager.adapter.FileModel
import com.manager.filemanager.settings.preference.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import java.io.Closeable
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.io.path.isDirectory


class FileListViewModel : ViewModel() {

    private val TAG = "FileViewModel"
    private val directoryList: MutableList<String> = mutableListOf()

    var firstInit = false
    private var fileListJob: Job? = null

    private val trailLiveData = TrailLiveData()
    val hasTrail: Boolean
        get() = trailLiveData.value != null
    val pendingState: Parcelable?
        get() = trailLiveData.value?.pendigSate




    private val _selectedFilesLiveData = MutableLiveData(fileItemSetOf())
    val selectedFilesLiveData: LiveData<FileItemSet>
        get() = _selectedFilesLiveData
    val selectedFiles: FileItemSet
        get() = _selectedFilesLiveData.value!!


    fun updateDirectoryList(path: String) {
        if (!directoryList.contains(path)) {
            directoryList.add(path)
        }
    }


    fun updateDirectoryListWithPos(position: Int) {
        if (position >= 0 && position < directoryList.size) {
            directoryList.subList(position + 1, directoryList.size).clear()
        }
    }

    fun getStoragePath(rootPath: RootPath): String {
        val filePathProvider = FilePathProvider(application)
        return when (rootPath) {
            RootPath.INTERNAL -> filePathProvider.internalStorageRootPath
            RootPath.EXTERNAL -> filePathProvider.externalStorageRootPath
        }
    }


    fun getDirectoryList(directoryPath: String): List<Path> {
        val directory = Paths.get(directoryPath)
        return Files.list(directory).filter { it.isDirectory() && Files.isReadable(it) }
            .collect(Collectors.toList())
    }



    fun selectFile(file: FileModel, selected: Boolean) {
        selectFiles(fileItemSetOf(file), selected)
    }

    fun selectFiles(files: FileItemSet, selected: Boolean) {
        val selectedFiles = _selectedFilesLiveData.value
        if (selectedFiles === files) {
            if (!selected && selectedFiles.isNotEmpty()) {
                selectedFiles.clear()
            }
            return
        }
        var changed = false
        for (file in files) {
            changed = changed or if (selected) {
                selectedFiles!!.add(file)
            } else {
                selectedFiles!!.remove(file)
            }
        }
        if (changed) {
            _selectedFilesLiveData.postValue(selectedFiles)
        }
    }

    fun clearSelectedFiles() {
        val selectedFiles = _selectedFilesLiveData.value
        if (selectedFiles!!.isEmpty()) {
            return
        }
        selectedFiles.clear()
        _selectedFilesLiveData.postValue(selectedFiles)
    }

    fun replaceSelectedFiles(files: FileItemSet) {
        val selectedFiles = _selectedFilesLiveData.value
        if (selectedFiles == files) {
            return
        }
        selectedFiles?.clear()
        selectedFiles?.addAll(files)
        _selectedFilesLiveData.value = selectedFiles

    }

    private val _pickOptionsLiveData = MutableLiveData<PickOptions?>()
    val pickOptionsLiveData: LiveData<PickOptions?>
        get() = _pickOptionsLiveData
    var pickOptions: PickOptions?
        get() = _pickOptionsLiveData.value
        set(value) {
            _pickOptionsLiveData.value = value
        }


    fun navigateTo(lastState: Parcelable, path: Path) = trailLiveData.navigateTo(lastState, path)
    fun resetTo(path: Path) = trailLiveData.resetTo(path)
    fun navigateUp(): Boolean {
        val currentPathValue = currentPath ?: return false
        val defaultFolderPath = Paths.get(Preferences.Behavior.defaultFolder).toString()
        return if (currentPathValue != defaultFolderPath) {
            trailLiveData.navigateUp()
        } else {
            false
        }
    }

    val currentPathLiveData = trailLiveData.map { it.currentPath }

   var currentPath: String = ""
       get() = currentPathLiveData.value.toString()!!

    private val _currentPathLiveData = MutableLiveData<String>()
    val currentPathLive: LiveData<String>
        get() = _currentPathLiveData

    private val _searchStateLiveData = MutableLiveData(SearchState(false, ""))
    val searchState: SearchState
        get() = _searchStateLiveData.value!!

    fun search(query: String){
        val searchState = _searchStateLiveData.value!!
        if (searchState.isSearching && searchState.query == query) return
        _searchStateLiveData.value = SearchState(true, query)
    }

    fun stopSearching(){
        val searchState = _searchStateLiveData.value!!
        if (!searchState.isSearching) return
        _searchStateLiveData.value = SearchState(false, "")
    }


    private val _fileListLiveData = FileListSwitchMapLiveData(currentPathLiveData, _searchStateLiveData)
    val fileListLiveData: LiveData<Stateful<List<FileModel>>>
        get() = _fileListLiveData
    val fileListStateful: Stateful<List<FileModel>>
        get() = _fileListLiveData.value!!

    fun reload() {
        _fileListLiveData.reload()
    }


    fun initFirstList() {
        // Initialize with the root directory
        val rootDirectory = context.filesDir.absolutePath
        _currentPathLiveData.value = rootDirectory
    }

    val searchViewExpandedLiveData = MutableLiveData(false)
    private val _searchViewQueryLiveData = MutableLiveData("")

    private class FileListSwitchMapLiveData(
        private val pathLiveData: LiveData<Path>,
        private val searchStateLiveData: LiveData<SearchState>
    ) : MediatorLiveData<Stateful<List<FileModel>>>(), Closeable {
        private var liveData: CloseableLiveData<Stateful<List<FileModel>>>? = null

        init {
            addSource(pathLiveData){updateSource()}
            addSource(searchStateLiveData){updateSource()}
        }

        private fun updateSource() {
            liveData?.let {
                removeSource(it)
                it.close()
            }
            val path = pathLiveData.value
            val searchState = searchStateLiveData.value!!
            val liveData = if (searchState.isSearching) SearchFileListLiveData(path!!, searchState.query) else FileListLiveData(path!!)


            this.liveData = liveData
            addSource(liveData) { value = it }
        }

        fun reload() {
            when (val liveData = liveData) {
                is FileListLiveData -> liveData.loadValue()
                is SearchFileListLiveData -> liveData.loadValue()
            }
        }

        override fun close() {
            liveData?.let {
                removeSource(it)
                it.close()
                this.liveData = null
            }
        }
    }

    private val _sortOptionsLiveData = MutableLiveData<Unit>()
    private val _showHiddenFilesLiveData = MutableLiveData<Unit>()
    private val _toggleGridLiveData = MutableLiveData<Boolean>()
    val showHiddenFilesLiveData: LiveData<Unit> = _showHiddenFilesLiveData
    val sortOptionsLiveData: LiveData<Unit> = _sortOptionsLiveData
    val toggleGridLiveData: LiveData<Boolean> = _toggleGridLiveData

    fun setSortBy(sortBy: FileSortOptions.SortBy) {
        Preferences.Popup.sortBy = sortBy
        _sortOptionsLiveData.value = Unit

    }

    fun setOrderFiles() {
        val currentOrder = Preferences.Popup.orderFiles
        val newOrder = if (currentOrder == FileSortOptions.Order.ASCENDING) FileSortOptions.Order.DESCENDING else FileSortOptions.Order.ASCENDING
        Preferences.Popup.orderFiles = newOrder
        _sortOptionsLiveData.value = Unit
    }
    fun setDirectoriesFirst() {
        Preferences.Popup.isDirectoriesFirst = !Preferences.Popup.isDirectoriesFirst
        _sortOptionsLiveData.value = Unit
    }

    fun setShowHiddenFiles(value: Boolean){
        Preferences.Popup.showHiddenFiles = value
        _showHiddenFilesLiveData.value = Unit

    }

    fun setGriToggle(){
        val isGridEnabled = !Preferences.Popup.isGridEnabled
        Preferences.Popup.isGridEnabled = isGridEnabled
        _toggleGridLiveData.value = isGridEnabled
    }


}

enum class TypeOperation() {
    CUT,
    // COPY,
}


