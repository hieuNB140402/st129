package com.example.st129_gravityfalls_maker.ui

import android.view.LayoutInflater
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.adapter.ChooseAvatarAdapter
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityChooseAvatarBinding
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntentAnim
import com.example.st129_gravityfalls_maker.utils.DataLocal

class ChooseAvatarActivity : BaseActivity<ActivityChooseAvatarBinding>() {
    private val adapter by lazy {
        ChooseAvatarAdapter(this)
    }

    override fun setViewBinding(): ActivityChooseAvatarBinding {
        return ActivityChooseAvatarBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initRcv()
    }

    override fun viewListener() {
        binding.actionBar.btnActionBarLeft.setOnSingleClick { handleBack() }
        handleRcv()
    }

    override fun initText() {
        binding.actionBar.tvCenter.select()
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
            tvCenter.text = getString(R.string.gravityfalls)
            tvCenter.show()
        }
    }

    private fun initRcv() {
        binding.apply {
            rcv.adapter = adapter
            rcv.itemAnimator = null
            adapter.submitList(DataLocal.All_DATA)
        }
    }

    private fun handleRcv() {
        binding.apply {
            adapter.onItemClick = { path, position ->
                startIntentAnim(CustomizeActivity::class.java, position)
            }
        }
    }
}