package com.manager.filemanager.fragment

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.manager.filemanager.R
import com.manager.filemanager.activity.MainActivity
import com.manager.filemanager.activity.SettingsActivity
import com.manager.filemanager.compose.core.navigation.CategoryListRoute
import com.manager.filemanager.compose.feature.presentation.HomeScreen
import com.manager.filemanager.files.extensions.applyBackgroundFromPreferences
import com.manager.filemanager.files.util.fileProviderUri
import com.manager.filemanager.interfaces.manager.ItemListener
import com.manager.filemanager.manager.category.adapter.Category
import com.manager.filemanager.manager.category.adapter.CategoryFileModel
import com.manager.filemanager.manager.category.adapter.CategoryFileModelAdapter
import com.manager.filemanager.manager.category.adapter.RecentImagemodelAdapter
import com.manager.filemanager.manager.category.adapter.getCategories
import com.manager.filemanager.manager.media.image.viewer.ImageViewerDialogFragment
import com.manager.filemanager.manager.util.FileUtils
import com.manager.filemanager.manager.util.FileUtils.SpaceType
import com.manager.filemanager.manager.util.MaterialDialogUtils
import com.manager.filemanager.settings.preference.AboutFragment
import com.manager.filemanager.ui.view.ModalBottomSheetAddCategory
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.io.path.pathString


class RecentFragment : Fragment(), ItemListener {

    private lateinit var fileUtils: FileUtils
    private lateinit var cRecentImg: MaterialCardView
    private lateinit var cInternalStorage: MaterialCardView
    private lateinit var cCategoryFileItem: MaterialCardView
    private lateinit var cBaseItem: MaterialCardView
    private lateinit var btnAddCategory: Button
    private lateinit var adapter: CategoryFileModelAdapter
    //  private lateinit var fabChat: ExtendedFloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_recent, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        cBaseItem = view.findViewById(R.id.cBaseItems)
//        cCategoryFileItem = view.findViewById(R.id.cCategoryItem)
       //cRecentImg = view.findViewById(R.id.cRecentImage)
        cInternalStorage = view.findViewById(R.id.cInternalStorage)
        // fabChat = view.findViewById(R.id.fab_chat)

        //val mnAbout = view.findViewById<ImageView>(R.id.mn_about)
        val aboutFragment = AboutFragment()

//        mnAbout.setOnClickListener {
//            (requireActivity() as MainActivity).startNewFragment(aboutFragment)
//        }
      //  initStyleView()
        fileUtils = FileUtils()
        setStorageSpaceInGB()
        initCategoryItem()
        initClick()
        setRecentImages()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        showDialogStoragePermission()

    }

    @SuppressLint("SuspiciousIndentation")
    fun initCategoryItem() {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.rvCategory)
        val categoryFileModels = getCategories(requireContext())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = CategoryFileModelAdapter(this, categoryFileModels, requireContext())
        recyclerView.adapter = adapter

    }

    private fun showBottomSheetAddCategory() {
        val modalBottomSheetAddCategory = ModalBottomSheetAddCategory()
        modalBottomSheetAddCategory.itemListener = this
        modalBottomSheetAddCategory.show(parentFragmentManager, ModalBottomSheetAddCategory.TAG)
    }

    private fun setRecentImages() {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recy_recents_images)
        val listener = this
        val mainScope = CoroutineScope(Dispatchers.Main)
        mainScope.launch {
            val recentImage = withContext(Dispatchers.IO) {
                fileUtils.getRecentImages(requireContext())
            }
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val adapter = RecentImagemodelAdapter(listener, recentImage, requireContext())
            recyclerView.adapter = adapter
        }
    }

    private fun initClick() {
        val itemStorage = requireView().findViewById<MaterialCardView>(R.id.cInternalStorage)
       // val ivTrash = requireView().findViewById<ImageView>(R.id.mn_trash)
       // val ivSettings = requireView().findViewById<ImageView>(R.id.iv_settings)
       // ivTrash.visibility = View.INVISIBLE
        itemStorage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }
//        ivSettings.setOnClickListener {
//            val settingsIntent: Intent = SettingsActivity().getIntent(requireContext())
//            startActivity(settingsIntent)
//        }
//        btnAddCategory.setOnClickListener { showBottomSheetAddCategory() }

//        fabChat.setOnClickListener {
//            val intent = Intent(requireActivity(), HomeScreen::class.java).apply {
//                putExtra("startDestination", ChatRoute)
//            }
//            startActivity(intent)
//        }
    }

    private fun openListFiles() {
        val homeFragment = HomeFragment()
        (requireActivity() as MainActivity).startNewFragment(homeFragment)
    }

    private fun initStyleView() {
//        cBaseItem.applyBackgroundFromPreferences()
//        cRecentImg.applyBackgroundFromPreferences()
      //  cInternalStorage.applyBackgroundFromPreferences()
      //  cCategoryFileItem.applyBackgroundFromPreferences()
    }

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    fun setStorageSpaceInGB() {
       // val tvSpaceUsed = requireView().findViewById<TextView>(R.id.tv_space_used)
        val tvSpaceFree = requireView().findViewById<TextView>(R.id.tv_space_free)
        val tvSpaceOf = requireView().findViewById<TextView>(R.id.tv_space_of)
     //   val cpSpace = requireView().findViewById<CircularProgressIndicator>(R.id.cp_space)
        val pbSpace = requireView().findViewById<LinearProgressIndicator>(R.id.pb_space)

        val totalSpace = fileUtils.getStorageSpaceInGB(SpaceType.TOTAL)
        val freeSpace = fileUtils.getStorageSpaceInGB(SpaceType.FREE)
        val usedSpace = fileUtils.getStorageSpaceInGB(SpaceType.USED)
       // val animation = ObjectAnimator.ofInt(cpSpace, "progress", 0, usedSpace)

        val formattedUsedSpace = String.format("%dGB", usedSpace)
        val formattedTotalSpace = String.format("%dGB", totalSpace)
        val formattedFreeSpace = String.format("Free space: %dMB", freeSpace)


      //  tvSpaceUsed.text = formattedUsedSpace
        tvSpaceFree.text = formattedFreeSpace
  //      tvSpaceTotal.text = formattedTotalSpace
        tvSpaceOf.text = getString(R.string.space_of_format, formattedUsedSpace, formattedTotalSpace)

//        animation.duration = 1000
       // animation.start()

        pbSpace.progress = 0
//        animation.duration = 1000
//        animation.addUpdateListener { valueAnimator ->
//            pbSpace.progress = valueAnimator.animatedValue as Int
//        }
//        animation.start()
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageExternalPermission()
        } else {
            requestReadWritePermission()
        }
    }

    private fun requestReadWritePermission() {
        val readWritePermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (arePermissionsGranted(readWritePermission)) {
            openListFiles()
            setRecentImages()
        } else {
            val title = getString(R.string.permission_required)
            val message = getString(R.string.permission_required_body)
            val textPositiveButton = getString(R.string.allow)
            val textNegativeButton = getString(R.string.dialog_cancel)

            MaterialDialogUtils().createDialogInfo(
                title, message, textPositiveButton, textNegativeButton, requireContext(), true
            ) { dialogResult ->
                val isConfirmed = dialogResult.confirmed
                if (isConfirmed) {
                    ActivityCompat.requestPermissions(
                        requireActivity(), readWritePermission, 1
                    )
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)


                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestManageExternalPermission() {
        if (Environment.isExternalStorageManager()) {
            openListFiles()
            setRecentImages()
        } else {
            val title = getString(R.string.permission_required)
            val message = getString(R.string.permission_required_body)
            val textPositiveButton = getString(R.string.allow)
            val textNegativeButton = getString(R.string.dialog_cancel)

            MaterialDialogUtils().createDialogInfo(
                title, message, textPositiveButton, textNegativeButton, requireContext(), true
            ) { dialogResult ->
                val isConfirmed = dialogResult.confirmed
                if (isConfirmed) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                }
            }

        }
    }

    private fun showDialogStoragePermission() {
        if (!checkPermission()) {
            val title = getString(R.string.permission_required)
            val message = getString(R.string.permission_required_body)
            val textPositiveButton = getString(R.string.allow)
            val textNegativeButton = getString(R.string.dialog_cancel)

            MaterialDialogUtils().createDialogInfo(
                title, message, textPositiveButton, textNegativeButton, requireContext(), true
            ) { dialogResult ->
                val isConfirmed = dialogResult.confirmed
                if (isConfirmed) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        val uri = Uri.fromParts("package", requireContext().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        val readWritePermission = arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )

                        ActivityCompat.requestPermissions(
                            requireActivity(), readWritePermission, 1
                        )
                    }
                }
            }
        }
    }


    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readWritePermission = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            arePermissionsGranted(readWritePermission)
        }
    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openListFiles()
        } else {
            requestStoragePermission()
        }
    }

    override fun openFileCategory(path: Path, categoryFileModel: CategoryFileModel) {
        if (isReadStoragePermissionGranted()) {
            if (categoryFileModel.category == Category.DOCUMENTS) {
                val uri = path.fileProviderUri
                val homeFragment = HomeFragment.newInstance(uri)
                (requireActivity() as MainActivity).startNewFragment(homeFragment)
            } else {
                val intent = Intent(requireContext(), HomeScreen::class.java)
                intent.putExtra("categoryFileModel", categoryFileModel)
                intent.putExtra("startDestination", CategoryListRoute)
                requireActivity().startActivity(intent)
            }

        }
    }

    override fun openItemWith(path: Path) {
        showImageViewerDialog(listOf(path))

    }

    private fun showImageViewerDialog(imagePathList: List<Path>) {
        val imageViewerDialogFragment = ImageViewerDialogFragment()
        imageViewerDialogFragment.arguments = Bundle().apply {
            putStringArrayList(
                ImageViewerDialogFragment.ARG_IMAGE_PATH_LIST,
                java.util.ArrayList(imagePathList.map { it.pathString })
            )
        }
        imageViewerDialogFragment.show(
            requireActivity().supportFragmentManager, ImageViewerDialogFragment.TAG
        )
    }

    private fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || Environment.isExternalStorageManager()
        }
    }

    override fun refreshItem() {
        initCategoryItem()
    }

}