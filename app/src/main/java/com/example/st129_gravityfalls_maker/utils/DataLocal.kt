package com.example.st129_gravityfalls_maker.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.model.ColorModel
import com.example.st129_gravityfalls_maker.model.CustomizeModel
import com.example.st129_gravityfalls_maker.model.IntroModel
import com.example.st129_gravityfalls_maker.model.LanguageModel
import com.example.st129_gravityfalls_maker.model.LayerModel
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.ASSET_MANAGER
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.AVATAR_ASSET
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.DATA
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.DATA_ASSET
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_JPG
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_PNG
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_WEBP

object DataLocal {
    fun getLanguageList(): ArrayList<LanguageModel> {
        return arrayListOf(
            LanguageModel("hi", "Hindi", R.drawable.ic_flag_hindi),
            LanguageModel("es", "Spanish", R.drawable.ic_flag_spanish),
            LanguageModel("fr", "French", R.drawable.ic_flag_french),
            LanguageModel("en", "English", R.drawable.ic_flag_english),
            LanguageModel("pt", "Portuguese", R.drawable.ic_flag_portugeese),
            LanguageModel("in", "Indonesian", R.drawable.ic_flag_indo),
            LanguageModel("de", "German", R.drawable.ic_flag_germani),
        )
    }

    val itemIntroList = listOf(
        IntroModel(R.drawable.img_intro_1, R.string.title_1), IntroModel(R.drawable.img_intro_2, R.string.title_2), IntroModel(R.drawable.img_intro_3, R.string.title_3)
    )

    var listImageNotFocus: ArrayList<Int> = arrayListOf(
        R.drawable.nav_body,
        R.drawable.nav_head,
        R.drawable.nav_eye,
        R.drawable.nav_iris,
        R.drawable.nav_eyebrow,
        R.drawable.nav_nose,
        R.drawable.nav_ear,
        R.drawable.nav_mouth,
        R.drawable.nav_beard,
        R.drawable.nav_facepaint,
        R.drawable.nav_sidehair,
        R.drawable.nav_fronthair,
        R.drawable.nav_backhair,
        R.drawable.nav_shirt,
        R.drawable.nav_jacket,
        R.drawable.nav_glass,
        R.drawable.nav_hand,
        R.drawable.nav_necklace,
        R.drawable.nav_earring,
        R.drawable.nav_sleeve,
        R.drawable.nav_accessories,
        R.drawable.nav_hat,
        R.drawable.nav_other,
        R.drawable.nav_wing,
    )

    var listImageFocus: ArrayList<Int> = arrayListOf(
        R.drawable.nav_sel_body,
        R.drawable.nav_sel_head,
        R.drawable.nav_sel_eye,
        R.drawable.nav_sel_iris,
        R.drawable.nav_sel_eyebrow,
        R.drawable.nav_sel_nose,
        R.drawable.nav_sel_ear,
        R.drawable.nav_sel_mouth,
        R.drawable.nav_sel_beard,
        R.drawable.nav_sel_facepaint,
        R.drawable.nav_sel_sidehair,
        R.drawable.nav_sel_fronthair,
        R.drawable.nav_sel_backhair,
        R.drawable.nav_sel_shirt,
        R.drawable.nav_sel_jacket,
        R.drawable.nav_sel_glass,
        R.drawable.nav_sel_hand,
        R.drawable.nav_sel_necklace,
        R.drawable.nav_sel_earring,
        R.drawable.nav_sel_sleeve,
        R.drawable.nav_sel_accessories,
        R.drawable.nav_sel_hat,
        R.drawable.nav_sel_other,
        R.drawable.nav_sel_wing,
    )

    var All_DATA: ArrayList<CustomizeModel> = arrayListOf()

    val avatarList: ArrayList<String> = arrayListOf()

    var isLoadFailAllAPI: Boolean = true

    private fun sortAsset(listFiles: Array<String>?): List<String>? {
        val sortedFiles = listFiles?.sortedWith(compareBy { fileName ->
            val matchResult = Regex("\\d+").find(fileName)
            matchResult?.value?.toIntOrNull() ?: Int.MAX_VALUE
        })
        return sortedFiles
    }

    fun getPathAsset(context: Context, path: String): ArrayList<String> {
        val assetManager = context.assets
        val allAvatar = assetManager.list(path)
        val sortedAvatar = sortAsset(allAvatar)
        val returnAvatar = ArrayList<String>()
        sortedAvatar!!.forEach {
            returnAvatar.add("$ASSET_MANAGER/$path/$it")
        }

        return returnAvatar
    }

    fun getLayerAsset(context: Context): ArrayList<CustomizeModel> {
        val list: ArrayList<CustomizeModel> = arrayListOf()
        val assetManager = context.assets
        val allCharacter = assetManager.list(DATA)
        val allAvatar = getPathAsset(context, AVATAR_ASSET)
        val sortedCharacter = sortAsset(allCharacter)
        sortedCharacter?.let {
            // SWORD1
            sortedCharacter.forEachIndexed { indexCharacter, character ->
                Log.d("nbhieu", "data: $indexCharacter")
                val nuggtsCharacter = CustomizeModel(character, allAvatar[indexCharacter])
                // accessories, body, ...
                val folderLayer = assetManager.list("$DATA/$character")
                // accessories
                folderLayer?.let {
                    folderLayer.forEachIndexed { indexLayer, layer ->
                        when (layer) {
                            KeyApp.NameFolderLayer.BODY -> { nuggtsCharacter.body = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.HEAD -> { nuggtsCharacter.head = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.EYE -> { nuggtsCharacter.eye = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.IRIS -> { nuggtsCharacter.iris = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.EYEBROW -> { nuggtsCharacter.eyebrow = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.NOSE -> { nuggtsCharacter.nose = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.EAR -> { nuggtsCharacter.ear = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.MOUTH -> { nuggtsCharacter.mouth = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.BEARD -> { nuggtsCharacter.beard = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.FACEPAINT -> { nuggtsCharacter.facepaint = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.SIDEHAIR -> { nuggtsCharacter.sidehair = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.FRONTHAIR -> { nuggtsCharacter.fronthair = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.BACKHAIR -> { nuggtsCharacter.backhair = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.SHIRT -> { nuggtsCharacter.shirt = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.JACKET -> { nuggtsCharacter.jacket = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.GLASS -> { nuggtsCharacter.glass = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.HAND -> { nuggtsCharacter.hand = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.NECKLACE -> { nuggtsCharacter.necklace = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.EARRING -> { nuggtsCharacter.earring = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.SLEEVE -> { nuggtsCharacter.sleeve = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.ACCESSORIES -> { nuggtsCharacter.accessories = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.HAT -> { nuggtsCharacter.hat = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.OTHER -> { nuggtsCharacter.other = getFileLayer(character, layer, assetManager) }
                            KeyApp.NameFolderLayer.WING -> { nuggtsCharacter.wing = getFileLayer(character, layer, assetManager) }
                        }
                    }
                }
                list.add(nuggtsCharacter)
            }
        }

        return list
    }

    private fun getFileLayer(character: String, layer: String, assetManager: AssetManager): ArrayList<LayerModel> {
        val layerPath = ArrayList<LayerModel>()
        val fileLayer = assetManager.list("$DATA/$character/$layer")
        fileLayer?.let {
            if (fileLayer[0] == FIRST_PNG || fileLayer[0] == FIRST_JPG || fileLayer[0] == FIRST_WEBP) {
                val sortedFile = sortAsset(fileLayer)
                sortedFile?.let {
                    layerPath.addAll(getDataNoColor(character, sortedFile, layer))
                }
            } else {
                layerPath.addAll(getDataColor(assetManager, character, fileLayer, layer))
            }
        }
        return layerPath
    }

    private fun getDataNoColor(character: String, filesList: List<String>, folder: String): ArrayList<LayerModel> {
        val layerPath = ArrayList<LayerModel>()
        for (fileName in filesList) {
            // file:///android_asset/nuggts/ + nuggts1 + body + 1.png
            layerPath.add(LayerModel(image = "$DATA_ASSET$character/$folder/$fileName", isMoreColors = false, listColor = arrayListOf()))
        }
        return layerPath
    }

    private fun getDataColor(assetManager: AssetManager, character: String, folderList: Array<String>?, folder: String): ArrayList<LayerModel> {
        val colorNames = folderList!!.map { "#$it" }
        val fileList = folderList.map { colorFolder ->
            assetManager.list("$DATA/$character/$folder/$colorFolder")?.let { sortAsset(it) }?.map { "$DATA_ASSET$character/$folder/$colorFolder/$it" } ?: emptyList()
        }

        // Khởi tạo danh sách màu và ghép danh sách file theo index
        val colorList = Array(fileList.first().size) { index ->
            Array(folderList.size) { folderIndex ->
                ColorModel(color = colorNames[folderIndex], path = fileList[folderIndex][index])
            }.toCollection(ArrayList())
        }.toCollection(ArrayList())

        return fileList.first().mapIndexed { index, file ->
            LayerModel(image = file, isMoreColors = true, listColor = colorList[index])
        }.toCollection(ArrayList())
    }
    
}

