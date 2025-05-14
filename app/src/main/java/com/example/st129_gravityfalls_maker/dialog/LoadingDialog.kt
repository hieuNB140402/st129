package com.example.st129_gravityfalls_maker.dialog

import android.app.Activity
import com.example.st129_gravityfalls_maker.base.BaseDialog
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.databinding.DialogLoadingBinding


class LoadingDialog(val context: Activity) : BaseDialog<DialogLoadingBinding>(context, maxWidth = true, maxHeight = true) {
    override val layoutId: Int = R.layout.dialog_loading
    override val isCancel: Boolean = false
    override val isBack: Boolean = false
    override fun initView() {}

    override fun initAction() {}

    override fun onDismissListener() {}
}