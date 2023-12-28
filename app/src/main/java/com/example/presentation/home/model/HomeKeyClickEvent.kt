package com.example.presentation.home.model

sealed class HomeKeyClickEvent {
    data class AddKeyToPassword(val number : Int) : HomeKeyClickEvent()
    data object DeletePasswordKey : HomeKeyClickEvent()
    data object ScanUserFingerPrint : HomeKeyClickEvent()
}