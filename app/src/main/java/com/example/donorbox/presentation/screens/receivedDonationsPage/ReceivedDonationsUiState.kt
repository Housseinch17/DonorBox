package com.example.donorbox.presentation.screens.receivedDonationsPage

import androidx.compose.runtime.Stable

@Stable
data class ReceivedDonationsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val receivedDonationsList: List<String> = emptyList()
)
