package com.example.st129_gravityfalls_maker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.base.BaseCustomActivity
import com.example.st129_gravityfalls_maker.databinding.ActivityCustomizeBinding
import com.example.st129_gravityfalls_maker.dialog.ConfirmDialog
import com.example.st129_gravityfalls_maker.extionsion.createBimapFromView
import com.example.st129_gravityfalls_maker.extionsion.eLog
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.handleBack
import com.example.st129_gravityfalls_maker.extionsion.hide
import com.example.st129_gravityfalls_maker.extionsion.hideNavigation
import com.example.st129_gravityfalls_maker.extionsion.saveBitmapToInternalStorage
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.extionsion.startIntentAnim
import com.example.st129_gravityfalls_maker.model.ItemColorModel
import com.example.st129_gravityfalls_maker.model.ItemNavCustomModel
import com.example.st129_gravityfalls_maker.utils.DataLocal.All_DATA
import com.example.st129_gravityfalls_maker.utils.DataLocal.listImageFocus
import com.example.st129_gravityfalls_maker.utils.DataLocal.listImageNotFocus
import com.example.st129_gravityfalls_maker.utils.KeyApp
import com.example.st129_gravityfalls_maker.utils.KeyApp.DOWNLOAD_ALBUM_BACKGROUND
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyIntent.INTENT_KEY
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.CUSTOM_BODY
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.NAV_BODY
import com.example.st129_gravityfalls_maker.utils.KeyApp.PositionLayer.QUANTITY_LAYER
import com.example.st129_gravityfalls_maker.utils.SystemUtils.setLocale
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CustomizeActivity : BaseCustomActivity<ActivityCustomizeBinding>() {
    override fun setViewBinding(): ActivityCustomizeBinding {
        return ActivityCustomizeBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initList()
        initRcv()
        initData()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.apply {
                btnActionBarLeft.setOnSingleClick { finishActivity() }
                btnActionBarCenter.setOnSingleClick { handleReset() }
                btnActionBarRight.setOnSingleClick { handleSave() }
            }
            btnRandom.setOnSingleClick { handleRandomFullLayer() }
            btnFlip.setOnSingleClick { handleFlip() }
            btnHide.setOnSingleClick { handleHide() }
        }
        handleRcv()
        handleNav()
    }

    override fun initText() {
        binding.apply {}
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            btnActionBarLeft.setImageResource(R.drawable.ic_back)
            btnActionBarLeft.show()
            btnActionBarCenter.setImageResource(R.drawable.ic_reset)
            btnActionBarCenter.show()
            btnActionBarRight.setImageResource(R.drawable.ic_next)
            btnActionBarRight.show()
        }
    }

    private fun initList() {
        binding.apply {
            listNav = arrayListOf(
                navBody,
                navHead,
                navEye,
                navIris,
                navEyebrow,
                navNose,
                navEar,
                navMouth,
                navBeard,
                navFacepaint,
                navSidehair,
                navFronthair,
                navBackhair,
                navShirt,
                navJacket,
                navGlass,
                navHand,
                navNecklace,
                navEarring,
                navSleeve,
                navAccessories,
                navHat,
                navOther,
                navWing,
            )

            repeat(QUANTITY_LAYER) {
                listPositionColorItem.add(0)
                listIsSelectedItem.add(false)
                listKeySelectedItem.add("")
                listPartSelected.add("")
            }

            hideList = arrayListOf(layoutNav, layoutRcv, layoutRcvColor)
            btnHide.tag = R.drawable.ic_show
        }
    }

    private fun initRcv() {
        binding.apply {
            rcvLayer.adapter = customizeLayerAdapter
            rcvLayer.itemAnimator = null

            rcvColor.adapter = colorLayerAdapter
            rcvColor.itemAnimator = null
        }
    }

    private fun initData() {
        binding.apply {
            val avatarPosition = intent.getIntExtra(INTENT_KEY, 0)
            setLocale(this@CustomizeActivity)
            loading.show()
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                eLog("initData: ${throwable.message}")
                loading.dismiss()
                CoroutineScope(Dispatchers.Main).launch {
                    loading.dismiss()
                    val dialogExit = ConfirmDialog(
                        this@CustomizeActivity, R.string.error, R.string.an_error_occurred
                    )
                    dialogExit.show()
                    dialogExit.onNoClick = {
                        dialogExit.dismiss()
                        hideNavigation(true)
                        finish()
                    }
                    dialogExit.onYesClick = {
                        dialogExit.dismiss()
                        hideNavigation(true)
                        startIntentAnim(CustomizeActivity::class.java, avatarPosition)
                        finish()
                    }
                }
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                var bitmapDefault: Bitmap? = null
                // Get data activity
                val job1 = async {
                    dataActivity = All_DATA[avatarPosition]
                    avatarId = All_DATA[avatarPosition].avatar
                    return@async true
                }
                // Get data from list
                val job2 = async {
                    if (job1.await()) {
                        val attributes = arrayListOf(
                            dataActivity!!.body,
                            dataActivity!!.head,
                            dataActivity!!.eye,
                            dataActivity!!.iris,
                            dataActivity!!.eyebrow,
                            dataActivity!!.nose,
                            dataActivity!!.ear,
                            dataActivity!!.mouth,
                            dataActivity!!.beard,
                            dataActivity!!.facepaint,
                            dataActivity!!.sidehair,
                            dataActivity!!.fronthair,
                            dataActivity!!.backhair,
                            dataActivity!!.shirt,
                            dataActivity!!.jacket,
                            dataActivity!!.glass,
                            dataActivity!!.hand,
                            dataActivity!!.necklace,
                            dataActivity!!.earring,
                            dataActivity!!.sleeve,
                            dataActivity!!.accessories,
                            dataActivity!!.hat,
                            dataActivity!!.other,
                            dataActivity!!.wing,
                        )
                        attributes.forEach { listCategory.add(it) }

                        listCategory.forEachIndexed { index, layerModels ->
                            if (index == 0) {
                                listItemNav.add(createListItemBody(layerModels))
                            } else {
                                listItemNav.add(createListItem(layerModels))
                            }
                        }


                        for (itemParent in listItemNav) {
                            itemParent.forEachIndexed { index, item ->
                                item.isSelected = index == 0
                            }
                        }

                        listItemNav[0][0].isSelected = false
                        listItemNav[0][1].isSelected = true
                    }
                    return@async true
                }
                // Color
                val job3 = async {
                    if (job1.await() && job2.await()) {
                        for (i in 0 until listCategory.size) {
                            // Lấy đối tượng LayerModel đầu tiên trong danh sách con
                            if (listCategory[i].isNotEmpty()) {
                                val currentLayer = listCategory[i][0]
                                var firstIndex = true
                                // Kiểm tra isMoreColors để thêm màu hoặc danh sách rỗng
                                if (currentLayer.isMoreColors) {
                                    val colorList = ArrayList<ItemColorModel>()
                                    for (j in 0 until currentLayer.listColor.size) {
                                        val color = currentLayer.listColor[j].color
                                        if (firstIndex) {
                                            colorList.add(ItemColorModel(color, true))
                                        } else {
                                            colorList.add(ItemColorModel(color))
                                        }
                                        firstIndex = false
                                    }
                                    listColorItemNav.add(colorList)
                                } else {
                                    listColorItemNav.add(arrayListOf())
                                }
                            } else {
                                listColorItemNav.add(arrayListOf())
                            }
                        }
                    }
                    return@async true
                }
                // Add custom view in FrameLayout
                val job4 = async(Dispatchers.Main) {
                    if (job1.await() && job2.await() && job3.await()) {
                        listCustomView.addAll(
                            addImageViewMatchParent(
                                QUANTITY_LAYER, layoutCustomLayer
                            )
                        )
                    }
                    return@async true
                }
                // Remove custom view in FrameLayout & Hide Navigation
                val job5 = async(Dispatchers.Main) {
                    if (job1.await() && job2.await() && job3.await() && job4.await()) {
                        for (j in 0 until listCategory.size) {
                            if (listCategory[j].isEmpty()) {
                                listNav[j].gone()
                                layoutCustomLayer.removeView(listCustomView[setUpPositionCustom(j)])
                            }
                        }
                    }
                    return@async true
                }
                // Fill data default
                val job6 = async {
                    if (job1.await() && job2.await() && job3.await() && job4.await() && job5.await()) {
                        val imageDefaultBoy = listCategory[0][0].image
                        bitmapDefault =
                            Glide.with(this@CustomizeActivity).load(imageDefaultBoy).submit().get()
                                .toBitmap()
                        listPartSelected[positionCustom] = imageDefaultBoy
                        listKeySelectedItem[positionNavSelected] = imageDefaultBoy
                        listIsSelectedItem[positionNavSelected] = true
                    }
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await() && job2.await() && job3.await() && job4.await() && job5.await() && job6.await()) {
                        listCustomView[CUSTOM_BODY].setImageBitmap(bitmapDefault)
                        customizeLayerAdapter.submitList(listItemNav[NAV_BODY])
                        listNav[NAV_BODY].setImageResource(listImageFocus[NAV_BODY])
                        colorLayerAdapter.submitList(listColorItemNav[NAV_BODY])
                        loading.dismiss()
                        hideNavigation(true)
                    }
                }
            }

        }
    }

    private fun handleRcv() {
        // Layer
        customizeLayerAdapter.onItemClick = { item, position -> handleFillLayer(item, position) }
        customizeLayerAdapter.onNoneClick = { position -> handleNoneLayer(position) }
        customizeLayerAdapter.onRandomClick = { handleRandomLayer() }

        colorLayerAdapter.onItemClick = { position -> handleChangeColorLayer(position) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleFillLayer(item: ItemNavCustomModel, position: Int) {
        var bitmap: Bitmap? = null
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("handleFillLayer: ${throwable.message}")
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                try {
                    val path = item.path
                    listKeySelectedItem[positionNavSelected] = path
                    if (item.listImageColor.isEmpty()) {
                        bitmap =
                            Glide.with(this@CustomizeActivity).load(path).submit().get().toBitmap()
                        listPartSelected[positionCustom] = path
                    } else {
                        val pathColor =
                            item.listImageColor[listPositionColorItem[positionNavSelected]].path
                        bitmap = Glide.with(this@CustomizeActivity).load(pathColor).submit().get()
                            .toBitmap()
                        listPartSelected[positionCustom] = pathColor
                    }

                    return@async true
                } catch (e: Exception) {
                    eLog("handlePath_1: ${e.message}")
                    return@async false
                }
            }

            val job2 = async {
                try {
                    if (job1.await()) {
                        listIsSelectedItem[positionNavSelected] = true
                        for (i in 0 until listItemNav[positionNavSelected].size) {
                            listItemNav[positionNavSelected][i].isSelected = i == position
                        }

                    }
                    return@async true
                } catch (e: Exception) {
                    eLog("handlePath_2: ${e.message}")
                    return@async false
                }
            }

            launch(Dispatchers.Main) {
                try {
                    if (job1.await() && job2.await()) {
                        listCustomView[positionCustom].setImageBitmap(bitmap)
                        if (isFlip) {
                            listCustomView[positionCustom].flipImage()
                        }
                        customizeLayerAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    eLog("handlePath_launch: ${e.message}")
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleNoneLayer(position: Int) {
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("handleNoneLayer: ${throwable.message}")
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                listIsSelectedItem[positionNavSelected] = false
                listKeySelectedItem[positionNavSelected] = ""
                listPartSelected[positionCustom] = ""
                for (i in 0 until listItemNav[positionNavSelected].size) {
                    listItemNav[positionNavSelected][i].isSelected = i == position
                }

                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await()) {
                    customizeLayerAdapter.notifyDataSetChanged()
                    listCustomView[positionCustom].setImageBitmap(null)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRandomLayer() {
        if (positionNavSelected == 0) {
            handleRandomLayerBody()
            return
        }

        val randomLayer = (2..<listItemNav[positionNavSelected].size).random()

        var randomColor: Int? = null

        var isMoreColors = false

        if (listItemNav[positionNavSelected][2].listImageColor.isNotEmpty()) {
            isMoreColors = true
            randomColor = (0..<listItemNav[positionNavSelected][2].listImageColor.size).random()
        }

        var bitmap: Bitmap? = null
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("handleRandom: ${throwable.message}")
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                val pathRandom = listItemNav[positionNavSelected][randomLayer].path
                listKeySelectedItem[positionNavSelected] = pathRandom
                if (!isMoreColors) {
                    listPositionColorItem[positionNavSelected] = 0
                    bitmap = Glide.with(this@CustomizeActivity).load(pathRandom).submit().get()
                        .toBitmap()
                    listPartSelected[positionCustom] = pathRandom
                } else {
                    val pathRandomColor =
                        listItemNav[positionNavSelected][randomLayer].listImageColor[randomColor!!].path
                    listPositionColorItem[positionNavSelected] = randomColor
                    bitmap = Glide.with(this@CustomizeActivity).load(pathRandomColor).submit().get()
                        .toBitmap()
                    listPartSelected[positionCustom] = pathRandomColor
                }
                return@async true
            }
            val job2 = async {
                if (job1.await()) {
                    listItemNav[positionNavSelected].forEachIndexed { index, itemNavCustomModel ->
                        itemNavCustomModel.isSelected = index == randomLayer
                    }

                    if (isMoreColors) {
                        for (i in 0 until listColorItemNav[positionNavSelected].size) {
                            listColorItemNav[positionNavSelected][i].isSelected = i == randomColor
                        }
                    }
                }
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job2.await()) {
                    listCustomView[setUpPositionCustom(positionNavSelected)].setImageBitmap(bitmap)
                    customizeLayerAdapter.notifyDataSetChanged()
                    if (isFlip) {
                        listCustomView[setUpPositionCustom(positionNavSelected)].flipImage()
                    }
                    if (isMoreColors) {
                        colorLayerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRandomLayerBody() {
        var randomLayer = 1
        randomLayer = if (listItemNav[positionNavSelected].size == 1) {
            1
        } else {
            (1..<listItemNav[positionNavSelected].size).random()
        }

        var randomColor: Int? = null

        var isMoreColors = false

        if (listItemNav[positionNavSelected][1].listImageColor.isNotEmpty()) {
            isMoreColors = true
            randomColor = (0..<listItemNav[positionNavSelected][1].listImageColor.size).random()
        }

        var bitmap: Bitmap? = null
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("handleRandomBody: ${throwable.message}")
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                val pathRandomBody = listItemNav[positionNavSelected][randomLayer].path
                listKeySelectedItem[positionNavSelected] = pathRandomBody

                if (!isMoreColors) {
                    listPositionColorItem[positionNavSelected] = 0
                    bitmap = Glide.with(this@CustomizeActivity).load(pathRandomBody).submit().get()
                        .toBitmap()
                    listPartSelected[positionCustom] = pathRandomBody
                } else {
                    listPositionColorItem[positionNavSelected] = randomColor!!
                    val pathRandomBodyColor =
                        listItemNav[positionNavSelected][randomLayer].listImageColor[randomColor].path

                    bitmap =
                        Glide.with(this@CustomizeActivity).load(pathRandomBodyColor).submit().get()
                            .toBitmap()
                    listPartSelected[positionCustom] = pathRandomBodyColor
                }

                return@async true
            }
            val job2 = async {
                if (job1.await()) {
                    listItemNav[positionNavSelected].forEachIndexed { index, itemNavCustomModel ->
                        itemNavCustomModel.isSelected = index == randomLayer
                    }
                    if (isMoreColors) {
                        for (i in 0 until listColorItemNav[positionNavSelected].size) {
                            listColorItemNav[positionNavSelected][i].isSelected = i == randomColor
                        }
                    }
                }
                return@async true
            }
            launch(Dispatchers.Main) {
                if (job2.await()) {
                    listCustomView[setUpPositionCustom(positionNavSelected)].setImageBitmap(bitmap)
                    customizeLayerAdapter.notifyDataSetChanged()
                    if (isFlip) {
                        listCustomView[setUpPositionCustom(positionNavSelected)].flipImage()
                    }
                    if (isMoreColors) {
                        colorLayerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleChangeColorLayer(position: Int) {
        var bitmap: Bitmap? = null
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("changeColor: ${throwable.message}")
        }

        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                try {
                    listPositionColorItem[positionNavSelected] = position
                    // Đã chọn hình ảnh chưa

                    if (listKeySelectedItem[positionNavSelected] != "") {
                        // Duyệt qua từng item trong bộ phận
                        for (item in listCategory[positionNavSelected]) {
                            if (item.image == listKeySelectedItem[positionNavSelected]) {
                                val pathColor = item.listColor[position].path
                                bitmap = Glide.with(this@CustomizeActivity).load(pathColor).submit()
                                    .get().toBitmap()
                                listPartSelected[positionCustom] = pathColor
                            }
                        }
                    }

                    return@async true
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@async false
                }
            }

            val job2 = async {
                try {
                    if (job1.await()) {
                        for (i in 0 until listColorItemNav[positionNavSelected].size) {
                            listColorItemNav[positionNavSelected][i].isSelected = i == position
                        }
                    }
                    return@async true
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@async false
                }
            }

            launch(Dispatchers.Main) {
                if (job1.await() && job2.await()) {
                    listCustomView[positionCustom].setImageBitmap(bitmap)
                    if (isFlip) {
                        listCustomView[positionCustom].flipImage()
                    }
                    colorLayerAdapter.notifyDataSetChanged()
                    hideNavigation(true)
                }
            }
        }
    }

    private fun handleNav() {
        binding.apply {
            listNav.forEachIndexed { index, navButton ->
                navButton.setOnClickListener {
                    if (currentIndex != index) {
                        updateNavFocus(index)
                        updateAdapters()
                    }
                }
            }
        }
    }

    private fun updateNavFocus(index: Int) {
        listNav[currentIndex].setImageResource(listImageNotFocus[currentIndex])
        listNav[index].setImageResource(listImageFocus[index])
        currentIndex = index
        positionNavSelected = index
        positionCustom = setUpPositionCustom(positionNavSelected)
    }

    private fun updateAdapters() {
        try {
            customizeLayerAdapter.submitList(listItemNav[positionNavSelected])
            colorLayerAdapter.submitList(listColorItemNav[positionNavSelected])
            listItemNav[positionNavSelected].forEachIndexed { index, itemItemModel ->
                if (itemItemModel.isSelected) {
                    binding.rcvLayer.smoothScrollToPosition(index)
                }
            }
            listColorItemNav[positionNavSelected].forEachIndexed { index, itemColorModel ->
                if (itemColorModel.isSelected) {
                    binding.rcvColor.smoothScrollToPosition(index)
                }
            }
        } catch (e: Exception) {
            Log.e("nbhieu", "updateAdapters: ${e.message}")
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleReset() {
        var bitmapDefault: Bitmap? = null
        val dialog =
            ConfirmDialog(this@CustomizeActivity, R.string.reset, R.string.do_you_want_to_reset)
        setLocale(this)
        dialog.show()

        dialog.binding.btnYes.setOnSingleClick {
            dialog.dismiss()
            setLocale(this)
            loading.show()
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                eLog("handleReset: ${throwable.message}")
                loading.dismiss()
                hideNavigation(true)
            }

            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                // Set image null
                val job1 = async(Dispatchers.Main) {
                    for (i in 0 until listCustomView.size) {
                        listCustomView[i].setImageBitmap(null)
                    }
                    return@async true
                }
                // Reset data + focus
                val job2 = async {
                    if (job1.await()) {
                        listPositionColorItem.clear()
                        listIsSelectedItem.clear()
                        listKeySelectedItem.clear()
                        listPartSelected.clear()

                        repeat(QUANTITY_LAYER) {
                            listPositionColorItem.add(0)
                            listIsSelectedItem.add(false)
                            listKeySelectedItem.add("")
                            listPartSelected.add("")
                        }

                        for (itemParent in listItemNav) {
                            itemParent.forEachIndexed { index, item ->
                                item.isSelected = index == 0
                            }
                        }

                        for (itemParent in listColorItemNav) {
                            var selectFirst = true
                            for (item in itemParent) {
                                item.isSelected = selectFirst
                                selectFirst = false
                            }
                        }
                        listItemNav[0][0].isSelected = false
                        listItemNav[0][1].isSelected = true
                    }
                    return@async true
                }
                // Set layer default
                val job3 = async {
                    if (job2.await()) {
                        val pathDefaultBoy = listCategory[0][0].image
                        bitmapDefault =
                            Glide.with(this@CustomizeActivity).load(pathDefaultBoy).submit().get()
                                .toBitmap()
                        listPartSelected[CUSTOM_BODY] = pathDefaultBoy
                    }
                    return@async true
                }
                // Attach default
                val job4 = async {
                    if (job3.await()) {
                        listKeySelectedItem[0] = listCategory[0][0].image
                        listPartSelected[CUSTOM_BODY] = listCategory[0][0].image
                        listIsSelectedItem[0] = true
                    }
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await() && job2.await() && job3.await() && job4.await()) {
                        listCustomView[CUSTOM_BODY].setImageBitmap(bitmapDefault)
                        isFlip = false
                        customizeLayerAdapter.notifyDataSetChanged()
                        colorLayerAdapter.notifyDataSetChanged()
                        loading.dismiss()
                        hideNavigation(true)
                    }
                }
            }
        }

        dialog.binding.btnNo.setOnClickListener {
            dialog.dismiss()
            hideNavigation(true)
        }
    }

    private fun handleFlip() {
        setLocale(this@CustomizeActivity)
        loading.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("Random_All: ${throwable.message}")
            loading.dismiss()
            hideNavigation(true)
        }
        CoroutineScope(SupervisorJob() + Dispatchers.Main + handleExceptionCoroutine).launch {
            launch {
                for (i in 0 until listCustomView.size) {
                    if (listPartSelected[i] != "") {
                        listCustomView[i].flipImage()
                    }
                }
                isFlip = !isFlip
                loading.dismiss()
                hideNavigation(true)
            }
        }
    }

    private fun handleHide() {
        binding.apply {
            if (btnHide.tag == R.drawable.ic_show) {
                btnHide.tag = R.drawable.ic_hide
                btnHide.setImageResource(R.drawable.ic_hide)
                hideList.forEach {
                    it.hide()
                }

            } else {
                btnHide.tag = R.drawable.ic_show
                btnHide.setImageResource(R.drawable.ic_show)
                for (i in 0 until hideList.size) {
                    hideList[i].show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRandomFullLayer() {
        val listBitmap: ArrayList<Bitmap?> = arrayListOf()
        setLocale(this@CustomizeActivity)
        loading.show()
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("Random_All: ${throwable.message}")
            loading.dismiss()
            hideNavigation(true)
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            val job1 = async {
                for (i in 0 until listNav.size) {
                    if (i != 0) {
                        if (listItemNav[i].size == 2) {
                            listBitmap.add(null)
                            continue
                        }
                        val randomLayer = (2..<listItemNav[i].size).random()

                        var randomColor: Int? = null

                        var isMoreColors = false

                        if (listItemNav[i][2].listImageColor.isNotEmpty()) {
                            isMoreColors = true
                            randomColor = (0..<listItemNav[i][2].listImageColor.size).random()
                        }
                        listKeySelectedItem[i] = listItemNav[i][randomLayer].path


                        if (!isMoreColors) {
                            val pathItem = listItemNav[i][randomLayer].path
                            listPositionColorItem[i] = 0
                            listBitmap.add(
                                Glide.with(this@CustomizeActivity).load(pathItem).submit().get()
                                    .toBitmap()
                            )
                            listPartSelected[setUpPositionCustom(i)] = pathItem
                        } else {
                            listPositionColorItem[i] = randomColor!!
                            val pathItemColor =
                                listItemNav[i][randomLayer].listImageColor[randomColor].path
                            listBitmap.add(
                                Glide.with(this@CustomizeActivity).load(pathItemColor).submit()
                                    .get().toBitmap()
                            )
                            listPartSelected[setUpPositionCustom(i)] = pathItemColor

                        }

                        listItemNav[i].forEachIndexed { index, itemNavCustomModel ->
                            itemNavCustomModel.isSelected = index == randomLayer
                        }
                        if (isMoreColors) {
                            for (j in 0 until listColorItemNav[i].size) {
                                listColorItemNav[i][j].isSelected = j == randomColor
                            }
                        }

                    } else {
                        val randomLayer = if (listItemNav[0].size == 1) {
                            1
                        } else {
                            (1..<listItemNav[0].size).random()
                        }

                        var randomColor: Int? = null

                        var isMoreColors = false
                        listKeySelectedItem[0] = listItemNav[0][randomLayer].path
                        if (listItemNav[0][1].listImageColor.isNotEmpty()) {
                            isMoreColors = true
                            randomColor = (0..<listItemNav[0][1].listImageColor.size).random()
                        }
                        listKeySelectedItem[i] = listItemNav[0][randomLayer].path
                        if (!isMoreColors) {
                            listPositionColorItem[positionNavSelected] = 0
                            listBitmap.add(
                                Glide.with(this@CustomizeActivity)
                                    .load(listItemNav[0][randomLayer].path).submit().get()
                                    .toBitmap()
                            )
                            listPartSelected[setUpPositionCustom(i)] =
                                listItemNav[i][randomLayer].path
                        } else {
                            listPositionColorItem[i] = randomColor!!
                            listBitmap.add(
                                Glide.with(this@CustomizeActivity).load(
                                    listItemNav[0][randomLayer].listImageColor[randomColor].path
                                ).submit().get().toBitmap()
                            )
                            listPartSelected[setUpPositionCustom(i)] =
                                listItemNav[i][randomLayer].listImageColor[randomColor].path
                        }

                        listItemNav[i].forEachIndexed { index, itemNavCustomModel ->
                            itemNavCustomModel.isSelected = index == randomLayer
                        }

                        if (isMoreColors) {
                            for (j in 0 until listColorItemNav[i].size) {
                                listColorItemNav[i][j].isSelected = j == listPositionColorItem[i]
                            }
                        }
                    }
                }

                return@async true
            }
            launch(Dispatchers.Main) {
                if (job1.await()) {
                    for (i in 0 until listNav.size) {
                        if (listBitmap[i] != null) {
                            listCustomView[setUpPositionCustom(i)].setImageBitmap(listBitmap[i])
                        }
                    }
                    isFlip = false
                    customizeLayerAdapter.notifyDataSetChanged()
                    colorLayerAdapter.notifyDataSetChanged()
                    loading.dismiss()
                    hideNavigation(true)
                }
            }
        }
    }

    private fun handleSave() {
        binding.apply {
            setLocale(this@CustomizeActivity)
            loading.show()
            var pathInternal = ""
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                eLog("e: ${throwable.message}")
                loading.dismiss()
                hideNavigation(true)
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    pathInternal = saveBitmapToInternalStorage(
                        DOWNLOAD_ALBUM_BACKGROUND, createBimapFromView(layoutCustomLayer)
                    ).toString()
                    return@async true
                }

                launch(Dispatchers.Main) {
                    if (job1.await()) {
                        loading.dismiss()
                        hideNavigation(true)
                        val intent = Intent(this@CustomizeActivity, BackgroundActivity::class.java)
                        intent.putExtra(KeyApp.KeyIntent.INTENT_KEY, pathInternal)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun finishActivity() {
        val dialog = ConfirmDialog(this, R.string.exit, R.string.do_you_want_to_exit)
        setLocale(this)
        dialog.show()
        dialog.binding.btnYes.setOnSingleClick {
            dialog.dismiss()
            hideNavigation(true)
            handleBack()
        }
        dialog.binding.btnNo.setOnClickListener() {
            dialog.dismiss()
            hideNavigation(true)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishActivity()
    }

    override fun onResume() {
        super.onResume()
        hideNavigation(true)
    }
}