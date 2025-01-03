package com.example.donorbox.presentation.screens.main

import androidx.compose.runtime.Immutable
import com.example.donorbox.presentation.navigation.NavigationScreens

@Immutable
data class MainUiState(
    val currentScreen: NavigationScreens = NavigationScreens.Loading,
    val isLoading: Boolean = true
)
