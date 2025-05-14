package com.example.st129_gravityfalls_maker.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.utils.SystemUtils


abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 112
    }

    lateinit var binding: T

    protected abstract fun setViewBinding(): T

    protected abstract fun initView()

    protected abstract fun viewListener()

    open fun dataObservable() {}

    protected abstract fun initText()
    protected abstract fun initActionBar()

    open fun initAds() {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtils.setLocale(this)
        binding = setViewBinding()
        setContentView(binding.root)
        initView()
        viewListener()
        dataObservable()
        initText()
        initActionBar()
        initAds()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        super.onResume()
        hideNavigation()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        handleBack()
    }

}
