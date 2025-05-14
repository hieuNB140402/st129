package com.example.st129_gravityfalls_maker.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.adapter.IntroAdapter
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityIntroBinding
import com.example.st129_gravityfalls_maker.utils.DataLocal.itemIntroList
import com.example.st129_gravityfalls_maker.utils.NetworkMonitorService
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.example.st129_gravityfalls_maker.utils.SystemUtils.isConnectInternet
import kotlin.system.exitProcess

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    private var checkStartHome = false
    private val adapter = IntroAdapter(this, itemIntroList)

    override fun setViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initVpg()
    }

    override fun viewListener() {
        binding.btnNext.setOnClickListener { handleNext() }
    }

    override fun initText() {

    }

    override fun initActionBar() {}

    private fun initVpg() {
        binding.apply {
            binding.vpgTutorial.adapter = adapter
            binding.dotsIndicator.setViewPager2(binding.vpgTutorial)
        }
    }


    private fun handleNext() {
        binding.apply {
            val nextItem = binding.vpgTutorial.currentItem + 1
            if (nextItem < itemIntroList.size) {
                vpgTutorial.setCurrentItem(nextItem, true)
            } else {
                if (!checkStartHome) {
                    if (SystemUtils.getFirstPermission(this@IntroActivity)) {
                        checkStartHome = true
                        val intent = Intent(this@IntroActivity, PermissionActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        checkStartHome = true
                        val intent = Intent(this@IntroActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        exitProcess(0)
    }
}