package com.example.st129_gravityfalls_maker.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.st129_gravityfalls_maker.adapter.CustomizeLayerAdapter
import com.example.st129_gravityfalls_maker.adapter.ItemColorLayerAdapter
import com.example.st129_gravityfalls_maker.custom.imageview.CustomImageView
import com.example.st129_gravityfalls_maker.dialog.LoadingDialog
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.model.CustomizeModel
import com.example.st129_gravityfalls_maker.model.ItemColorImageModel
import com.example.st129_gravityfalls_maker.model.ItemColorModel
import com.example.st129_gravityfalls_maker.model.ItemNavCustomModel
import com.example.st129_gravityfalls_maker.model.LayerModel
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.AVATAR_ASSET
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_ACCESSORIES
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_BACKHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_BEARD
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_BODY
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_EAR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_EARRING
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_EYE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_EYEBROW
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_FACEPAINT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_FRONTHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_GLASS
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_HAND
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_HAT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_HEAD
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_IRIS
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_JACKET
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_MOUTH
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_NECKLACE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_NOSE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_OTHER
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_SHIRT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_SIDEHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_SLEEVE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_WING
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_ACCESSORIES
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_BACKHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_BEARD
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_BODY
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_EAR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_EARRING
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_EYE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_EYEBROW
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_FACEPAINT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_FRONTHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_GLASS
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_HAND
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_HAT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_HEAD
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_IRIS
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_JACKET
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_MOUTH
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_NECKLACE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_NOSE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_OTHER
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_SHIRT
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_SIDEHAIR
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_SLEEVE
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_WING
import com.example.st129_gravityfalls_maker.utils.SystemUtils


abstract class BaseCustomActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var binding: T

    protected abstract fun setViewBinding(): T

    protected abstract fun initView()

    protected abstract fun viewListener()

    open fun dataObservable() {}

    protected abstract fun initText()

    protected abstract fun initActionBar()

    open fun initAds() {}
    val customizeLayerAdapter by lazy {
        CustomizeLayerAdapter(this)
    }

    val colorLayerAdapter by lazy {
        ItemColorLayerAdapter(this)
    }

    // Dialog Loading
    protected lateinit var loading: LoadingDialog

    // Vị trí botton nav đang chọn
    protected var positionNavSelected: Int = NAV_BODY

    // Vị trí layer custom
    protected var positionCustom: Int = CUSTOM_BODY

    // Vị trí được chọn gần đây
    protected var currentIndex: Int = NAV_BODY

    // Danh sách button bottom navigation
    protected var listNav: ArrayList<ImageView> = arrayListOf()

    protected val listCustomView: ArrayList<CustomImageView> = arrayListOf()

    // Vị trí màu đã được chọn của từng bộ phận
    protected var listPositionColorItem: ArrayList<Int> = arrayListOf()

    // Danh sách item đã được chọn hay chưa
    protected var listIsSelectedItem: ArrayList<Boolean> = arrayListOf()

    // avatar: id
    protected var avatarId: String = ""

    // Data của màn
    protected var dataActivity: CustomizeModel? = null

    // Danh sách hình ảnh trong từng thành phần
    protected var listItemNav: ArrayList<ArrayList<ItemNavCustomModel>> = arrayListOf()

    // Danh sách các list của mỗi thành phần
    protected var listCategory = ArrayList<ArrayList<LayerModel>>()

    // Danh sách màu của từng bộ phận
    protected var listColorItemNav: ArrayList<ArrayList<ItemColorModel>> = arrayListOf()

    // Danh sách key item đã chọn của từng bộ phận
    protected var listKeySelectedItem: ArrayList<String> = arrayListOf()

    protected var listPartSelected: ArrayList<String> = arrayListOf()

    protected var countRandom = 0

    protected var hideList = ArrayList<View>()

    // Trạng thái đảo ngược
    protected var isFlip: Boolean = false

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtils.setLocale(this)
        binding = setViewBinding()
        setContentView(binding.root)
        loading = LoadingDialog(this)
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

    fun createListItemBody(layers: ArrayList<LayerModel>): ArrayList<ItemNavCustomModel> {
        val listItem = arrayListOf<ItemNavCustomModel>()
        listItem.add(ItemNavCustomModel(layers[0].image))
        for (layer in layers) {
            if (!layer.isMoreColors) {
                listItem.add(ItemNavCustomModel(layer.image))
            } else {
                val listItemColor = ArrayList<ItemColorImageModel>()
                for (colorModel in layer.listColor) {
                    listItemColor.add(
                        ItemColorImageModel(
                            color = colorModel.color, path = colorModel.path
                        )
                    )
                }
                listItem.add(
                    ItemNavCustomModel(
                        path = layer.image, isSelected = false, listImageColor = listItemColor
                    )
                )
            }
        }
        return listItem
    }

    fun createListItem(layers: ArrayList<LayerModel>): ArrayList<ItemNavCustomModel> {
        val listItem = arrayListOf<ItemNavCustomModel>()
        listItem.add(ItemNavCustomModel("$AVATAR_ASSET/${AVATAR_ASSET}/1.png", true))
        listItem.add(ItemNavCustomModel("$AVATAR_ASSET/${AVATAR_ASSET}/1.png", true))
        for (layer in layers) {
            if (!layer.isMoreColors) {
                listItem.add(ItemNavCustomModel(layer.image))
            } else {
                val listItemColor = ArrayList<ItemColorImageModel>()

                for (colorModel in layer.listColor) {
                    listItemColor.add(
                        ItemColorImageModel(
                            color = colorModel.color, path = colorModel.path
                        )
                    )
                }
                listItem.add(
                    ItemNavCustomModel(
                        path = layer.image, isSelected = false, listImageColor = listItemColor
                    )
                )
            }
        }
        return listItem
    }

    fun addImageViewMatchParent(
        quantityLayer: Int,
        frameLayout: FrameLayout
    ): ArrayList<CustomImageView> {
        val customViewList = ArrayList<CustomImageView>()
        for (i in 0 until quantityLayer) {
            val imageView = CustomImageView(frameLayout.context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
            frameLayout.addView(imageView)
            customViewList.add(imageView)
        }
        return customViewList
    }

    fun setUpPositionCustom(input: Int): Int {
        val setUpPositionCustom = when (input) {
            NAV_BODY -> CUSTOM_BODY
            NAV_HEAD -> CUSTOM_HEAD
            NAV_EYE -> CUSTOM_EYE
            NAV_IRIS -> CUSTOM_IRIS
            NAV_EYEBROW -> CUSTOM_EYEBROW
            NAV_NOSE -> CUSTOM_NOSE
            NAV_EAR -> CUSTOM_EAR
            NAV_MOUTH -> CUSTOM_MOUTH
            NAV_BEARD -> CUSTOM_BEARD
            NAV_FACEPAINT -> CUSTOM_FACEPAINT
            NAV_SIDEHAIR -> CUSTOM_SIDEHAIR
            NAV_FRONTHAIR -> CUSTOM_FRONTHAIR
            NAV_BACKHAIR -> CUSTOM_BACKHAIR
            NAV_SHIRT -> CUSTOM_SHIRT
            NAV_JACKET -> CUSTOM_JACKET
            NAV_GLASS -> CUSTOM_GLASS
            NAV_HAND -> CUSTOM_HAND
            NAV_NECKLACE -> CUSTOM_NECKLACE
            NAV_EARRING -> CUSTOM_EARRING
            NAV_SLEEVE -> CUSTOM_SLEEVE
            NAV_ACCESSORIES -> CUSTOM_ACCESSORIES
            NAV_HAT -> CUSTOM_HAT
            NAV_OTHER -> CUSTOM_OTHER
            NAV_WING -> CUSTOM_WING
            else -> -1
        }
        return setUpPositionCustom
    }
}
