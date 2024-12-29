package com.example.donorbox.presentation.screens.mydonations

import androidx.compose.runtime.Stable
import com.example.donorbox.data.model.MyDonations

@Stable
data class MyDonationsUiState(
    val isLoading: Boolean = true,
    val list: List<MyDonations> = emptyList(),
    val isRefreshing: Boolean = false,
)
