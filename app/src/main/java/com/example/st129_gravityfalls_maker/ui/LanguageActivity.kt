package com.example.st129_gravityfalls_maker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.adapter.LanguageAdapter
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityLanguageBinding
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntent
import com.example.st129_gravityfalls_maker.model.LanguageModel
import com.example.st129_gravityfalls_maker.utils.DataLocal.getLanguageList
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyIntent.INTENT_KEY
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setFirstLang
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setPreLanguage
import kotlin.system.exitProcess

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private var listLanguage: ArrayList<LanguageModel> = arrayListOf()
    private var codeLang: String = ""
    private var isFirstAccess: Boolean = true
    private val languageAdapter by lazy {
        LanguageAdapter(this)
    }

    override fun setViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initData()
        initRcv()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarLeft.setOnSingleClick { handleBack() }
            actionBar.btnActionBarRight.setOnSingleClick { handleDone() }
        }
        handleRcv()
    }

    override fun initText() {
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarRight.setImageResource(R.drawable.ic_done)
            btnActionBarRight.show()
            tvCenter.select()
            tvStart.select()
            tvCenter.text = getString(R.string.language)
            tvCenter.show()
        }
    }

    private fun initData() {
        binding.apply {
            val intent = intent.getStringExtra(INTENT_KEY)
            codeLang = SystemUtils.getPreLanguage(this@LanguageActivity)
            if (intent != null) {
                isFirstAccess = false
                actionBar.apply {
                    btnActionBarLeft.setImageResource(R.drawable.ic_back)
                    btnActionBarLeft.show()
                }
            } else {
                isFirstAccess = true
            }
        }
    }

    private fun initRcv() {
        binding.apply {
            listLanguage.clear()
            listLanguage.addAll(getLanguageList())

            val lang = SystemUtils.getPreLanguage(this@LanguageActivity)

            for (i in listLanguage.indices) {
                if (listLanguage[i].code == lang) {
                    val matchingLanguage = listLanguage[i]
                    listLanguage.removeAt(i)
                    listLanguage.add(0, matchingLanguage)

                    if (!SystemUtils.getFirstLang(this@LanguageActivity)) {
                        listLanguage[0].activate = true
                    }
                    break
                }
            }

            if (isFirstAccess) {
                codeLang = "en"
                listLanguage[0].activate = true
            }
            rcv.adapter = languageAdapter
            rcv.itemAnimator = null
            languageAdapter.submitList(listLanguage)
        }
    }

    private fun handleRcv() {
        binding.apply {
            languageAdapter.onItemClick = { item ->
                codeLang = item.code
                languageAdapter.submitItem(item.code)
            }
        }
    }

    private fun handleDoneFirst() {
        if (codeLang == "") {
            Toast.makeText(this, R.string.not_select_lang, Toast.LENGTH_SHORT).show()
        } else {
            setPreLanguage(this@LanguageActivity, codeLang)
            setFirstLang(this, false)
            startIntent(IntroActivity::class.java)
            finishAffinity()
        }
    }

    private fun handleChangeSetting() {
        setPreLanguage(this@LanguageActivity, codeLang)
        startIntent(HomeActivity::class.java)
        finishAffinity()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (!SystemUtils.getFirstLang(this)) {
            handleBack()
        } else {
            exitProcess(0)
        }
    }

    private fun handleDone() {
        if (isFirstAccess) {
            handleDoneFirst()
        } else {
            handleChangeSetting()
        }
    }
}