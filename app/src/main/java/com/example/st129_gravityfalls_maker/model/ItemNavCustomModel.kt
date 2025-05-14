package com.example.st129_gravityfalls_maker.model

data class ItemNavCustomModel (
    val path: String,
    var isSelected: Boolean = false,
    val listImageColor: ArrayList<ItemColorImageModel> = arrayListOf()
)