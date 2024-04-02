
package com.manager.filemanager.manager.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.manager.filemanager.R
import com.manager.filemanager.databinding.FileItemViewBinding
import com.manager.filemanager.files.provider.archive.common.mime.MimeTypeUtil
import com.manager.filemanager.files.util.FileUtil
import com.manager.filemanager.files.util.getColorByAttr
import com.manager.filemanager.files.util.getDimension
import com.manager.filemanager.files.util.getDimensionPixelSize
import com.manager.filemanager.files.util.layoutInflater
import com.manager.filemanager.interfaces.manager.FileDiffCallback
import com.manager.filemanager.interfaces.manager.FileListener
import com.manager.filemanager.interfaces.settings.util.click
import com.manager.filemanager.manager.files.filelist.FileItemSet
import com.manager.filemanager.manager.files.filelist.PickOptions
import com.manager.filemanager.manager.files.filelist.fileItemSetOf
import com.manager.filemanager.manager.files.ui.AnimatedListAdapter
import com.manager.filemanager.manager.files.ui.CheckableItemBackground
import com.manager.filemanager.manager.util.FileUtils
import com.manager.filemanager.settings.preference.Preferences
import com.manager.filemanager.ui.style.ColorUtil
import me.zhanghai.android.fastscroll.PopupTextProvider
import java.util.*


class FileModelAdapter(
    private val listener: FileListener,
    private val mContext: Context,
    private val onClick: ((FileModel) -> Unit)? = null,

) : AnimatedListAdapter<FileModel, FileModelAdapter.ViewHolder>(CALLBACK), PopupTextProvider {
    private var directoryItems: List<FileModel> = emptyList()
    private val basePath = "/storage/emulated/0"
    private var currentPath = basePath


    private val defaultComparator: Comparator<FileModel> =
        compareBy<FileModel> { it.fileName }.thenBy { it.fileName }
    private lateinit var _comparator: Comparator<FileModel>
    private var isSearching = false
    var comparator: Comparator<FileModel>
        get() {
            if (!::_comparator.isInitialized) {
                _comparator = defaultComparator
            }
            return _comparator
        }
        set(value) {
            _comparator = value
            if (!isSearching) {
                super.replace(list.sortedWith(value), true)
                rebuildFilePositionMap()
            }
        }

    private var pickOptions: PickOptions? = null
        set(value) {
            field = value
            notifyItemRangeChanged(0, itemCount, PAYLOAD_STATE_CHANGED)
        }

    private val selectedFiles = fileItemSetOf()
    private val filePositionMap = mutableMapOf<String, Int>()


    fun replaceSelectedFiles(files: FileItemSet) {
        val changedFiles = fileItemSetOf()
        val iterator = selectedFiles.iterator()
        while (iterator.hasNext()) {
            val file = iterator.next()
            if (file !in files) {
                iterator.remove()
                changedFiles.add(file)
            }
        }
        for (file in files) {
            if (file !in selectedFiles) {
                selectedFiles.add(file)
                changedFiles.add(file)
            }
        }
        for (file in changedFiles) {

            val position = filePositionMap[file.filePath]
            position?.let { notifyItemChanged(it, PAYLOAD_STATE_CHANGED) }
        }

    }

    private fun selectFile(file: FileModel) {
        if (!isFileSelectable(file)) {
            return
        }
        val selected = file in selectedFiles
        val pickOptions = pickOptions
        if (!selected && pickOptions != null && !pickOptions.allowMultiple) {
            listener.clearSelectedFiles()

        }
        listener.selectFile(file, !selected)
    }

    private fun isFileSelectable(file: FileModel): Boolean {
        val pickOptions = pickOptions ?: return true
        return if (pickOptions.pickDirectory) {
            file.isDirectory
        } else {
            !file.isDirectory
        }
    }

    fun selectAllFiles() {

        val files = fileItemSetOf()
        for (index in 0 until itemCount) {
            val file = getItem(index)
            if (isFileSelectable(file)) {
                files.add(file)
            }
        }
        listener.selectFiles(files, true)
    }


    @Deprecated("", ReplaceWith("replaceList(list)"))
    override fun replace(list: List<FileModel>, clear: Boolean) {
        throw UnsupportedOperationException()
    }


    fun replaceList(list: List<FileModel>, isSearching: Boolean) {
        val clear = this.isSearching != isSearching
        this.isSearching = isSearching
        super.replace(if (!isSearching) list.sortedWith(comparator) else list, clear)
        rebuildFilePositionMap()
    }

    private fun rebuildFilePositionMap() {
        filePositionMap.clear()
        for (index in 0 until itemCount) {
            val file = getItem(index)
            filePositionMap[file.filePath] = index
        }
    }

    private fun applyStyle(binding: FileItemViewBinding) {
        if (Preferences.Interface.isEnabledRoundedCorners) {
            mContext.let { ctx ->
                binding.itemFile.radius = ctx.getDimension(R.dimen.corner_radius_base)
                binding.layoutBase.layoutParams = binding.layoutBase.layoutParams.apply {
                    if (this is ViewGroup.MarginLayoutParams) {
                        val padding = ctx.getDimensionPixelSize(R.dimen.spacing_tiny)
                        setMargins(padding, padding, padding, padding)
                    }
                }
            }
        }
    }


    fun setItems(newItems: List<FileModel>) {
        val diffResult = DiffUtil.calculateDiff(FileDiffCallback(directoryItems, newItems))
        directoryItems = newItems
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        FileItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
    ).apply {
        applyStyle(binding)
        binding.itemFile.background = CheckableItemBackground.create(binding.itemFile.context)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        bindViewHolderAnimation(holder)
        val file = getItem(position)
        val binding = holder.binding
        val filePath = file.filePath
        val mimeType = FileUtil().getMimeType(null, filePath)
        val selected = file in selectedFiles
        val isDirectory = file.isDirectory
        val itemCount = FileUtil().getItemCountOfFolder(file.filePath) // Get item count of the folder
        if (file.isDirectory) {
            // If it's a folder, display the item count
            val itemCountText = if (itemCount <= 1) {
                "$itemCount item"
            } else {
                "$itemCount items"
            }
            binding.fileSize.text = itemCountText
        } else {
            // If it's a file, display the file size
            binding.fileSize.text = FileUtils().getFileSizeFormatted(file.fileSize)
        }




        currentPath = file.filePath
        binding.itemFile.isChecked = selected
        if (payloads.isNotEmpty()) {
            return
        }

        setVisibility(isDirectory, mimeType, binding)
        if (!isDirectory) {
            if (mimeType != null && mimeType.isMimeTypeMedia()) {
                loadImage(filePath, binding)

            } else {
                getIconByMimeType(mimeType, binding)

            }

        } else loadImageFromDirectory(binding)


        binding.fileTitle.text = file.fileName
        binding.fileDate.text = FileUtils().getFormatDateFile(filePath, true)

        binding.itemFile.setOnClickListener {
            if (selectedFiles.isEmpty()) {
                listener.openFile(file)

            } else {
                selectFile(file)
            }
        }
        binding.itemFile.setOnLongClickListener {
            if (Preferences.Behavior.selectFileLongClick) {
                if (selectedFiles.isEmpty()) {
                    selectFile(file)
                } else {
                    listener.openFile(file)
                }
            } else {
                listener.showBottomSheet(file)
            }

            true
        }
        binding.iconFile.setOnClickListener { selectFile(file) }

        binding.root.click {
            val position = holder.adapterPosition
            onClick?.invoke(directoryItems[position])
        }
    }

    private fun setVisibility(isDir: Boolean, mimeType: String?, binding: FileItemViewBinding) {
        val isMedia = mimeType?.isMimeTypeMedia() ?: false
        val viewFileInformationOption = Preferences.Interface.viewFileInformationOption
        val colorPrimary = mContext.getColorByAttr(com.google.android.material.R.attr.colorPrimary)
        val colorOnPrimary =
            mContext.getColorByAttr(com.google.android.material.R.attr.colorOnPrimary)
        val iconFile = binding.iconFile
        val showStroke = false
        val showBackground = true
        val strokeWidth = 10

        // val shapeAppearanceModel = createShapeModelBasedOnCornerFamilyPreference()
        //  val borderDrawable = MaterialShapeDrawable(shapeAppearanceModel)

//        if (showBackground && !isMedia) borderDrawable.setTint(colorPrimary) else borderDrawable.setTint(
//            Color.TRANSPARENT
//        )
//        if (showStroke && isMedia) borderDrawable.setStroke(strokeWidth.toFloat(), colorOnPrimary)

        // iconFile.background = borderDrawable
        iconFile.scaleType =
            if (isMedia) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.CENTER
        //  iconFile.shapeAppearanceModel = shapeAppearanceModel


//        binding.fileDate.visibility = when (viewFileInformationOption) {
//            InterfacePreferences.ViewFileInformationOption.DATE_ONLY -> if (isDir) View.GONE else View.VISIBLE
//            InterfacePreferences.ViewFileInformationOption.EVERYTHING -> if (isDir) View.GONE else View.VISIBLE
//            else -> {
//                View.VISIBLE
//            }
//        }
//        binding.fileSize.visibility = when (viewFileInformationOption) {
//            InterfacePreferences.ViewFileInformationOption.SIZE_ONLY -> if (isDir) View.GONE else View.VISIBLE
//            InterfacePreferences.ViewFileInformationOption.EVERYTHING -> if (isDir) View.GONE else View.VISIBLE
//            else -> {
//                View.GONE
//            }
//        }
    }


    private fun getIconByMimeType(
        mimeType: String?, binding: FileItemViewBinding
    ) {
        val icFile = AppCompatResources.getDrawable(mContext, R.drawable.file_generic_icon)
        val tint = getTintForIcons()
        // icFile?.setTint(tint)
        val iconResourceId = mimeType?.let { MimeTypeUtil().getIconByMimeType(it) } ?: icFile

        //  binding.iconFile.setColorFilter(tint, PorterDuff.Mode.SRC_IN)
        Glide.with(mContext).load(iconResourceId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().placeholder(icFile)).into(binding.iconFile)

    }

    private fun loadImage(path: String, binding: FileItemViewBinding) {
        val imageView = binding.iconFile

        binding.iconFile.clearColorFilter()
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(50, 50).placeholder(R.drawable.ic_image_category).into(imageView)
    }

    private fun loadImageFromDirectory(binding: FileItemViewBinding) {
        val icFolder = AppCompatResources.getDrawable(mContext, R.drawable.ic_home_folder)!!
//        icFolder.setTint(getTintForIcons())
//        binding.iconFile.setColorFilter(getTintForIcons(), PorterDuff.Mode.SRC_IN)

        Glide.with(mContext).load(icFolder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().placeholder(icFolder)).into(binding.iconFile)
    }

    private fun getTintForIcons(): Int {
        val colorPrimaryInverse = ColorUtil().getColorPrimaryInverse(mContext)
        val colorControlNormal =
            mContext.getColor(R.color.file_color)
        val showBackground = true

        return if (showBackground) colorPrimaryInverse else colorControlNormal
    }

    private fun String.isMimeTypeMedia(): Boolean {
        val mediaMimeTypes = listOf("video/", "audio/", "image/")
        val mimeType = this.lowercase(Locale.getDefault())
        return mediaMimeTypes.any { mimeType.startsWith(it) }
    }


    class ViewHolder(val binding: FileItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun clear() {
        super.clear()
        rebuildFilePositionMap()
    }

    override val isAnimationEnabled: Boolean
        get() = Preferences.Interface.isAnimationEnabledForFileList

    companion object {
        private val PAYLOAD_STATE_CHANGED = Any()

        private val CALLBACK = object : DiffUtil.ItemCallback<FileModel>() {
            override fun areItemsTheSame(oldItem: FileModel, newItem: FileModel): Boolean =
                oldItem.filePath == newItem.filePath


            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FileModel, newItem: FileModel): Boolean =
                oldItem == newItem

        }

    }

    override fun getPopupText(view: View, position: Int): CharSequence {
        val locale = Locale(Preferences.Interface.language)
        return getItem(position).fileName.take(1).uppercase(locale)
    }

}
