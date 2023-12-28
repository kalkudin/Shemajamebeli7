package com.example.presentation.home.model

data class Key(
    val number: Int?,
    val icon: Int?,
    val itemType: ItemType = when {
        number != null -> ItemType.NUMBER
        icon != null -> ItemType.IMAGE
        else -> throw IllegalArgumentException("Both number and iconPath cannot be null")
    },

    val itemFunctionality : ItemFunction
){
    enum class ItemType {
        NUMBER,
        IMAGE
    }
    enum class ItemFunction{
        SCAN,
        NONE,
        DELETE
    }
}

