package com.example.donorbox.presentation.screens.contactUs

import androidx.compose.runtime.Immutable

@Immutable
data class ContactUsUiState(
    val name: String = "",
    val title: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
)
