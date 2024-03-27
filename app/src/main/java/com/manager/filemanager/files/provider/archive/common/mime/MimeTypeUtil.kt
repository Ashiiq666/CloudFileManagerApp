package com.manager.filemanager.files.provider.archive.common.mime


class MimeTypeUtil {


    fun getIconByMimeType(mimeType: String): Int {
        val mimeTypeObj = MimeType(mimeType)

        val icon: MimeTypeIcon = mimeTypeObj.icon
        val iconResourceId: Int = icon.resourceId
        return iconResourceId


    }


    fun isSpecificFileType(mimeType: String, type: MimeTypeIcon): Boolean {
        val iconMimeType = getIconByMimeType(mimeType)

        return iconMimeType == type.resourceId
    }
}
  fun MimeType.isSpecificMimeType(type: MimeTypeIcon): Boolean{
      val iconMimeType = MimeTypeUtil().getIconByMimeType(subtype)
      return iconMimeType == type.resourceId
  }