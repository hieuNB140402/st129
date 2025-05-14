package com.example.st129_gravityfalls_maker.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityViewBinding
import com.example.st129_gravityfalls_maker.dialog.ConfirmDialog
import com.example.st129_gravityfalls_maker.dialog.LoadingDialog
import com.example.st129_gravityfalls_maker.extionsion.checkPermissions
import com.example.st129_gravityfalls_maker.extionsion.dLog
import com.example.st129_gravityfalls_maker.extionsion.goToSettings
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.handleComeBackHome
import com.example.st129_gravityfalls_maker.extionsion.handleShare
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.extionsion.requestPermission
import com.example.st129_gravityfalls_maker.extionsion.saveBitmapToExternalStorage
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.showToast
import com.example.st129_gravityfalls_maker.utils.KeyApp
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setLocale
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class ViewActivity : BaseActivity<ActivityViewBinding>() {
    private var path: String = ""
    private lateinit var bitmap: Bitmap
    private lateinit var dialogLoading: LoadingDialog
    private var isStatus = KeyApp.ValueApp.VIEW

    override fun setViewBinding(): ActivityViewBinding {
        return ActivityViewBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initData()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.apply {
                btnActionBarLeft.setOnSingleClick { handleBack() }
                btnActionBarRight.setOnSingleClick { handleRightTop() }
                btnActionBarCenter.setOnSingleClick { handleComeBackHome(this@ViewActivity) }

            }
            btnBottomLeft.setOnSingleClick { handleLeftBot() }
            btnBottomRight.setOnSingleClick { handleRightBot() }
        }
    }

    override fun initText() {
        binding.apply {
            tvBottomLeft.select()
            tvBottomRight.select()
            actionBar.tvCenter.select()
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
        }
    }

    private fun initData() {
        binding.apply {
            val getPath = intent.getStringExtra(KeyApp.KeyIntent.PATH_KEY)
            val getStatus = intent.getIntExtra(KeyApp.KeyIntent.STATUS_KEY, KeyApp.ValueApp.VIEW)
            getStatus.let {
                isStatus = getStatus
                when (isStatus) {
                    KeyApp.ValueApp.VIEW -> {
                        tvBottomLeft.text = getString(R.string.download)
                        tvBottomRight.text = getString(R.string.share)
                        actionBar.apply {
                            tvCenter.text = getString(R.string.view)
                            tvCenter.show()
                            btnActionBarRight.setImageResource(R.drawable.ic_delete)
                            btnActionBarRight.show()
                        }
                    }

                    else -> {
                        tvBottomLeft.text = getString(R.string.my_album)
                        tvBottomRight.text = getString(R.string.download)
                        actionBar.apply {
                            btnActionBarCenter.setImageResource(R.drawable.ic_home)
                            btnActionBarCenter.show()
                            btnActionBarRight.setImageResource(R.drawable.ic_share_suc)
                            btnActionBarRight.show()
                        }
                    }
                }
            }

            dialogLoading = LoadingDialog(this@ViewActivity)
            getPath.let {
                path = it!!
                setLocale(this@ViewActivity)
                dialogLoading.show()
                val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                    dialogLoading.dismiss()
                    hideNavigation()
                }

                CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                    val job1 = async {
                        bitmap = Glide.with(this@ViewActivity).load(path).submit().get().toBitmap()
                        return@async true
                    }
                    launch(Dispatchers.Main) {
                        if (job1.await()) {
                            imvImage.setImageBitmap(bitmap)
                            dialogLoading.dismiss()
                            hideNavigation()
                        }
                    }
                }
            }

        }
    }

    private fun handleRightTop() {
        when (isStatus) {
            KeyApp.ValueApp.VIEW -> {
                handleDelete()
            }

            else -> {
                handleShare(this@ViewActivity, bitmap)
            }
        }
    }

    private fun handleDelete() {
        val dialogDelete = ConfirmDialog(this@ViewActivity, R.string.delete, R.string.do_you_want_to_delete)
        setLocale(this@ViewActivity)
        dialogDelete.show()

        dialogDelete.onNoClick = {
            dialogDelete.dismiss()
            hideNavigation()
        }


        dialogDelete.onYesClick = {
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                dialogDelete.dismiss()
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    val file = File(path)
                    if (file.exists() && file.delete()) {
                        dLog("Delete Successfully")
                    }
                    return@async true
                }
                launch(Dispatchers.Main) {
                    if (job1.await()) {
                        dialogDelete.dismiss()
                        hideNavigation()
                        finish()
                    }
                }
            }
        }
    }

    private fun handleLeftBot() {
        binding.apply {
            when (isStatus) {
                KeyApp.ValueApp.VIEW -> {
                    checkPermission()
                }

                else -> {
                    val intent = Intent(this@ViewActivity, MyAlbumActivity::class.java)
                    intent.putExtra(KeyApp.KeyIntent.FROM_SAVE, KeyApp.KeyIntent.FROM_SAVE)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

    private fun handleRightBot() {
        when (isStatus) {
            KeyApp.ValueApp.VIEW -> {
                handleShare(this@ViewActivity, bitmap)

            }

            else -> {
                checkPermission()
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (checkPermissions(SystemUtils.storagePermission)) {
                handleDownload()
            } else {
                if (SystemUtils.getStoragePermission(this) < 2 && !checkPermissions(SystemUtils.storagePermission)) {
                    requestPermission(SystemUtils.storagePermission, KeyApp.RequestCode.STORAGE_PERMISSION_CODE)
                } else {
                    goToSettings()
                }
            }
        } else {
            handleDownload()
        }
    }

    private fun handleDownload() {
        setLocale(this@ViewActivity)
        dialogLoading.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            dialogLoading.dismiss()
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                saveBitmapToExternalStorage(bitmap)
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await()) {
                    dialogLoading.dismiss()
                    showToast(getString(R.string.download_success))
                    hideNavigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigation()
    }
}