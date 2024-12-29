package com.example.donorbox.presentation.screens.mydonations

import androidx.compose.runtime.Stable

@Stable
data class MyDonationsUiState(
    val isLoading: Boolean = true,
    val list: List<String> = emptyList()
)
