package com.example.data.common

sealed class Resource() {
    data class Success(val successMessage : String) : Resource()
    data class Failure(val failureMessage : String) : Resource()
    data object Nothing : Resource()
}