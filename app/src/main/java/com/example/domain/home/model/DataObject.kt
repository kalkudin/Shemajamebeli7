package com.example.domain.home.model

data class DataObject(
    val number : Int?,
    val iconPath : Int?,
    val functionality : ItemFunctionality

) {
    enum class ItemFunctionality{
        SCAN,
        DELETE,
        NONE
    }
}