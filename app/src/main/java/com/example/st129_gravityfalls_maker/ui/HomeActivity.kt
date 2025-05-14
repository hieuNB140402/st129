package com.example.st129_gravityfalls_maker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityHomeBinding
import com.example.st129_gravityfalls_maker.dialog.LoadingDialog
import com.example.st129_gravityfalls_maker.dialog.RateDialog
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntentAnim
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun setViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        countAccess()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarRight.setOnSingleClick { startIntentAnim(SettingsActivity::class.java) }
//            btnCreate.setOnSingleClick { startIntentAnim(ChooseAvatarActivity::class.java) }
//            btnMyAlbum.setOnSingleClick { showInterAll { startIntentAnim(MyAlbumActivity::class.java) } }
        }
    }

    override fun initText() {
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarRight.setImageResource(R.drawable.ic_settings)
            btnActionBarRight.show()
        }
    }

    private fun countAccess() {
        if (!SystemUtils.getIsRate(this) && !SystemUtils.FIRST_ACCESS) {
            SystemUtils.FIRST_ACCESS = true
            SystemUtils.setCountBack(this, SystemUtils.getCountBack(this) + 1)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (!SystemUtils.getIsRate(this) && SystemUtils.getCountBack(this) % 2 == 0) {
            val dialogRate = RateDialog(this)
            SystemUtils.setLocale(this)
            dialogRate.show()
            dialogRate.apply {
                onRateLess3 = {
                    SystemUtils.setIsRate(this@HomeActivity, true)
                    Toast.makeText(this@HomeActivity, R.string.have_rated, Toast.LENGTH_SHORT).show()
                    val handler = Handler()
                    handler.postDelayed({
                        dialogRate.dismiss()
                        exitProcess(0)
                    }, 1000)
                }
                onRateGreater3 = {
                    SystemUtils.setIsRate(this@HomeActivity, true)
                    SystemUtils.reviewApp(this@HomeActivity, true)
                }

                onCancel = {
                    dialogRate.dismiss()
                    exitProcess(0)
                }
            }
        } else {
            exitProcess(0)
        }
    }



}