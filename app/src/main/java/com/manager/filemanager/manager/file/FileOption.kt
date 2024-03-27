package com.manager.filemanager.manager.file

 class FileAction(var icon: Int, var title: String, var action: CreateFileAction){
 }

enum class CreateFileAction{
    OPEN_WITH,
    SELECT,
    EXTRACT,
    CUT,
    COPY,
    DELETE,
    RENAME,
    COMPRESS,
    SHARE,
    COPY_PATH,
    PROPERTIES
}



