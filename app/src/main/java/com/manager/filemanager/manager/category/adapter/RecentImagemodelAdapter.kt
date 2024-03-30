
package com.manager.filemanager.manager.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.manager.filemanager.R
import com.manager.filemanager.interfaces.manager.ItemListener
import java.nio.file.Paths

class RecentImagemodelAdapter(private var itemListener: ItemListener, private var recentImageModel: List<RecentImageModel>, private val mContext: Context) :
    RecyclerView.Adapter<RecentImagemodelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentImagemodelAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_image_item, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecentImagemodelAdapter.ViewHolder, position: Int) {
        val recentImageViewModel = recentImageModel[position]
        val imagePath = recentImageViewModel.imagePath
        val imageTitle = recentImageViewModel.imageTitle
        val imageTime = recentImageViewModel.imageTime
        val mPath = Paths.get(imagePath)

        Glide.with(mContext)
            .load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_category))
            .into(holder.itemImage)

        holder.itemImage.setOnClickListener { itemListener.openItemWith(mPath) }
        holder.imageTitle.text = imageTitle
        holder.imageTime.text = imageTime

    }

    override fun getItemCount(): Int {
        return recentImageModel.size
    }

    class ViewHolder(itemImageView: View) : RecyclerView.ViewHolder(itemImageView) {
        val itemImage = itemImageView.findViewById<ShapeableImageView>(R.id.ivRecentImageItem)
        val imageTitle = itemImageView.findViewById<TextView>(R.id.tvRecentImageTitle)
        val imageTime = itemImageView.findViewById<TextView>(R.id.tvRecentImageTime)


    }

}