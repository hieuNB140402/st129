package com.example.st129_gravityfalls_maker.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.example.st129_gravityfalls_maker.model.ColorModel
import com.example.st129_gravityfalls_maker.model.CustomizeModel
import com.example.st129_gravityfalls_maker.model.IntroModel
import com.example.st129_gravityfalls_maker.model.LanguageModel
import com.example.st129_gravityfalls_maker.model.LayerModel

import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.ASSET_MANAGER
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.AVATAR_ASSET
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_JPG
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_PNG
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.FIRST_WEBP
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.DATA
import com.example.st129_gravityfalls_maker.utils.KeyApp.KeyAssets.DATA_ASSET

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
        
    )

    var listImageFocus: ArrayList<Int> = arrayListOf(
        
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

    fun getAvatarAsset(context: Context): ArrayList<String> {
        val assetManager = context.assets
        val allAvatar = assetManager.list(AVATAR_ASSET)
        val sortedAvatar = sortAsset(allAvatar)
        val returnAvatar = ArrayList<String>()
        sortedAvatar!!.forEach {
            returnAvatar.add("$ASSET_MANAGER/$AVATAR_ASSET/$it")
        }

        return returnAvatar
    }

    fun getLayerAsset(context: Context): ArrayList<CustomizeModel> {
        val list: ArrayList<CustomizeModel> = arrayListOf()
        val assetManager = context.assets
        val allCharacter = assetManager.list(DATA)
        val allAvatar = getAvatarAsset(context)
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