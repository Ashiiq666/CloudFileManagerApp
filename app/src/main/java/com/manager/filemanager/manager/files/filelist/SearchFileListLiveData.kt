

package com.manager.filemanager.manager.files.filelist

import android.os.AsyncTask
import android.util.Log
import com.manager.filemanager.files.provider.archive.common.search
import com.manager.filemanager.files.util.CloseableLiveData
import com.manager.filemanager.files.util.Failure
import com.manager.filemanager.files.util.Loading
import com.manager.filemanager.files.util.Stateful
import com.manager.filemanager.files.util.Success
import com.manager.filemanager.manager.adapter.FileModel
import com.manager.filemanager.manager.adapter.loadFileItem
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SearchFileListLiveData(
    private val path: Path,
    private val query: String
) : CloseableLiveData<Stateful<List<FileModel>>>() {
    private var future: Future<Unit>? = null

    init {
        loadValue()
    }

    fun loadValue() {
        future?.cancel(true)
        value = Loading(emptyList())
        future = (AsyncTask.THREAD_POOL_EXECUTOR as ExecutorService).submit<Unit> {
            val fileList = mutableListOf<FileModel>()
            try {
                path.search(query, INTERVAL_MILLIS) { paths: List<Path> ->
                    for (path in paths) {
                        val fileItem = try {
                            path.loadFileItem()
                        } catch (e: IOException) {
                            Log.e("WEER", "ERRO: $e")

                            e.printStackTrace()
                            continue
                        }
                        fileList.add(fileItem)
                    }
                    postValue(Loading(fileList.toList()))
                }
                postValue(Success(fileList))
            } catch (e: Exception) {
                Log.e("WEER", "ERRO: $e")
                Failure(value, e)
            }
            Log.i("RESULTS", "Results: $fileList")
        }
    }

    override fun close() {
        future?.cancel(true)
    }

    companion object {
        private const val INTERVAL_MILLIS = 500L
    }
}
class SearchState(val isSearching: Boolean, val query: String)
