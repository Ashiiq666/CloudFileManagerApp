package com.manager.filemanager.compose.core.models

import kotlinx.serialization.Serializable

@Serializable
data class FileOperationResult(val name: String, val desc: String, val result: OperationResult)

enum class OperationResult(val value: String){
    SUCCESS("Success"),
    FAILED("Failed")
}
