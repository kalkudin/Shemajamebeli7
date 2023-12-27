package com.example.presentation.home
import com.example.presentation.home.model.Key

sealed class HomeButtonClick {
    data class ItemClick(val key: Key) : HomeButtonClick()
    data object DeleteClick : HomeButtonClick()
    data object ScanClick : HomeButtonClick()
}