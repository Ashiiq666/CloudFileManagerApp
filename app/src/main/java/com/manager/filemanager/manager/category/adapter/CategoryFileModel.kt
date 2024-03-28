package com.manager.filemanager.manager.category.adapter

import android.content.Context
import android.os.Environment
import android.os.Parcelable
import com.manager.filemanager.R
import com.manager.filemanager.settings.preference.Preferences
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryFileModel(val icon: Int, val title: String, val path: String, val category: Category = Category.DOCUMENTS) :
    Parcelable


enum class Category(){
    IMAGE,
    AUDIOS,
    VIDEOS,
    DOWNLOADS,
    APK,
    DOCUMENTS,

}
fun getCategories(context: Context): ArrayList<CategoryFileModel> {
    val listCategoryName = Preferences.Behavior.categoryNameList
    val listCategoryPath = Preferences.Behavior.categoryPathList

    val imagePath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
    val audioPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath

    val videoPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath
    val downloadsPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

    val documentsPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath

    val apkPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath



    val categoryFileModels = ArrayList<CategoryFileModel>()
    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_image_category, context.getString(R.string.images), imagePath, Category.IMAGE
        )
    )
    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_music_category, context.getString(R.string.audios), audioPath, Category.AUDIOS
        )
    )
    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_video_category, context.getString(R.string.videos), videoPath, Category.VIDEOS
        )
    )
    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_download_category, context.getString(R.string.downloads), downloadsPath, Category.DOWNLOADS
        )
    )

    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_apk_category, context.getString(R.string.apk), apkPath, Category.APK
        )
    )
    categoryFileModels.add(
        CategoryFileModel(
            R.drawable.ic_doc_category, context.getString(R.string.documents), documentsPath, Category.DOCUMENTS)
    )
    if (listCategoryName.isNotEmpty()) {
        for ((index, name) in listCategoryName.withIndex()) {
            val mName = name
            val mPath = listCategoryPath[index]
            categoryFileModels.add(CategoryFileModel(R.drawable.ic_folder, mName, mPath))
        }
    }

    return categoryFileModels
}

fun Category?.getName(context: Context): String{
    return when(this){
        Category.IMAGE -> context.getString(R.string.images)
        Category.VIDEOS -> context.getString(R.string.videos)
        Category.AUDIOS -> context.getString(R.string.songs)
        Category.DOCUMENTS -> context.getString(R.string.files)
        Category.APK -> context.getString(R.string.apps)
        else ->  context.getString(R.string.files)
    }
}