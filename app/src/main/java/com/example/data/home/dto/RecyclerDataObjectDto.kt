package com.example.data.home.dto

data class RecyclerDataObjectDto(
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