package com.example.st129_gravityfalls_maker.ui

import android.content.Intent
import android.view.LayoutInflater
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.adapter.BackgroundAdapter
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityBackgroundBinding
import com.example.st129_gravityfalls_maker.dialog.LoadingDialog
import com.example.st129_gravityfalls_maker.extionsion.createBimapFromView
import com.example.st129_gravityfalls_maker.extionsion.eLog
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.extionsion.saveBitmapToInternalStorage
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.utils.DataLocal
import com.example.st129_gravityfalls_maker.utils.KeyApp
import com.example.st129_gravityfalls_maker.utils.KeyApp.DOWNLOAD_ALBUM
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyIntent.PATH_KEY
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyIntent.STATUS_KEY
import com.example.st129_gravityfalls_maker.utils.KeyApp.ValueApp.SUCCESSFUL
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setLocale
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.abs

class BackgroundActivity : BaseActivity<ActivityBackgroundBinding>() {
    private var isFlip = false
    private var isNone = false
    private val backgroundAdapter by lazy {
        BackgroundAdapter(this)
    }
    private val backgroundList = ArrayList<String>()
    private lateinit var transformerImage: CompositePageTransformer
    private lateinit var transformerNone: CompositePageTransformer
    private lateinit var pathImage: String
    override fun setViewBinding(): ActivityBackgroundBinding {
        return ActivityBackgroundBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initTransformer()
        initData()
        initVpg()
        binding.imvImage.setImageResource(R.drawable.img_intro_3)
    }

    override fun viewListener() {
        binding.apply {
            actionBar.apply {
                btnActionBarLeft.setOnSingleClick { handleBack() }
                btnActionBarCenter.setOnSingleClick { handleFlip() }
                btnActionBarRight.setOnSingleClick { handleSave() }
            }
            btnNone.setOnSingleClick { handleNone() }
            binding.vpgBackground.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handleSetBackground(position)
                }
            })
        }
        handleVpg()
    }

    override fun initText() {}

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
            btnActionBarCenter.setImageResource(R.drawable.ic_flip)
            btnActionBarCenter.show()
            btnActionBarRight.setImageResource(R.drawable.ic_save)
            btnActionBarRight.show()
        }
    }

    private fun handleFlip() {
        binding.apply {
            isFlip = !isFlip
            if (imvBackground != null) {
                imvBackground.flipImage()
            }
            imvImage.flipImage()
        }
    }

    private fun initData() {
        backgroundList.clear()
        backgroundList.addAll(DataLocal.getPathAsset(this, KeyApp.KeyAssets.BACKGROUND_ASSET))
        val getPath = intent.getStringExtra(KeyApp.KeyIntent.INTENT_KEY)
        pathImage = getPath!!
        Glide.with(this).load(pathImage).into(binding.imvImage)
    }

    private fun initTransformer() {
        transformerImage = CompositePageTransformer()
        transformerImage.addTransformer { page, position ->
            val scale = 0.9f + (1 - abs(position)) * 0.1f
            page.scaleX = scale
            page.scaleY = scale

        }

        transformerNone = CompositePageTransformer()
        transformerNone.addTransformer { page, position ->
            val scale = 0.9f
            page.scaleX = scale
            page.scaleY = scale

        }
    }

    private fun initVpg() {
        binding.vpgBackground.adapter = backgroundAdapter
        backgroundAdapter.submitList(backgroundList)
        binding.vpgBackground.offscreenPageLimit = 3
        binding.vpgBackground.currentItem = 0
    }

    private fun handleNone() {
        binding.apply {
            isNone = false
            imvBackground.setImageBitmap(null)
            binding.vpgBackground.setPageTransformer(transformerNone)
        }
    }

    private fun handleSetBackground(position: Int) {
        if (!isNone) {
            isNone = true
            binding.vpgBackground.setPageTransformer(transformerImage)
        }
        Glide.with(this@BackgroundActivity).load(backgroundList[position])
            .into(binding.imvBackground)
        if (isFlip) {
            binding.imvBackground.flipImage()
        }
    }

    private fun handleVpg() {
        backgroundAdapter.itemClick = { position -> handleSetBackground(position) }
    }

    override fun onDestroy() {
        super.onDestroy()
        val file = File(pathImage)
        if (file.exists() && file.delete()) {
            eLog("delete success: $pathImage")
        }
    }
    private fun handleSave() {
        binding.apply {
            setLocale(this@BackgroundActivity)
            val loading = LoadingDialog(this@BackgroundActivity)
            loading.show()
            var pathInternal = ""
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                eLog("e: ${throwable.message}")
                loading.dismiss()
                hideNavigation(true)
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    pathInternal = saveBitmapToInternalStorage(
                        DOWNLOAD_ALBUM, createBimapFromView(layoutExport)
                    ).toString()
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await()) {
                        loading.dismiss()
                        hideNavigation(true)
                        val intent = Intent(this@BackgroundActivity, ViewActivity::class.java)
                        intent.putExtra(PATH_KEY, pathInternal)
                        intent.putExtra(STATUS_KEY, SUCCESSFUL)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}