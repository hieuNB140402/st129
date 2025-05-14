package com.example.st129_gravityfalls_maker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivitySplashBinding
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var check = false
    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)) {
            finish(); return;
        }

        CoroutineScope(Job() + Dispatchers.Main).launch{
            delay(3000)
            if (SystemUtils.getFirstLang(this@SplashActivity)) {
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
                check = true
                finishAffinity()
            } else {
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                check = true
                finishAffinity()
            }
        }
    }

    override fun viewListener() {
    }

    override fun initText() {
        binding.apply {

        }
    }

    override fun initActionBar() {
    }

    override fun onBackPressed() {
        if (check) {
            super.onBackPressed()
        } else {
            check = false
        }
    }

}