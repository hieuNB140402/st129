package com.example.st129_gravityfalls_maker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivitySettingsBinding
import com.example.st129_gravityfalls_maker.dialog.RateDialog
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundButtonSolidBlue
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntentAnim
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyIntent.INTENT_KEY
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.example.st129_gravityfalls_maker.utils.SystemUtils.policy
import com.example.st129_gravityfalls_maker.utils.SystemUtils.shareApp

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    override fun setViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initRate()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarLeft.setOnSingleClick { handleBack() }
            btnLang.setOnSingleClick { startIntentAnim(LanguageActivity::class.java, INTENT_KEY) }
            btnShare.setOnSingleClick(1500) { shareApp() }
            btnRate.setOnSingleClick { rateApp() }
            btnPolicy.setOnSingleClick(1500) { policy() }
        }
    }

    override fun initText() {
        binding.actionBar.tvCenter.select()
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            tvCenter.text = getString(R.string.settings)
            tvCenter.show()
            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
        }
    }

    private fun initRate() {
        if (SystemUtils.getIsRate(this)) {
            binding.btnRate.gone()
        } else {
            binding.btnRate.show()
        }
    }

    private fun rateApp() {
        val dialogRate = RateDialog(this)
        SystemUtils.setLocale(this)
        dialogRate.show()

        dialogRate.onRateLess3 = {
            binding.btnRate.gone()
            SystemUtils.setIsRate(this, true)
            Toast.makeText(this, R.string.have_rated, Toast.LENGTH_SHORT).show()
            dialogRate.dismiss()
            hideNavigation()
        }
        dialogRate.onRateGreater3 = {
            binding.btnRate.gone()
            SystemUtils.setIsRate(this, true)
            SystemUtils.reviewApp(this, false)
            Toast.makeText(this, R.string.have_rated, Toast.LENGTH_SHORT).show()
            dialogRate.dismiss()
            hideNavigation()
        }

        dialogRate.onCancel = {
            dialogRate.dismiss()
            hideNavigation()
        }
    }
}