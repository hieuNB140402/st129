package com.example.st129_gravityfalls_maker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.adapter.MyAlbumAdapter
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityMyAlbumBinding
import com.example.st129_gravityfalls_maker.databinding.PopupMyAlbumBinding
import com.example.st129_gravityfalls_maker.dialog.ConfirmDialog
import com.example.st129_gravityfalls_maker.dialog.LoadingDialog
import com.example.st129_gravityfalls_maker.extionsion.checkPermissions
import com.example.st129_gravityfalls_maker.extionsion.dpToPx
import com.example.st129_gravityfalls_maker.extionsion.eLog
import com.example.st129_gravityfalls_maker.extionsion.getImageInternal
import com.example.st129_gravityfalls_maker.extionsion.goToSettings
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.handleComeBackHome
import com.example.st129_gravityfalls_maker.extionsion.hide
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.extionsion.requestPermission
import com.example.st129_gravityfalls_maker.extionsion.saveBitmapToCache
import com.example.st129_gravityfalls_maker.extionsion.saveBitmapToExternalStorage
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.shareImage
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.showToast
import com.example.st129_gravityfalls_maker.model.MyAlbumModel
import com.example.st129_gravityfalls_maker.utils.KeyApp
import com.example.st129_gravityfalls_maker.utils.KeyApp.DOWNLOAD_ALBUM
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MyAlbumActivity : BaseActivity<ActivityMyAlbumBinding>() {
    private val myAlbumList: ArrayList<MyAlbumModel> = arrayListOf()
    private val adapterMyAlbum by lazy {
        MyAlbumAdapter(this)
    }
    private var listSelect: ArrayList<View> = arrayListOf()
    private var isFromSuccess = false
    override fun setViewBinding(): ActivityMyAlbumBinding {
        return ActivityMyAlbumBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initList()
        initData()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarLeft.setOnSingleClick { handleFinish() }
            btnShare.setOnSingleClick {
                selectList(1)
            }
            btnDownload.setOnSingleClick {
                selectList(0)
            }
            actionBar.btnActionBarRight.setOnSingleClick {
                selectList(-1)
            }
            main.setOnSingleClick {
                resetLongClick()
            }
        }
        handleRcv()
    }

    override fun initText() {
        binding.apply {
            actionBar.tvCenter.select()
            tvDownload.select()
            tvShare.select()
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            tvCenter.text = getString(R.string.my_album)
            tvCenter.show()
            btnActionBarRight.setImageResource(R.drawable.ic_delete)

            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
        }
    }

    private fun initList() {
        listSelect = arrayListOf(
            binding.actionBar.btnActionBarRight, binding.btnSelectAll, binding.tvSelectAll, binding.layoutBot
        )
    }

    private fun initData() {
        val getFromSuccess = intent.getStringExtra(KeyApp.KeyIntent.FROM_SAVE)
        if (getFromSuccess != null) {
            isFromSuccess = true
        }
        getData()
        if (myAlbumList.isEmpty()) {
            binding.layoutNoItem.show()
        } else {
            initRcv()
        }
    }

    private fun getData() {
        val listImageMyDs = getImageInternal(this, DOWNLOAD_ALBUM)
        myAlbumList.clear()
        listImageMyDs.forEach {
            myAlbumList.add(MyAlbumModel(it))
        }
    }

    private fun initRcv() {
        binding.apply {
            rcvMyAlbum.adapter = adapterMyAlbum
            rcvMyAlbum.itemAnimator = null
            adapterMyAlbum.submitList(myAlbumList)
        }
    }

    override fun onResume() {
        super.onResume()
        resetLongClick()
    }

    private fun handleRcv() {
        adapterMyAlbum.apply {
            onItemClick = { path ->
                handleOnItemClick(path)
            }
            onMoreClick = { path, position, view ->
                handleMoreMyDesign(path, view)
            }
            onLongClick = { position ->
                handleLongItemClick(position)
            }
        }

        binding.rcvMyAlbum.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView, motionEvent: MotionEvent
            ): Boolean {
                return when {
                    motionEvent.action != MotionEvent.ACTION_UP || recyclerView.findChildViewUnder(
                        motionEvent.x, motionEvent.y
                    ) != null -> false

                    else -> {
                        resetLongClick()
                        true
                    }
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
        })
    }

    private fun resetLongClick() {
        myAlbumList.clear()
        getData()
        adapterMyAlbum.submitList(myAlbumList)
        if (myAlbumList.size == 0) {
            binding.layoutNoItem.show()
        } else {
            binding.layoutNoItem.gone()
        }
        binding.btnSelectAll.setImageResource(R.drawable.ic_not_select_all)
        listSelect.forEachIndexed { index, view ->
            if (index == listSelect.size - 1) {
                view.gone()
            } else {
                view.hide()
            }
        }
    }

    private fun handleOnItemClick(path: String) {
        val intent = Intent(this, ViewActivity::class.java)
        intent.putExtra(KeyApp.KeyIntent.PATH_KEY, path)
        intent.putExtra(KeyApp.KeyIntent.STATUS_KEY, (KeyApp.ValueApp.VIEW))
        this.startActivity(intent)
    }

    private fun handleMoreMyDesign(path: String, view: View) {
        val popupBinding = PopupMyAlbumBinding.inflate(LayoutInflater.from(this))
        val popupWindow = PopupWindow(
            popupBinding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        popupWindow.elevation = 10f

        popupBinding.tvDownload.select()
        popupBinding.tvShare.select()
        popupBinding.tvDelete.select()

        popupBinding.mnuDelete.setOnSingleClick {
            handleDeleteItemPopup(path, popupWindow)
        }
        popupBinding.mnuShare.setOnSingleClick {
            handleShareItemPopup(path, popupWindow)
        }
        popupBinding.mnuDownload.setOnSingleClick {
            checkPermissionPath(path, popupWindow)
        }

        val xOffset = this.dpToPx(-125)
        val yOffset = this.dpToPx(6)

        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewY = location[1]
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val distanceToBottom = screenHeight - viewY - view.height
        if (distanceToBottom >= this.dpToPx(180)) {
            popupWindow.showAsDropDown(view, xOffset, yOffset)
        } else {
            popupWindow.showAsDropDown(view, xOffset, this.dpToPx(-135))
        }
    }

    private fun handleDeleteItemPopup(path: String, popupWindow: PopupWindow) {
        val dialogDelete = ConfirmDialog(this, R.string.delete, R.string.do_you_want_to_delete)
        SystemUtils.setLocale(this)
        dialogDelete.show()
        popupWindow.dismiss()
        dialogDelete.binding.btnNo.setOnSingleClick {
            dialogDelete.dismiss()
            hideNavigation()
        }

        dialogDelete.binding.btnYes.setOnSingleClick {
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                dialogDelete.dismiss()
                hideNavigation()
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    val file = File(path)
                    if (file.exists() && file.delete()) {
                        myAlbumList.removeIf {
                            it.path == path
                        }
                    }
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await()) {
                        adapterMyAlbum.submitList(myAlbumList)
                        if (myAlbumList.isEmpty()) {
                            binding.layoutNoItem.show()
                        }
                        dialogDelete.dismiss()
                        hideNavigation()
                    }
                }
            }
        }
    }

    private fun handleShareItemPopup(path: String, popupWindow: PopupWindow) {
        var image: Bitmap? = null
        var imageFile: File? = null
        popupWindow.dismiss()
        val dialogLoading = LoadingDialog(this)
        SystemUtils.setLocale(this)
        dialogLoading.show()

        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            dialogLoading.dismiss()
            hideNavigation()
        }

        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job = async {
                image = Glide.with(this@MyAlbumActivity).load(path).submit().get().toBitmap()
                return@async true
            }

            val job2 = async {
                if (job.await()) {
                    imageFile = image?.let { saveBitmapToCache(it) }
                }
                return@async true
            }

            withContext(Dispatchers.Main) {
                if (job.await() && job2.await()) {
                    imageFile?.let { file ->
                        val imageUri = FileProvider.getUriForFile(
                            this@MyAlbumActivity, "${packageName}.provider", file
                        )
                        dialogLoading.dismiss()
                        imageUri?.let {
                            shareImage(this@MyAlbumActivity, it)
                            hideNavigation()
                        }
                    }
                }
            }
        }
    }

    private fun checkPermissionPath(path: String, popupWindow: PopupWindow) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (this.checkPermissions(SystemUtils.storagePermission)) {
                handleDownloadItemPopup(path, popupWindow)
            } else {
                if (SystemUtils.getStoragePermission(this) < 2 && !this.checkPermissions(
                        SystemUtils.storagePermission
                    )
                ) {
                    this.requestPermission(
                        SystemUtils.storagePermission, KeyApp.RequestCode.STORAGE_PERMISSION_CODE
                    )
                } else {
                    this.goToSettings()
                }
            }
        } else {
            handleDownloadItemPopup(path, popupWindow)
        }
    }

    private fun handleDownloadItemPopup(path: String, popupWindow: PopupWindow) {
        var bitmap: Bitmap? = null
        popupWindow.dismiss()
        val dialogDownload = LoadingDialog(this)
        SystemUtils.setLocale(this)
        dialogDownload.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            dialogDownload.dismiss()
            hideNavigation()
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                bitmap = Glide.with(this@MyAlbumActivity).load(path).submit().get().toBitmap()
                return@async true
            }
            val job2 = async {
                if (job1.await()) {
                    bitmap?.let { saveBitmapToExternalStorage(it) }
                }
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await() && job2.await()) {
                    dialogDownload.dismiss()
                    showToast(getString(R.string.download_success))
                    hideNavigation()
                }
            }
        }
    }

    private fun handleLongItemClick(position: Int) {
        binding.apply {
            myAlbumList.forEach {
                it.isShowSelection = true
            }
            listSelect.forEach {
                it.show()
            }

            myAlbumList[position].isSelected = true
            adapterMyAlbum.submitList(myAlbumList)


            adapterMyAlbum.onItemTick = { positionTick ->
                handleSelect(positionTick)
            }
            if (myAlbumList.size == 1) {
                btnSelectAll.setImageResource(R.drawable.ic_select_all)
            }

            btnSelectAll.setOnSingleClick {
                checkSelectAll()
            }
        }
    }

    private fun handleSelect(positionTick: Int) {
        myAlbumList[positionTick].isSelected = !myAlbumList[positionTick].isSelected
        var checkAll = true
        myAlbumList.forEach {
            if (!it.isSelected) {
                checkAll = false
                return@forEach
            }
        }
        if (checkAll) {
            binding.btnSelectAll.setImageResource(R.drawable.ic_select_all)
        } else {
            binding.btnSelectAll.setImageResource(R.drawable.ic_not_select_all)
        }
        adapterMyAlbum.submitItem(positionTick, myAlbumList[positionTick].isSelected)
    }

    private fun checkSelectAll() {
        binding.apply {
            var check = true
            for (i in 0 until myAlbumList.size) {
                if (!myAlbumList[i].isSelected) {
                    check = false
                    break
                }
            }
            if (!check) {
                myAlbumList.forEach {
                    it.isSelected = true
                }
                adapterMyAlbum.submitList(myAlbumList)
                btnSelectAll.setImageResource(R.drawable.ic_select_all)
            } else {
                myAlbumList.forEach {
                    it.isSelected = false
                }
                adapterMyAlbum.submitList(myAlbumList)
                btnSelectAll.setImageResource(R.drawable.ic_not_select_all)
            }
        }
    }

    // -1: delete - 0: download - 1: share
    private fun selectList(status: Int) {
        var isTick = false

        for (i in 0 until myAlbumList.size) {
            if (myAlbumList[i].isSelected) {
                isTick = true
                break
            }
        }
        if (!isTick) {
            this.showToast(getString(R.string.please_select_an_image))
        } else {
            val listFile: ArrayList<String> = arrayListOf()
            myAlbumList.forEach {
                if (it.isSelected) {
                    listFile.add(it.path)
                }
            }
            when (status) {
                -1 -> handleDelete(listFile)
                0 -> checkPermission(listFile)
                1 -> handleShare(listFile)
            }
        }
    }

    private fun handleDelete(listPath: ArrayList<String>) {
        val dialogDelete = ConfirmDialog(this, R.string.delete, R.string.do_you_want_to_delete)
        SystemUtils.setLocale(this)
        dialogDelete.show()

        dialogDelete.binding.btnNo.setOnSingleClick {
            dialogDelete.dismiss()
            hideNavigation()
        }

        dialogDelete.binding.btnYes.setOnSingleClick {
            val dialog = LoadingDialog(this)
            SystemUtils.setLocale(this)
            dialog.show()

            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                dialogDelete.dismiss()
                dialog.dismiss()
                hideNavigation()
            }

            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    for (i in 0 until listPath.size) {
                        val file = File(listPath[i])
                        if (file.exists() && file.delete()) {
                            myAlbumList.removeIf {
                                it.path == listPath[i]
                            }
                        }
                    }
                    return@async true
                }
                val job2 = async {
                    if (job1.await()) {
                        myAlbumList.forEach {
                            it.isShowSelection = false
                        }
                    }
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await() && job2.await()) {
                        listSelect.forEachIndexed { index, view ->
                            if (index == listSelect.size - 1) {
                                view.gone()
                            } else {
                                view.hide()
                            }
                        }
                        binding.btnSelectAll.setImageResource(R.drawable.ic_not_select_all)
                        adapterMyAlbum.submitList(myAlbumList)
                        if (myAlbumList.isEmpty()) {
                            binding.layoutNoItem.show()
                        }
                        dialogDelete.dismiss()
                        dialog.dismiss()
                        hideNavigation()
                    }
                }
            }
        }
    }

    private fun checkPermission(listFile: ArrayList<String>) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (this.checkPermissions(SystemUtils.storagePermission)) {
                handleDownload(listFile)
            } else {
                if (SystemUtils.getStoragePermission(this) < 2 && !this.checkPermissions(
                        SystemUtils.storagePermission
                    )
                ) {
                    this.requestPermission(
                        SystemUtils.storagePermission, KeyApp.RequestCode.STORAGE_PERMISSION_CODE
                    )
                } else {
                    this.goToSettings()
                }
            }
        } else {
            handleDownload(listFile)
        }
    }

    private fun handleDownload(listPath: ArrayList<String>) {
        val dialog = LoadingDialog(this)
        SystemUtils.setLocale(this)
        dialog.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            dialog.dismiss()
            hideNavigation()
        }
        val totalBitmaps = listPath.size

        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                for (i in 0 until totalBitmaps) {
                    val bitmap = Glide.with(this@MyAlbumActivity).load(listPath[i]).submit().get().toBitmap()
                    saveBitmapToExternalStorage(bitmap)
                }
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await()) {
                    showToast(R.string.download_success)
                    dialog.dismiss()
                    hideNavigation()
                }
            }
        }
    }

    private fun handleShare(imagePaths: ArrayList<String>) {
        val imageUris = ArrayList<Uri>()

        val loading = LoadingDialog(this)
        SystemUtils.setLocale(this)
        loading.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            this.eLog("share: ${throwable.message}")
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                for (filePath in imagePaths) {
                    val imageFile = File(filePath)
                    val imageUri = FileProvider.getUriForFile(
                        this@MyAlbumActivity, "${packageName}.provider", imageFile
                    )
                    imageUris.add(imageUri)
                }
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await()) {
                    loading.dismiss()
                    hideNavigation()
                    shareImages(imageUris)
                }
            }
        }
    }

    private fun shareImages(imageUris: ArrayList<Uri>) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        this.startActivity(Intent.createChooser(intent, "Share Images"))
    }

    override fun onPause() {
        super.onPause()
        resetLongClick()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        handleFinish()
    }

    private fun handleFinish() {
        if (isFromSuccess) {
            handleComeBackHome(this)
            finishAffinity()
        } else {
            handleBack()
        }
    }
}