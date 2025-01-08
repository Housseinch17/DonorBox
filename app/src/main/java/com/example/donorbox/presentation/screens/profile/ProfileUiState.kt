package com.example.donorbox.presentation.screens.profile

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val username: String = "",
    val name: String = "",
    val isLoading: Boolean = true,
)
