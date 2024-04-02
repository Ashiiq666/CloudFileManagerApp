
package com.manager.filemanager.manager.adapter

import android.os.Parcelable
import androidx.annotation.WorkerThread
import com.manager.filemanager.files.util.ParcelableParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.io.File
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.isHidden
import kotlin.io.path.name
import kotlin.io.path.pathString

@Parcelize
 class FileModel(
    var id: Long,
    var fileName: String,
    var filePath: @WriteWith<ParcelableParceler> String,
    var isDirectory: Boolean,
    var fileExtension: String,
    var fileSize: Long,
    var file: File,
    var isHidden: Boolean,
    var absolutePath: String,

    ) : Parcelable

@WorkerThread
fun Path.loadFileItem(): FileModel {
    val file = toFile()
    val fileName = name
    val filePath = pathString
    val isDirectory = isDirectory()
    val fileExtension = extension
    val fileSize = fileSize()
    val isHidden = isHidden()
    val absolutePath = toAbsolutePath().toString()


    return FileModel(
        UUID.randomUUID().mostSignificantBits,
        fileName,
        filePath,
        isDirectory,
        fileExtension,
        fileSize,
        file,
        isHidden,
        absolutePath
    )
}