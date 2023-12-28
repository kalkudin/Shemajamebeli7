package com.example.presentation.home.events

sealed class HomeKeyClickEvent {
    data class AddKeyToPassword(val number : Int) : HomeKeyClickEvent()
    data object DeletePasswordKey : HomeKeyClickEvent()
    data object ScanUserFingerPrint : HomeKeyClickEvent()
}