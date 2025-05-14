package com.example.st129_gravityfalls_maker.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityPermissionBinding
import com.example.st129_gravityfalls_maker.extionsion.checkPermissions
import com.example.st129_gravityfalls_maker.extionsion.goToSettings
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.requestPermission
import com.example.st129_gravityfalls_maker.extionsion.select
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntentAnim
import com.example.st129_gravityfalls_maker.utils.KeyApp.RequestCode.NOTIFICATION_PERMISSION_CODE
import com.example.st129_gravityfalls_maker.utils.KeyApp.RequestCode.STORAGE_PERMISSION_CODE
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.example.st129_gravityfalls_maker.utils.SystemUtils.notificationPermission
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setNotificationPermission
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setStoragePermission
import com.example.st129_gravityfalls_maker.utils.SystemUtils.storagePermission

class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {
    override fun setViewBinding(): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initData()
    }

    override fun viewListener() {
        binding.apply {
            switchPermission.setOnSingleClick {
                if (checkPermissions(storagePermission)) {
                    Toast.makeText(this@PermissionActivity, R.string.granted_storage, Toast.LENGTH_SHORT).show()
                } else {
                    if (SystemUtils.getStoragePermission(this@PermissionActivity) >= 2 && !checkPermissions(storagePermission)) {
                        goToSettings()
                    } else {
                        requestPermission(storagePermission, STORAGE_PERMISSION_CODE)
                    }
                }

            }

            switchNotification.setOnSingleClick {
                if (checkPermissions(notificationPermission)) {
                    Toast.makeText(
                        this@PermissionActivity, getString(R.string.granted_notification), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (SystemUtils.getNotificationPermission(this@PermissionActivity) >= 2) {
                        goToSettings()
                    } else {
                        requestPermission(notificationPermission, NOTIFICATION_PERMISSION_CODE)
                    }
                }
            }

            tvContinue.setOnSingleClick(1500) {
                startIntentAnim(HomeActivity::class.java)
                SystemUtils.setFirstPermission(this@PermissionActivity, false)
                finishAffinity()
            }
        }
    }

    override fun initText() {
        binding.apply {
            actionBar.tvCenter.select()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                txtPer.text = TextUtils.concat(
                    changeColor(
                        this@PermissionActivity, resources.getString(R.string.allow), R.color.white, R.font.roboto_medium
                    ), " ", changeColor(
                        this@PermissionActivity, resources.getString(R.string.app_name), R.color.white, R.font.roboto_medium
                    ), " ",

                    changeColor(
                        this@PermissionActivity, resources.getString(R.string.to_access_13), R.color.white, R.font.roboto_medium
                    )
                )
            } else {
                txtPer.text = TextUtils.concat(
                    changeColor(
                        this@PermissionActivity, resources.getString(R.string.allow), R.color.white, R.font.roboto_medium
                    ), " ", changeColor(
                        this@PermissionActivity, resources.getString(R.string.app_name), R.color.white, R.font.roboto_medium
                    ), " ", changeColor(
                        this@PermissionActivity, resources.getString(R.string.to_access), R.color.white, R.font.roboto_medium
                    )
                )
            }
        }
    }

    private fun initData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            binding.btnStorage.show()
            binding.btnNotification.gone()
        } else {
            binding.btnNotification.show()
            binding.btnStorage.gone()
        }
    }

    private fun changeColor(
        context: Context,
        text: String,
        color: Int,
        fontfamily: Int,
    ): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(context.getColor(color)), 0, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val font = ResourcesCompat.getFont(context, fontfamily)
        val typefaceSpan = CustomTypefaceSpan("", font)
        spannableString.setSpan(
            typefaceSpan, 0, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    class CustomTypefaceSpan(private val family: String, private val typeface: Typeface?) : TypefaceSpan(family) {

        override fun updateDrawState(ds: TextPaint) {
            applyCustomTypeFace(ds, typeface)
        }

        override fun updateMeasureState(paint: TextPaint) {
            applyCustomTypeFace(paint, typeface)
        }

        private fun applyCustomTypeFace(paint: Paint, tf: Typeface?) {
            if (tf != null) {
                paint.typeface = tf
            } else {
                paint.typeface = Typeface.DEFAULT
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                binding.switchPermission.setImageResource(R.drawable.ic_sw_on)
                Toast.makeText(this, R.string.granted_storage, Toast.LENGTH_SHORT).show()
            } else {
                binding.switchPermission.setImageResource(R.drawable.ic_sw_off)
                setStoragePermission(this, (SystemUtils.getStoragePermission(this) + 1))
            }
        } else if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.switchNotification.setImageResource(R.drawable.ic_sw_on)
                Toast.makeText(this, R.string.granted_notification, Toast.LENGTH_SHORT).show()
            } else {
                R.drawable.ic_sw_off
                setNotificationPermission(this, (SystemUtils.getNotificationPermission(this) + 1))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if (checkPermissions(storagePermission)) {
            binding.switchPermission.setImageResource(R.drawable.ic_sw_on)
            setStoragePermission(this, 0)
        } else {
            binding.switchPermission.setImageResource(R.drawable.ic_sw_off)
        }
        if (checkPermissions(notificationPermission)) {
            binding.switchNotification.setImageResource(R.drawable.ic_sw_on)
            setNotificationPermission(this, 0)
        } else {
            binding.switchNotification.setImageResource(R.drawable.ic_sw_off)
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            tvCenter.text = getString(R.string.permission)
            tvCenter.show()
        }
    }
}