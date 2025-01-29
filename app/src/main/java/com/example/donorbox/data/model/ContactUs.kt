package com.example.donorbox.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class ContactUs(
    val name: String = "",
    val title: String = "",
    val message: String = "",
    val isVerified: Boolean = false
)
