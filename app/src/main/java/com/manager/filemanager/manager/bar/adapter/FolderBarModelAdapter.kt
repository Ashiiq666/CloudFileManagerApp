package com.manager.filemanager.manager.bar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manager.filemanager.R
import com.manager.filemanager.interfaces.settings.PopupSettingsListener
import com.manager.filemanager.interfaces.settings.util.SelectPreferenceUtils
import com.manager.filemanager.files.util.FileUtil


class FolderBarModelAdapter(private var folderBarModel: List<FolderBarModel>, private val mContext: Context) :
    RecyclerView.Adapter<FolderBarModelAdapter.ViewHolder>() {

    private val fileUtil = FileUtil()
    private lateinit var selectPreferenceUtils: SelectPreferenceUtils
    private lateinit var popupSettings: PopupSettingsListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderBarModelAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_file_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderBarModelAdapter.ViewHolder, position: Int) {
        val folderViewModel = folderBarModel[position]
        var folderTitle = fileUtil.getFileAndFolderName(FileUtil.TypeFile.FOLDER, folderViewModel.path)
        holder.itemTitle.text = folderTitle


    }

    override fun getItemCount(): Int {
        return folderBarModel.size
    }

    class ViewHolder(itemBarView: View) : RecyclerView.ViewHolder(itemBarView) {
        val itemTitle = itemBarView.findViewById<TextView>(R.id.item_title)
        val itemBase = itemBarView.findViewById<LinearLayout>(R.id.item_base)

    }

}