
package com.manager.filemanager.manager.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.manager.filemanager.R
import com.manager.filemanager.interfaces.manager.ItemListener
import com.manager.filemanager.interfaces.settings.PopupSettingsListener
import com.manager.filemanager.interfaces.settings.util.SelectPreferenceUtils
import com.manager.filemanager.manager.util.FileUtils
import java.nio.file.Paths

class CategoryFileModelAdapter(private var listener: ItemListener, private var categoryFileModel: List<CategoryFileModel>, private val mContext: Context) :
    RecyclerView.Adapter<CategoryFileModelAdapter.ViewHolder>() {

    private val fileUtils: FileUtils = FileUtils.getInstance()
    private lateinit var selectPreferenceUtils: SelectPreferenceUtils
    private lateinit var popupSettings: PopupSettingsListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFileModelAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_file_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryFileModelAdapter.ViewHolder, position: Int) {
        val categoryViewFileModel = categoryFileModel[position]
        val path = Paths.get(categoryViewFileModel.path)

        selectPreferenceUtils = SelectPreferenceUtils.getInstance()
        holder.itemIcon.setImageResource(categoryViewFileModel.icon)
        holder.itemTitle.text = categoryViewFileModel.title
        holder.itemCount.text = categoryViewFileModel.count

        holder.itemCategory.setOnClickListener{
            listener.openFileCategory(path, categoryViewFileModel)
        }
    }

    override fun getItemCount(): Int {
        return categoryFileModel.size
    }

    class ViewHolder(itemFileView: View) : RecyclerView.ViewHolder(itemFileView) {
             val itemIcon = itemFileView.findViewById<ImageView>(R.id.ivImage)
             val itemTitle = itemFileView.findViewById<TextView>(R.id.tvCategoryName)
             val itemCategory = itemFileView.findViewById<MaterialCardView>(R.id.cvImages)
             val itemCount = itemFileView.findViewById<TextView>(R.id.tvCount)

    }

}