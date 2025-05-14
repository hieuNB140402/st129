package com.example.st129_gravityfalls_maker.dialog

import android.app.Activity
import androidx.core.content.ContextCompat
import com.example.st129_gravityfalls_maker.base.BaseDialog
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundButtonSolidBlue
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundButtonSolidWhite
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.databinding.DialogConfirmBinding


class ConfirmDialog(val context: Activity, val title: Int, val description: Int) : BaseDialog<DialogConfirmBinding>(context, maxWidth = true, maxHeight = true) {
    override val layoutId: Int = R.layout.dialog_confirm
    override val isCancel: Boolean = false
    override val isBack: Boolean = false

    var onNoClick: (() -> Unit)? = null
    var onYesClick: (() -> Unit)? = null
    var onDismissClick: (() -> Unit)? = null
    override fun initView() {
        initText()
    }

    override fun initAction() {
        binding.apply {
            btnNo.setOnSingleClick {
                onNoClick?.invoke()
            }
            btnYes.setOnSingleClick {
                onYesClick?.invoke()
            }
            main.setOnSingleClick {
                onDismissClick?.invoke()
            }
        }
    }

    override fun onDismissListener() {

    }

    private fun initText() {
        binding.apply {
            tvTitle.text = ContextCompat.getString(context, title)
            tvDescription.text = ContextCompat.getString(context, description)
        }
    }
}